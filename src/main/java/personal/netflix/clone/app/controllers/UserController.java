package personal.netflix.clone.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.netflix.clone.app.entities.User;
import personal.netflix.clone.app.services.UserService;
import personal.netflix.clone.app.services.VideoInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUsers();
    }
}
