package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
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
    public JFXButton homeBtn;

    @FXML
    public JFXButton wishlistBtn;

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    public JFXComboBox releaseYearComboBox;

    public JFXComboBox ratingComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    public MovieAPI movieAPI = new MovieAPI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initializeLayout();
    }

    public void initializeState () throws Exception {
        allMovies = Movie.initializeMovies();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        List<Integer> releaseYearList = new ArrayList<>();
        for(Movie movie : observableMovies) {
            if(!releaseYearList.contains(movie.getReleaseYear())) {
                releaseYearList.add(movie.getReleaseYear());
            }
        }
        releaseYearList.sort(Comparator.naturalOrder());
        releaseYearComboBox.getItems().add("No release year");
        releaseYearComboBox.getItems().addAll(releaseYearList);
        releaseYearComboBox.setPromptText("Filter by Release Year");

        List<Double> ratingList = new ArrayList<>();
        for(Movie movie : observableMovies) {
            if(!ratingList.contains(movie.getRating())){
                ratingList.add(movie.getRating());
            }
        }
        ratingList.sort(Comparator.naturalOrder());
        ratingComboBox.getItems().add("No rating");
        ratingComboBox.getItems().addAll(ratingList);
        ratingComboBox.setPromptText("Filter By Rating");
        releaseYearComboBox.setPromptText("Filter by Year");
    }

    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                    movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }

    public List<Movie> filterByReleaseYear(List<Movie> movies,int releaseYear) {
        if(releaseYear == 0) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getReleaseYear() == releaseYear)
                .toList();
    }

    public List<Movie> filterByRating(List<Movie> movies, double rating) {
        if(rating == 0.0) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getRating() == rating)
                .toList();
    }

    public void applyAllFilters(String searchQuery, Object genre, Object releaseYear, Object rating) throws Exception{
        String genreStr = (genre != null) ? genre.toString() : null;
        List<Movie> filteredMovies = (List<Movie>) movieAPI.getFilmList(searchQuery,genreStr,releaseYear,rating);

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        if (releaseYear != null && !releaseYear.toString().equals("No release year")) {
            filteredMovies = filterByReleaseYear(filteredMovies, Integer.valueOf(releaseYear.toString()));
        }

        if (rating != null && !rating.toString().equals("No rating")) {
            filteredMovies = filterByRating(filteredMovies, Double.valueOf(rating.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void homeBtnClicked(ActionEvent actionEvent) {
        //Button should be greyed out
    }

    public void watchlistBtnClicked(ActionEvent actionEvent) {
        //toDo
    }

    public void searchBtnClicked(ActionEvent actionEvent) throws Exception {
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        Object rating = ratingComboBox.getSelectionModel().getSelectedItem();

        applyAllFilters(searchQuery, genre,releaseYear,rating);
        sortMovies(sortedState);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    String getMostPopularActor(List<Movie> movies) {
        if(movies.size()==0 || movies.stream().flatMap(movie -> movie.getMainCast().stream()).count() == 0) {
            return "";
        }
        List<String> actors = movies
                .stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.toList());

        Map<String, Long> frequencyMap = actors.stream()
                .collect(Collectors.groupingBy(
                        actor -> actor,
                        Collectors.counting()
                ));

        String maxValuesString = frequencyMap.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        return maxValuesString;
    }

    int getLongestMovieTitle(List<Movie> movies) {
        if(movies.size()==0 || movies.stream().allMatch(movie -> movie.getTitle() == null)) {
            return 0;
        }
        List<String> titles = movies
                .stream()
                .map(movie -> movie.getTitle())
                .collect(Collectors.toList());

        String longestMovieTitle = titles
                .stream()
                .max(Comparator.comparingInt(String::length)).get();
        return longestMovieTitle.length();
    }

    long countMoviesFrom(List<Movie> movies, String director) {
        List<Movie> moviesFromDirector = movies
                .stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .collect(Collectors.toList());
        return moviesFromDirector.size();
    }

    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        List<Movie> moviesBetweenYears = movies
                .stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());

        return moviesBetweenYears;
    }
}