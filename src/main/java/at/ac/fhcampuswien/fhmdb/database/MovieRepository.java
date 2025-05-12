package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private final Dao<MovieEntity, Long> dao;

    public MovieRepository(Dao<MovieEntity, Long> dao) {
        this.dao = dao;
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    public int removeAll() throws SQLException {
        return dao.deleteBuilder().delete();
    }

    public int addAllMovies(List<Movie> movies) throws SQLException {
        List<MovieEntity> entities = MovieEntity.fromMovies(movies);
        int counter = 0;
        for (MovieEntity entity : entities) {
            if (dao.createIfNotExists(entity) != null) counter++;
        }
        return counter;
    }

    public MovieEntity getMovie(String apiId) throws SQLException {
        List<MovieEntity> result = dao.queryBuilder()
                .where()
                .eq("apiId", apiId)
                .query();
        return result.isEmpty() ? null : result.get(0);
    }
}
