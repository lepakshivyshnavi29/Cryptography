
package com.example.cryptography.service;

import com.example.cryptography.entity.User;
import com.example.cryptography.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        String nodeId = generateUniqueNodeId();
        User user = new User(username, password, nodeId);
        return userRepository.save(user);
    }
    
    public User loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        throw new RuntimeException("Invalid credentials");
    }
    
    public Optional<User> findByNodeId(String nodeId) {
        return userRepository.findByNodeId(nodeId);
    }
    
    private String generateUniqueNodeId() {
        String nodeId;
        do {
            nodeId = "NODE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (userRepository.existsByNodeId(nodeId));
        return nodeId;
    }
}
