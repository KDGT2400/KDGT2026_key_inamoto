package com.example.reservationsystem.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 所有者
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // タイトル
    @Column(nullable = false)
    private String title;

    // 内容
    private String description;

    // 日付
    @Column(nullable = false)
    private LocalDate date;

    // 時刻（任意）
    private LocalTime time;

    @Column(name = "notify_before_minutes")
    private Integer notifyBeforeMinutes = 0;
    
    @Column(nullable = false)
    private boolean notified = false;
    // 完了フラグ
    private boolean completed = false;
    
    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
    
    
    
}
