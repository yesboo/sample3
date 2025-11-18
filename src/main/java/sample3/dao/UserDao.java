package sample3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;

import sample3.model.User;

public class UserDao {

    public static List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id,acname,role,name,email FROM users ORDER BY id";

        try {
            Connection con = ConDao.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setAcname(rs.getString("acname"));
                u.setRole(rs.getInt("role"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                list.add(u);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("ユーザー一覧取得に失敗", e);
        }
        return list;
    }

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