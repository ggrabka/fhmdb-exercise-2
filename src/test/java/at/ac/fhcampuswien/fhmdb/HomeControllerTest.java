package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
        List<Movie> expected = movies.initializeMovies();
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
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "123456789",
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        2013,
                        8.4,
                        "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
                        180,
                        Arrays.asList("Martin Scorese"),
                        Arrays.asList("Terence Winter"),
                        Arrays.asList("Leonardo Di Caprio","Jonah Hill","Margot Robbie")
                        ),
                new Movie(
                        "23456789",
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY),
                        1995,
                        8.6,
                        "https://m.media-amazon.com/images/M/MV5BYTViNjMyNmUtNDFkNC00ZDRlLThmMDUtZDU2YWE4NGI2ZjVmXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                        106,
                        Arrays.asList("Bryan Singer"),
                        Arrays.asList("Christopher McQuarrie"),
                        Arrays.asList("Kevin Spacey", "Gabriel Byrne", "Chazz Palminteri")
                        ),
                new Movie(
                        "3456789",
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION),
                        2011,
                        6.6,
                        "https://m.media-amazon.com/images/M/MV5BNjMyMDBjMGUtNDUzZi00N2MwLTg1MjItZTk2MDE1OTZmNTYxXkEyXkFqcGdeQXVyMTQ5NjA0NDM0._V1_FMjpg_UX1000_.jpg",
                        90,
                        Arrays.asList("Chris Miller"),
                        Arrays.asList("Tom Wheeler", "Brian Lynch"),
                        Arrays.asList("Antonio Banderas","Salma Hayek","Zach Galifianakis")
                        ),
                new Movie(
                        "4367890",
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE),
                        1997,
                        8.6,
                        "https://m.media-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_FMjpg_UX1000_.jpg",
                        116,
                        Arrays.asList("Roberto Benigni"),
                        Arrays.asList("Vincenzo Cerami", "Roberto Benigni"),
                        Arrays.asList("Roberto Benigni","Nicoletta Braschi, Giorgio Cantarini")
                        ),
                new Movie(
                        "567890745",
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        2009,
                        7.8,
                        "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg",
                        162,
                        Arrays.asList("James Cameron"),
                        Arrays.asList("James Cameron"),
                        Arrays.asList("Sam Worthington", "Zoe Saldana","Sigourney Weaver")
                        )
        );

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
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "567890745",
                        "Avatar",
                        "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                        Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                        2009,
                        7.8,
                        "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg",
                        162,
                        Arrays.asList("James Cameron"),
                        Arrays.asList("James Cameron"),
                        Arrays.asList("Sam Worthington", "Zoe Saldana","Sigourney Weaver")
                ),
                new Movie(
                        "4367890",
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE),
                        1997,
                        8.6,
                        "https://m.media-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_FMjpg_UX1000_.jpg",
                        116,
                        Arrays.asList("Roberto Benigni"),
                        Arrays.asList("Vincenzo Cerami", "Roberto Benigni"),
                        Arrays.asList("Roberto Benigni","Nicoletta Braschi, Giorgio Cantarini")
                ),
                new Movie(
                        "3456789",
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION),
                        2011,
                        6.6,
                        "https://m.media-amazon.com/images/M/MV5BNjMyMDBjMGUtNDUzZi00N2MwLTg1MjItZTk2MDE1OTZmNTYxXkEyXkFqcGdeQXVyMTQ5NjA0NDM0._V1_FMjpg_UX1000_.jpg",
                        90,
                        Arrays.asList("Chris Miller"),
                        Arrays.asList("Tom Wheeler", "Brian Lynch"),
                        Arrays.asList("Antonio Banderas","Salma Hayek","Zach Galifianakis")
                ),
                new Movie(
                        "23456789",
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY),
                        1995,
                        8.6,
                        "https://m.media-amazon.com/images/M/MV5BYTViNjMyNmUtNDFkNC00ZDRlLThmMDUtZDU2YWE4NGI2ZjVmXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",
                        106,
                        Arrays.asList("Bryan Singer"),
                        Arrays.asList("Christopher McQuarrie"),
                        Arrays.asList("Kevin Spacey", "Gabriel Byrne", "Chazz Palminteri")
                ),
        new Movie(
                "123456789",
                "The Wolf of Wall Street",
                "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                2013,
                8.4,
                "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
                180,
                Arrays.asList("Martin Scorese"),
                Arrays.asList("Terence Winter"),
                Arrays.asList("Leonardo Di Caprio","Jonah Hill","Margot Robbie")
        ));

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
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "4367890",
                        "Life Is Beautiful",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp." ,
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE),
                        1997,
                        8.6,
                        "https://m.media-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_FMjpg_UX1000_.jpg",
                        116,
                        Arrays.asList("Roberto Benigni"),
                        Arrays.asList("Vincenzo Cerami", "Roberto Benigni"),
                        Arrays.asList("Roberto Benigni","Nicoletta Braschi, Giorgio Cantarini")
                ),
                new Movie(
                        "123456789",
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                        2013,
                        8.4,
                        "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
                        180,
                        Arrays.asList("Martin Scorese"),
                        Arrays.asList("Terence Winter"),
                        Arrays.asList("Leonardo Di Caprio","Jonah Hill","Margot Robbie")
                ));

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
        Genre genre = Genre.DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(4, actual.size());
    }

    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() throws Exception {
        // given
        homeController.initializeState();

        // when
        homeController.applyAllFilters("", null,0,null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void get_Most_Popular_Actor() throws Exception {
        //given
        homeController.initializeState();

        //when
       String mostPopularActor = homeController.getMostPopularActor(movies.initializeMovies());

        //then
        assertEquals("Leonardo DiCaprio",mostPopularActor);
    }

    @Test
    void get_Longest_Movie_Title() throws Exception {
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
    void get_movies_between_years() throws Exception {
        //given
        homeController.initializeState();

        //when
        List<Movie> movieList = homeController.getMoviesBetweenYears(movies.initializeMovies(), 2000, 2010);

        //then
        assertEquals(7,movieList.size());
    }

}