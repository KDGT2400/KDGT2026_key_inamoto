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
@Table(name = "shift")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 担当スタッフ
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    // 勤務日
    @Column(nullable = false)
    private LocalDate date;

    // 開始時刻
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // 終了時刻
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
