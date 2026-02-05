package com.example.reservationsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.reservationsystem.entity.Reminder;
import com.example.reservationsystem.repository.ReminderRepository;
import com.example.reservationsystem.util.MacNotificationUtil;

@Service
public class ReminderNotificationService {

    private final ReminderRepository reminderRepository;

    public ReminderNotificationService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    // 1分ごとにチェック
    @Scheduled(fixedRate = 60000)
    public void notifyReminders() {
    	
    	System.out.println("scheduler running");

        LocalDateTime now = LocalDateTime.now()
            .withSecond(0)
            .withNano(0);

        List<Reminder> reminders = reminderRepository.findAll();

        for (Reminder r : reminders) {
            if (r.getDate() == null || r.getTime() == null) continue;

            LocalDateTime reminderTime =
                LocalDateTime.of(r.getDate(), r.getTime());

            if (reminderTime.equals(now)) {
                MacNotificationUtil.notify(
                    "リマインド",
                    r.getTitle()
                );
            }
        }
    }
}
