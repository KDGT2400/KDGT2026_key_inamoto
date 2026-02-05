package com.example.reservationsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reservationsystem.entity.User;
import com.example.reservationsystem.entity.UserLayoutSetting;

public interface UserLayoutSettingRepository
        extends JpaRepository<UserLayoutSetting, Long> {

    Optional<UserLayoutSetting> findByUser(User user);
}
