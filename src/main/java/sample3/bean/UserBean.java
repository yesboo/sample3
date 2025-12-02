package sample3.bean;

import org.postgresql.util.PSQLException;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import sample3.dao.UserDao;
import sample3.model.User;

@Named
@RequestScoped
public class UserBean {
    private User user = new User();

    @PostConstruct
    public void init(){
        //リクエストパラメータからuserIDを取得
        FacesContext  context = FacesContext.getCurrentInstance();
        String strUID = context.getExternalContext().getRequestParameterMap().get("userId");

        if (strUID != null){
            try {
                Integer intUID = Integer.parseInt(strUID);
                user = UserDao.findById(intUID);
            } 
            catch (NumberFormatException  e) {
            }
        }
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
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

    public String updateUser(){
        try {
            if (new UserDao().updateUser(user) == 1){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ユーザー情報を更新しました。", null));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
            return null;   
        }
    }
}