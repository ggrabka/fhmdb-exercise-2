package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return dao.queryForAll();
    }

    public int addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            return dao.create(movie);
        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Hinzufügen zur Watchlist", e);
        }
    }


    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("apiId", apiId);
            return dao.delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Entfernen aus der Watchlist", e);
        }
    }

}
