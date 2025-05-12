package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.*;
import at.ac.fhcampuswien.fhmdb.logic.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistMovieCell;
import at.ac.fhcampuswien.fhmdb.util.ErrorDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable {

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Object> genreComboBox;

    public JFXComboBox<Object> releaseYearComboBox;
    public JFXComboBox<Object> ratingComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;
    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    protected SortedState sortedState;

    private MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;

    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (movie) -> {
        try {
            watchlistRepository.removeFromWatchlist(movie.getId());
            observableMovies.remove(movie);
        } catch (DatabaseException e) {
            ErrorDialog.show("Fehler beim Entfernen des Films aus der Watchlist.");
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeState();
        } catch (Exception e) {
            ErrorDialog.show("Fehler beim Initialisieren der Watchlist.");
        }
        initializeLayout();
    }

    public void initializeState() throws Exception {
        DatabaseManager.createConnectionSource();
        DatabaseManager.createTables();

        movieRepository = new MovieRepository(DatabaseManager.getMovieDao());
        watchlistRepository = new WatchlistRepository(DatabaseManager.getWatchlistDao());

        List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();
        List<MovieEntity> allCached = movieRepository.getAllMovies();

        allMovies = allCached.stream()
                .filter(movie -> watchlist.stream().anyMatch(w -> w.getApiId().equals(movie.getApiId())))
                .map(movie -> MovieEntity.toMovies(List.of(movie)).get(0))
                .toList();

        observableMovies.clear();
        observableMovies.addAll(allMovies);
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(view -> new WatchlistMovieCell(onRemoveFromWatchlistClicked));

        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        List<Integer> releaseYearList = observableMovies.stream()
                .map(Movie::getReleaseYear)
                .distinct()
                .sorted()
                .toList();

        releaseYearComboBox.getItems().add("No release year");
        releaseYearComboBox.getItems().addAll(releaseYearList);
        releaseYearComboBox.setPromptText("Filter by Year");

        List<Double> ratingList = observableMovies.stream()
                .map(Movie::getRating)
                .distinct()
                .sorted()
                .toList();

        ratingComboBox.getItems().add("No rating");
        ratingComboBox.getItems().addAll(ratingList);
        ratingComboBox.setPromptText("Filter by Rating");
    }

    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String query = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        Object rating = ratingComboBox.getSelectionModel().getSelectedItem();

        List<Movie> filtered = allMovies;

        if (query != null && !query.isEmpty()) {
            filtered = filtered.stream()
                    .filter(m -> m.getTitle().toLowerCase().contains(query)
                            || m.getDescription().toLowerCase().contains(query))
                    .toList();
        }

        if (genre != null && !genre.equals("No filter")) {
            filtered = filtered.stream()
                    .filter(m -> m.getGenres().contains(Genre.valueOf(genre.toString())))
                    .toList();
        }

        if (releaseYear != null && !releaseYear.equals("No release year")) {
            filtered = filtered.stream()
                    .filter(m -> m.getReleaseYear() == Integer.parseInt(releaseYear.toString()))
                    .toList();
        }

        if (rating != null && !rating.equals("No rating")) {
            filtered = filtered.stream()
                    .filter(m -> m.getRating() == Double.parseDouble(rating.toString()))
                    .toList();
        }

        observableMovies.clear();
        observableMovies.addAll(filtered);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    public void homeBtnClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 890, 620);
            scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("FHMDb – Home");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorDialog.show("Fehler beim Laden des Home-Screens.");
        }
    }

    public void watchlistBtnClicked(ActionEvent actionEvent) {
        // nichts tun – wir sind bereits auf der Watchlist
    }

    // Zusatzfunktionen für Tests oder Statistik
    public String getMostPopularActor(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) return "";

        return movies.stream()
                .flatMap(m -> m.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) return 0;

        return movies.stream()
                .map(Movie::getTitle)
                .filter(Objects::nonNull)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        if (movies == null || director == null) return 0;

        return movies.stream()
                .filter(m -> m.getDirectors().contains(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        if (movies == null) return List.of();

        return movies.stream()
                .filter(m -> m.getReleaseYear() >= startYear && m.getReleaseYear() <= endYear)
                .toList();
    }
}

