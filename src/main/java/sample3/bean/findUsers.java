package sample3.bean;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import sample3.dao.UserDao;
import sample3.model.User;

@Named
@RequestScoped
public class findUsers implements Serializable {
    private List<User> userList;
   private Integer selectedUserId;

   @PostConstruct
    public void init(){
        userList = UserDao.findAll();
    }
    public List<User> getUserList(){
        return userList;
    }
    public Integer getSelectedUserId(){
        return selectedUserId;
    }
    public void setSelectedUserId(Integer selectedUserId){
        this.selectedUserId = selectedUserId;
    }

    public String editUser(){
        //選択されている対象のユーザーIDを取得し、編集画面へ遷移
        return "profile.xhtml?faces-redirect=true&userId=" + selectedUserId;
    }
}