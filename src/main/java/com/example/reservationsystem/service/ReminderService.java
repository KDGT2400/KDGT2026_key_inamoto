package com.example.reservationsystem.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.example.reservationsystem.entity.Reminder;



@Service
public class ReminderService {

    public LocalDateTime getNotifyDateTime(Reminder reminder) {

        // 日付が無いものは通知不可
        if (reminder.getDate() == null) {
        	System.out.println("通知スキップ: time が null reminderId=" + reminder.getId());
            return null;
        }

        // 時刻が無い場合はデフォルト時刻（例：9:00）
        LocalTime time =
            reminder.getTime() != null
                ? reminder.getTime()
                : LocalTime.of(9, 0);

        LocalDateTime eventDateTime =
            LocalDateTime.of(reminder.getDate(), time);

        int minutes =
            reminder.getNotifyBeforeMinutes() != null
                ? reminder.getNotifyBeforeMinutes()
                : 0;

        return eventDateTime.minusMinutes(minutes);
    }
}

