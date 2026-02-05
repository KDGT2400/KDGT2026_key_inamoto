package com.example.reservationsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_settings")
public class UserSetting {

    @Id
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String settingsJson;
}
