package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;
//test
public class Movie {
    private final String id;
    private final String title;
    private final String description;
    private final List<Genre> genres;
    private final int releaseYear;
    private final double rating;
    private final String imgUrl;
    private final int lengthInMinutes;
    private final List<String> directors;
    private final List<String> writers;
    private final List<String> mainCast;



    public Movie(String id, String title, List<Genre> genres, String description, int releaseYear, double rating, String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers, List<String> mainCast) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", releaseYear=" + releaseYear +
                ", rating=" + rating +
                ", imgUrl='" + imgUrl + '\'' +
                ", lengthInMinutes=" + lengthInMinutes +
                ", directors=" + directors +
                ", writers=" + writers +
                ", mainCast=" + mainCast +
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

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getMainCast() {
        return mainCast;
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
