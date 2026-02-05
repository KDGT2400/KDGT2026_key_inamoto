package com.example.reservationsystem.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.reservationsystem.entity.Reminder;
import com.example.reservationsystem.repository.ReminderRepository;
import com.example.reservationsystem.service.ReminderService;
import com.example.reservationsystem.util.MacNotificationUtil;

@Component
public class ReminderScheduler {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderService reminderService;

    @Scheduled(fixedRate = 1000)
    public void checkReminders() {

        LocalDateTime now = LocalDateTime.now();

        List<Reminder> reminders =
            reminderRepository.findByNotifiedFalse();

        for (Reminder r : reminders) {

            LocalDateTime notifyTime =
                reminderService.getNotifyDateTime(r);

            if (notifyTime == null) continue;

            if (now.isAfter(notifyTime)
            	    && now.isBefore(notifyTime.plusSeconds(1))) {

            	    MacNotificationUtil.notify(
            	        "リマインド",
            	        r.getTitle()
            	    );

            	    r.setNotified(true);
            	    reminderRepository.save(r);
            	}

        }
    }

}
