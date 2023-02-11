package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@RestController
public class UserController {
    @Autowired
    private UserDao dao;
    @RequestMapping(path = "/users",method = RequestMethod.GET)
    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        users = dao.findAll();
        return users;
    }
    @RequestMapping(path = "/ids/{username}", method = RequestMethod.GET)
    public int findIdByUsername(@PathVariable String username){
        return dao.findIdByUsername(username);
    }
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public User getUserById(@RequestBody User user){
        return dao.getUserById(user.getId());
    }
    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public User findByUsername(@PathVariable String username){
        return dao.findByUsername(username);
    }
    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public void create(String username, String password){
        dao.create(username, password);
    }
}
