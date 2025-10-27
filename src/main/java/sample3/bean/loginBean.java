package sample3.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import sample3.dao.ConDao;
import sample3.dao.DaoUtil;

@Named
@RequestScoped
public class loginBean {
    private int id;
    private int role;
    private String name;
    private String email;
    private String acname;
    private String password;

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }   

    public String getEmail() {
        return email;
    } 
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }   

    public String getAcname() {
        return acname;
    }
    public void setAcname(String acname) {
        this.acname = acname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    private String hash(String plain) {
        return DaoUtil.hash(plain);
    }
    
    public String login() {
        String sql = DaoUtil.loadSql("find_user.sql");

        try (Connection conn = ConDao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, getAcname());
            ps.setString(2, DaoUtil.hash(getPassword())); // パスワードはハッシュ化して照合
            String xPass = hash(getPassword());
            System.out.println(xPass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // ユーザー情報をBeanに格納
                    this.id = rs.getInt("id");
                    this.acname = rs.getString("acname");
                    this.role = rs.getInt("role");
                    this.name = rs.getString("name");
                    this.email = rs.getString("email");

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", this);
                      return "menu?faces-redirect=true";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "エラー", "データベース接続に失敗しました"));
            return null;
        }

        // 認証失敗
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "ログイン失敗", "アカウント名またはパスワードが違います"));
        return null;
    }
}
