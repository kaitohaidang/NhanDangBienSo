package com.example.license_plate_recognition.service;


import com.example.license_plate_recognition.model.User;
import com.example.license_plate_recognition.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        return user;
    }
}
