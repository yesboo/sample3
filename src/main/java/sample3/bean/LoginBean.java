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
import sample3.model.User;

@Named
@RequestScoped
public class LoginBean {
    private User user = new User(); // model.User を内包
    private String plainPass;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getPlainPass() {
        return plainPass;
    }

    public void setPlainPass(String plainPass){
        this.plainPass = plainPass;
    }

    public String login() {
        String sql = DaoUtil.loadSql("find_user.sql");

        try (Connection conn = ConDao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getAcname());
            ps.setString(2, DaoUtil.hash(getPlainPass())); // パスワードはハッシュ化して照合
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // ユーザー情報をBeanに格納mvn clean 
                    user.setId(rs.getInt("id"));
                    user.setAcname(rs.getString("acname"));
                    user.setRole(rs.getInt("role"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));

                    //loginUser Beanに渡す
                    LoginUser loginUserBean = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(),"#{loginUser}",LoginUser.class);
                    loginUserBean.setUser(user);
                    
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
