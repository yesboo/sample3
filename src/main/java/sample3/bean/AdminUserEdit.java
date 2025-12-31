package sample3.bean;
import java.io.Serializable;
import java.security.SecureRandom;

import org.postgresql.util.PSQLException;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import sample3.dao.DaoUtil;
import sample3.dao.UserDao;
import sample3.model.User;

@Named("adminUserEdit")
@ViewScoped
public class AdminUserEdit implements Serializable {
    private User targetUser;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("userId");

        if (id != null) {
            targetUser = UserDao.findById(Integer.parseInt(id));
        }
    }

    public User getTargetUser(){
        return targetUser;
    }

    public String resetPassword(){
        try {
            String newPass = generateRandomPassword();       
            String hashed = DaoUtil.hash(newPass);

            UserDao udao = new UserDao();
            udao.updatePassword(targetUser.getId(), hashed);
            return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String createUser() {
        try {
            // パスワードをハッシュ化（例：SHA-256やbcrypt）
//            targetUser.setHashedPassword(hash(newPassword));
//            new UserDao().insertUser(targetUser);
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
    
    private String generateRandomPassword() {
        int length = 12;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public void updateUser(){
        try {
            if (new UserDao().updateUser(targetUser) == 1){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ユーザー情報を更新しました。", null));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新に失敗しました。", null));
        }
    }

}