package sample3.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConDao {
    private static HikariDataSource dataSource;

    private static synchronized void initDataSource() {
        if (dataSource != null) return;

        Properties props = new Properties();
        try (InputStream in = ConDao.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found in classpath");
            }
            props.load(in);
            HikariConfig config = new HikariConfig(props);
            dataSource = new HikariDataSource(config);  
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize HikariDataSource", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initDataSource();
        }
        return dataSource.getConnection();
    }
}