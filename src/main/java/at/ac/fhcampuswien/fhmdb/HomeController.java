package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.logic.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.HomeMovieCell;
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

public class HomeController implements Initializable {

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

    public MovieAPI movieAPI = new MovieAPI();
    private MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;

    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedMovie) -> {
        try {
            watchlistRepository.addToWatchlist(new WatchlistMovieEntity(clickedMovie.getApiId()));
        } catch (DatabaseException e) {
            ErrorDialog.show("Fehler beim Hinzufügen zur Watchlist.");
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeState();
        } catch (Exception e) {
            ErrorDialog.show("Initialisierung fehlgeschlagen: " + e.getMessage());
        }
        initializeLayout();
    }

    public void initializeState() throws Exception {
        DatabaseManager.createConnectionSource();
        DatabaseManager.createTables();
        movieRepository = new MovieRepository(DatabaseManager.getMovieDao());
        watchlistRepository = new WatchlistRepository(DatabaseManager.getWatchlistDao());

        try {
            allMovies = Movie.initializeMovies();                 // API-Zugriff
            movieRepository.removeAll();                          // alte Filme löschen
            movieRepository.addAllMovies(allMovies);              // neue Filme speichern
        } catch (MovieApiException apiEx) {
            ErrorDialog.show("API nicht erreichbar. Lokale Daten werden angezeigt.");
            List<MovieEntity> cachedEntities = movieRepository.getAllMovies();
            allMovies = MovieEntity.toMovies(cachedEntities);
        } catch (DatabaseException dbEx) {
            ErrorDialog.show("Fehler beim Zugriff auf lokale Daten.");
        }

        observableMovies.clear();
        observableMovies.addAll(allMovies);
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(view -> new HomeMovieCell(onAddToWatchlistClicked));

        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        List<Integer> releaseYearList = observableMovies.stream()
                .map(Movie::getReleaseYear).distinct().sorted().toList();
        releaseYearComboBox.getItems().add("No release year");
        releaseYearComboBox.getItems().addAll(releaseYearList);
        releaseYearComboBox.setPromptText("Filter by Year");

        List<Double> ratingList = observableMovies.stream()
                .map(Movie::getRating).distinct().sorted().toList();
        ratingComboBox.getItems().add("No rating");
        ratingComboBox.getItems().addAll(ratingList);
        ratingComboBox.setPromptText("Filter By Rating");
    }

    public void sortMovies(SortedState sortedState) {
        if (this.sortedState == SortedState.NONE || this.sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            this.sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            this.sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;
        return movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase())
                        || movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;
        return movies.stream()
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }

    public List<Movie> filterByReleaseYear(List<Movie> movies, int releaseYear) {
        if (releaseYear == 0) return movies;
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() == releaseYear)
                .toList();
    }

    public List<Movie> filterByRating(List<Movie> movies, double rating) {
        if (rating == 0.0) return movies;
        return movies.stream()
                .filter(movie -> movie.getRating() == rating)
                .toList();
    }

    public void applyAllFilters(String searchQuery, Object genre, Object releaseYear, Object rating) throws Exception {
        String genreStr = (genre != null) ? genre.toString() : null;
        List<Movie> filteredMovies = (List<Movie>) movieAPI.getFilmList(searchQuery, genreStr, releaseYear, rating);

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        if (releaseYear != null && !releaseYear.toString().equals("No release year")) {
            filteredMovies = filterByReleaseYear(filteredMovies, Integer.parseInt(releaseYear.toString()));
        }

        if (rating != null && !rating.toString().equals("No rating")) {
            filteredMovies = filterByRating(filteredMovies, Double.parseDouble(rating.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void homeBtnClicked(ActionEvent actionEvent) {
        // bereits auf Home
    }

    public void watchlistBtnClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("watchlist-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 890, 620);
            scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("FHMDb – Watchlist");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorDialog.show("Watchlist-Ansicht konnte nicht geladen werden.");
        }
    }

    public void searchBtnClicked(ActionEvent actionEvent) throws Exception {
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        Object rating = ratingComboBox.getSelectionModel().getSelectedItem();

        applyAllFilters(searchQuery, genre, releaseYear, rating);
        sortMovies(sortedState);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies(sortedState);
        }


        // Utility-Methoden (optional beibehalten)
    String getMostPopularActor(List<Movie> movies) {
        if (movies.isEmpty() || movies.stream().flatMap(movie -> movie.getMainCast().stream()).count() == 0)
            return "";
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .map(Movie::getTitle)
                .filter(Objects::nonNull)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .toList();
    }
}
