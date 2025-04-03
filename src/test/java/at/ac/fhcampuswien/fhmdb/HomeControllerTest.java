package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static at.ac.fhcampuswien.fhmdb.models.Genre.DRAMA;
import static org.junit.jupiter.api.Assertions.*;



class HomeControllerTest {
    private static HomeController homeController;
    private static Movie movies;
    @BeforeAll
    static void init() {
        homeController = new HomeController();
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() throws Exception {
        homeController.initializeState();
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() throws Exception {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.NONE;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Movie.initializeMovies();
        expected.sort(Comparator.comparing(movie -> movie.getTitle()));
        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void if_last_sort_ascending_next_sort_should_be_descending() throws Exception {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.ASCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Movie.initializeMovies();
        expected.sort(Comparator.comparing(Movie::getTitle).reversed());
        assertEquals(expected, homeController.observableMovies);
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() throws Exception {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.DESCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Movie.initializeMovies();
        expected.sort(Comparator.comparing(movie -> movie.getTitle()));
        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters() throws Exception {
        // given
        homeController.initializeState();
        String query = "IfE";

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        List<Movie> expected = Movie.initializeMovies()
                        .stream()
                        .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase())
                        || movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                        .toList();

        assertEquals(expected, actual);
    }

    @Test
    void query_filter_with_null_movie_list_throws_exception() throws Exception {
        // given
        homeController.initializeState();
        String query = "IfE";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, query));
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() throws Exception {
        // given
        homeController.initializeState();
        String query = null;

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() throws Exception {
        // given
        homeController.initializeState();
        Genre genre = null;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_returns_all_movies_containing_given_genre() throws Exception {
        // given
        homeController.initializeState();
        Genre genre = DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(22, actual.size());
    }

    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() throws Exception {
        // given
        homeController.initializeState();

        // when
        homeController.applyAllFilters("", null,null,null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void get_most_opular_actor() throws Exception {
        //given
        homeController.initializeState();

        //when
       String mostPopularActor = homeController.getMostPopularActor(movies.initializeMovies());

        //then
        assertEquals("Leonardo DiCaprio",mostPopularActor);
    }

    @Test
    public void get_most_popular_actor_empty_movie_list() {
        List<Movie> movies = Collections.emptyList();
        String mostPopular = homeController.getMostPopularActor(movies);
        assertEquals("", mostPopular);
    }

    @Test
    public void get_most_popular_actor_movies_with_empty_casts() {
        List<Movie> movies = Arrays.asList(
                new Movie("123456789"
                        ,"Movie1"
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Collections.emptyList()
                        ,Collections.emptyList()
                        ,Collections.emptyList()));
        String mostPopular = homeController.getMostPopularActor(movies);
        assertEquals("", mostPopular);
    }

    @Test
    public void get_most_popular_actor_single_movie_single_actor() {
        List<Movie> movies = Collections.singletonList(
                new Movie("123456789"
                        ,"Movie1"
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Collections.emptyList()
                        ,Collections.emptyList()
                        ,Arrays.asList("Mike Tester"))
        );
        String mostPopular = homeController.getMostPopularActor(movies);
        assertEquals("Mike Tester", mostPopular);
    }

    @Test
    public void get_longest_movie_title_empty_movie_list() {
        List<Movie> movies = Collections.emptyList();
        int longestMovie = homeController.getLongestMovieTitle(movies);
        assertEquals(0, longestMovie);
    }

    @Test
    public void get_longest_movie_title_with_empty_title() {
        List<Movie> movies = Arrays.asList(
                new Movie("123456789"
                        ,null
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Collections.emptyList()
                        ,Collections.emptyList()
                        ,Collections.emptyList()));
        int longestTitle = homeController.getLongestMovieTitle(movies);
        assertEquals(0, longestTitle);
    }

    @Test
    public void get_longest_title_single_movie_single_title() {
        List<Movie> movies = Collections.singletonList(
                new Movie("123456789"
                        ,"Movie1"
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Collections.emptyList()
                        ,Collections.emptyList()
                        ,Arrays.asList("Mike Tester"))
        );
        int longestTitle = homeController.getLongestMovieTitle(movies);
        assertEquals(6, longestTitle);
    }

    @Test
    void get_longest_movie_title() throws Exception {
        //given
        homeController.initializeState();

        //when
        int longestMovieTitleLength = homeController.getLongestMovieTitle(movies.initializeMovies());

        //then
        assertEquals(46,longestMovieTitleLength);
    }

    @Test
    void count_movies_from() throws Exception {
        //given
        homeController.initializeState();

        //when
        long movieCount = homeController.countMoviesFrom(movies.initializeMovies(),"Martin Scorsese");

        //then
        assertEquals(2,movieCount);
    }

    @Test
    public void count_movies_from_empty_movie_list() {
        List<Movie> movies = Collections.emptyList();
        long movieCount = homeController.countMoviesFrom(movies,"Martin Scorsese");
        assertEquals(0, movieCount);
    }

    @Test
    public void count_movies_with_empty_director() {
        List<Movie> movies = Arrays.asList(
                new Movie("123456789"
                        ,null
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Collections.emptyList()
                        ,Collections.emptyList()
                        ,Collections.emptyList()));
        long movieCount = homeController.countMoviesFrom(movies,"Martin Scorsese");
        assertEquals(0, movieCount);
    }

    @Test
    public void count_movies_single_movie_single_director() {
        List<Movie> movies = Collections.singletonList(
                new Movie("123456789"
                        ,"Movie1"
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Arrays.asList("Martin Scorsese")
                        ,Collections.emptyList()
                        ,Arrays.asList("Mike Tester"))
        );
        long movieCount = homeController.countMoviesFrom(movies,"Martin Scorsese");
        assertEquals(1, movieCount);
    }


    @Test
    void get_movies_between_years() throws Exception {
        //given
        homeController.initializeState();

        //when
        List<Movie> movieList = homeController.getMoviesBetweenYears(movies.initializeMovies(), 2000, 2010);

        //then
        assertEquals(7,movieList.size());
    }

    @Test
    public void get_movies_between_years_empty_movie_list() {
        List<Movie> movies = Collections.emptyList();
        List<Movie> movieList = homeController.getMoviesBetweenYears(movies,2000,2010);
        assertEquals(Collections.emptyList(), movieList);
    }

    @Test
    public void get_movies_between_years_single_movie() {
        List<Movie> movies = Collections.singletonList(
                new Movie("123456789"
                        ,"Movie1"
                        , "Test"
                        , Collections.singletonList(DRAMA)
                        ,1990
                        ,7.0
                        ,"test"
                        ,120
                        ,Arrays.asList("Martin Scorsese")
                        ,Collections.emptyList()
                        ,Arrays.asList("Mike Tester"))
        );
        List<Movie> movieList = homeController.getMoviesBetweenYears(movies,1990,1991);
        assertEquals(1, movieList.size());
    }

}