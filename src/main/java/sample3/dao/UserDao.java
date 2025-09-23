package sample3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

import sample3.model.User;

public class UserDao {

    public void insertUser(User user) throws Exception {
        String sql = "INSERT INTO users (acname, password, role, name, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConDao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getAcname());
            ps.setString(2, user.getPassword()); // ハッシュ済み
            ps.setInt(3, user.getRole());
            ps.setString(4, user.getName());
            ps.setString(5, user.getEmail());

            ps.executeUpdate();
        } catch (PSQLException e) {
            System.err.println("PostgreSQL例外発生:");
            System.err.println("メッセージ: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("サーバエラー: " + e.getServerErrorMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL例外発生:");
            System.err.println("メッセージ: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }

    }
}