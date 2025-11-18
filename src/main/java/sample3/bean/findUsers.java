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
    @PostConstruct
    public void init(){
        userList = UserDao.findAll();
    }
    public List<User> getUserList(){
        return userList;
    }
}