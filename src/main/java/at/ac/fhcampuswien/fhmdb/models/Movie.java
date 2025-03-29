package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;
//test
public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;
    private final int releaseYear;
    private final double rating;



    public Movie(String title, String description, List<Genre> genres, int releaseYear, int rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", releaseYear=" + releaseYear +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title)
                && this.description.equals(other.description)
                && this.genres.equals(other.genres)
                && this.releaseYear == other.releaseYear
                && this.rating == other.rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public static List<Movie> initializeMovies() throws  Exception{
        MovieAPI movieAPI = new MovieAPI();
        return (List<Movie>) movieAPI.getFilmList(null,null,null,null);
    }

    public static List<Integer> getReleaseYearList() throws Exception {
        List<Integer> releaseYearList = new ArrayList<>();
        List<Movie> movieList = initializeMovies();
        for(Movie movie : movieList) {
            releaseYearList.add(movie.getReleaseYear());
        }
        return releaseYearList;
    }

    public static List<Double> getRatingList() throws Exception {
        List<Double> ratingList = new ArrayList<>();
        List<Movie> movieList = initializeMovies();
        for(Movie movie : movieList) {
            ratingList.add(movie.getRating());
        }
        return ratingList;
    }
}
