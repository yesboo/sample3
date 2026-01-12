package sample3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static User findById(Integer id){
        String sql = "SELECT id,password,acname,role,name,email FROM users WHERE id = ?";

        try {
            Connection con = ConDao.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            User u = new User();
            while(rs.next()){
                u.setId(rs.getInt("id"));
                u.setHashedPassword(rs.getString("password"));
                u.setAcname(rs.getString("acname"));
                u.setRole(rs.getInt("role"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
            }
            return u;
        } catch (Exception e) {
            throw new RuntimeException("ユーザー一覧取得に失敗", e);
        }
    }

    public void insertUser(User user) throws Exception {
        String sql = "INSERT INTO users (acname, password, role, name, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConDao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getAcname());
            ps.setString(2, user.getHashedPassword()); // ハッシュ済み
            ps.setInt(3, user.getRole());
            ps.setString(4, user.getName());
            ps.setString(5, user.getEmail());

            ps.executeUpdate();;
        } catch (SQLException e) {
            System.err.println("SQL例外発生:");
            System.err.println("メッセージ: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
    }

    public int updateUser(User user) throws Exception{
        String sql = "UPDATE users SET acname=?, role=?, name=?, email=? WHERE id = ?";

        try (Connection conn = ConDao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getAcname());
            ps.setInt(2, user.getRole());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getId());

            return ps.executeUpdate();  //更新されれば、１件以上を返す。
        } catch (SQLException e) {
            System.err.println("SQL例外発生:");
            System.err.println("メッセージ: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return 0;   //更新件数０件とする。
        }
    }
    public int updatePassword(int uid, String hashedPassword) throws Exception{
        String sql = "UPDATE users SET password='" +hashedPassword+ "' WHERE id = " +uid;

        try (Connection conn = ConDao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            return ps.executeUpdate();  //更新されれば、１件以上を返す。
        } catch (SQLException e) {
            System.err.println("SQL例外発生:");
            System.err.println("メッセージ: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
            return 0;   //更新件数０件とする。
        }
    }

}