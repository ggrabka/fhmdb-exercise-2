package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String apiId;

    @DatabaseField
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private String genres; // gespeichert als "DRAMA,COMEDY"

    @DatabaseField
    private int releaseYear;

    @DatabaseField
    private String imgUrl;

    @DatabaseField
    private int lengthInMinutes;

    @DatabaseField
    private double rating;

    public MovieEntity() {
        // ORMLite braucht einen No-Arg-Konstruktor
    }

    public static String genresToString(List<Genre> genres) {
        return genres.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> entities = new ArrayList<>();
        for (Movie m : movies) {
            MovieEntity entity = new MovieEntity();
            entity.apiId = m.getId();
            entity.title = m.getTitle();
            entity.description = m.getDescription();
            entity.genres = genresToString(m.getGenres());
            entity.releaseYear = m.getReleaseYear();
            entity.imgUrl = m.getImgUrl();
            entity.lengthInMinutes = m.getLengthInMinutes();
            entity.rating = m.getRating();
            entities.add(entity);
        }
        return entities;
    }

    public static List<Movie> toMovies(List<MovieEntity> entities) {
        List<Movie> movies = new ArrayList<>();
        for (MovieEntity e : entities) {
            List<Genre> genres = Arrays.stream(e.genres.split(","))
                    .map(Genre::valueOf)
                    .collect(Collectors.toList());
            movies.add(new Movie(
                    e.apiId,
                    e.title,
                    e.description,
                    genres,
                    e.releaseYear,
                    e.rating,
                    e.imgUrl,
                    e.lengthInMinutes,
                    new ArrayList<>(), // keine Listen in DB gespeichert
                    new ArrayList<>(),
                    new ArrayList<>()
            ));
        }
        return movies;
    }

    public String getApiId() {
        return apiId;
    }




}
