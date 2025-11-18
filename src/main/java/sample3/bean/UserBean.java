package sample3.bean;

import org.postgresql.util.PSQLException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import sample3.dao.UserDao;
import sample3.model.User;

@Named
@RequestScoped
public class UserBean {
    private User user = new User();
   
    public User getUser() {
        return user;
    }

    public String createUser() {
        try {
            // パスワードをハッシュ化（例：SHA-256やbcrypt）
            user.setPassword(hash(user.getPassword()));
            new UserDao().insertUser(user);
            return "success.xhtml";
        } catch (Exception e) {
            if (e instanceof PSQLException) {
                PSQLException pe = (PSQLException) e;
                System.err.println(pe.getServerErrorMessage());
            }   
            e.printStackTrace();
            return "error.xhtml";
        }
    }

    private String hash(String plain) {
        // ハッシュ化処理（簡易例）
        return Integer.toHexString(plain.hashCode());
    }
}