package com.example.service;
 
import com.example.model.User;
import com.example.repo.UserRepo;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class UserService {
 

    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }
 
    public User createUser(User user) {
        return userRepository.save(user);
    }
 
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User login(String emailId, String password) {
        return userRepository.findByEmailIdAndPassword(emailId, password);
    }
    
}