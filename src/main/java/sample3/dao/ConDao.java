package sample3.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConDao {
    private static final HikariDataSource dataSource;
    static {
        Properties props = new Properties();
        try (InputStream in = ConDao.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("failed to load db.properties",e);
        }
        
        HikariConfig config = new HikariConfig(props);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
