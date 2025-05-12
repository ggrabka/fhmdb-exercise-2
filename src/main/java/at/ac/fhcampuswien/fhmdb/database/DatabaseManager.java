package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:file:./db/movies";
    private static final String USER = "user";
    private static final String PASSWORD = "pass";

    private static ConnectionSource connectionSource;

    public static void createConnectionSource() throws SQLException {
        if (connectionSource == null) {
            connectionSource = new JdbcConnectionSource(DB_URL, USER, PASSWORD);
        }
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    public static Dao<MovieEntity, Long> getMovieDao() throws SQLException {
        return com.j256.ormlite.dao.DaoManager.createDao(connectionSource, MovieEntity.class);
    }

    public static Dao<WatchlistMovieEntity, Long> getWatchlistDao() throws SQLException {
        return com.j256.ormlite.dao.DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
    }
}
