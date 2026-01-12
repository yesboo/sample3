package sample3.bean;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import sample3.dao.DaoUtil;
import sample3.dao.UserDao;
import sample3.model.User;

@Named("loginUser")
@SessionScoped
public class LoginUser implements Serializable {
    private User user;

    private String currentPassword;   // 現在のパスワード（入力）
    private String newPassword;       // 新しいパスワード（入力）
    private String confirmPassword;   // 確認パスワード（入力）

    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String password) {
        this.currentPassword = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User loginUser){
        this.user = loginUser;
    }

    public boolean isAdmin(){
        //ログインしていてかつ、ロールが管理者の場合
        return user != null && user.getRole() == 0 ;
    }
    

    private String hash(String plain) {
        // ハッシュ化処理（簡易例）
        return Integer.toHexString(plain.hashCode());
    }

    public void updateUser(){
        try {
            if (new UserDao().updateUser(user) == 1){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ユーザー情報を更新しました。", null));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
        }
    }

    public String changePassword(){
        if (!newPassword.equals(confirmPassword)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "パスワードが一致しません", null));
              return null;
        }
        //DB更新
        int uid = user.getId();
        String hashedPassword = DaoUtil.hash(newPassword);
        String reDirectString = "";
        try {
            if (new UserDao().updatePassword(uid, hashedPassword) == 1){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ユーザー情報を更新しました。", null));
                reDirectString = "changePass.xhtml?faces-redirect=false";
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
                reDirectString = "changePass.xhtml?faces-redirect=false";
            }            
        } catch (Exception e) {
        }
        //画面再描画
        return reDirectString;
    }
    
    public String resetPassword(){
        String restPassword = "Password";
        //DB更新
        int uid = user.getId();
        String hashedPassword = DaoUtil.hash(restPassword);
        String reDirectString = "";
        try {
            if (new UserDao().updatePassword(uid, hashedPassword) == 1){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "パスワードを初期化しました。", null));
                reDirectString = "changePass.xhtml?faces-redirect=false";
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "パスワードの初期化に失敗しました。", null));
                reDirectString = "changePass.xhtml?faces-redirect=false";
            }            
        } catch (Exception e) {
        }
        //画面再描画
        return reDirectString;
    }

}