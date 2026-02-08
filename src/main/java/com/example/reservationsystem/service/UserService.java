package com.example.reservationsystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.reservationsystem.entity.User;
import com.example.reservationsystem.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザー登録処理
    public void register(User user) {

        // メール重複チェック
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("すでに登録されているメールアドレスです");
        }

        // パスワード暗号化
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 初期テーマ（未設定なら）
        

        userRepository.save(user);
    }
}
