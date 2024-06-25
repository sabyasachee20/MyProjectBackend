package com.example.controller;
import com.example.model.User;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {
    
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
 
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
 
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
