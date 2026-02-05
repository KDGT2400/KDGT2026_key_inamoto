package com.example.reservationsystem.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.reservationsystem.entity.Reminder;
import com.example.reservationsystem.entity.User;
import com.example.reservationsystem.repository.ReminderRepository;
import com.example.reservationsystem.repository.UserRepository;

@Controller
@RequestMapping("/reminder")
public class ReminderController {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    public ReminderController(ReminderRepository reminderRepository,
                              UserRepository userRepository) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
    }

    // 登録画面表示
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("reminder", new Reminder());
        return "reminder_form";
    }
    
    @GetMapping("/reminder/new")
    public String showReminderForm(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        Model model
    ) {
        Reminder reminder = new Reminder();

        if (date != null) {
            reminder.setDate(date);
        }

        model.addAttribute("reminder", reminder);
        return "reminder-form";
    }


    // 登録処理
    @PostMapping("/new")
    public String createReminder(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam String title,
        @RequestParam(required = false) String description,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(required = false)
        Integer notifyBeforeMinutes,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        Reminder reminder = new Reminder();
        reminder.setTitle(title);
        reminder.setDescription(description);
        reminder.setDate(date);
        reminder.setTime(time);
        reminder.setUser(user);
        if (notifyBeforeMinutes == null) {
            notifyBeforeMinutes = 0;
        }
        reminder.setNotifyBeforeMinutes(notifyBeforeMinutes);
        reminder.setNotified(false);
        reminder.setUser(user);
        

        reminderRepository.save(reminder);

        return "redirect:/calendar";
    }
    
    
    
    @GetMapping("/{id}")
    public String showDetail(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        Reminder reminder = reminderRepository.findById(id)
            .orElseThrow();

        // 他人の予定を見れないようにする（重要）
        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("不正アクセス");
        }

        model.addAttribute("reminder", reminder);
        return "reminder-detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        Reminder reminder = reminderRepository.findById(id)
            .orElseThrow();

        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("不正アクセス");
        }

        model.addAttribute("reminder", reminder);
        return "reminder-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateReminder(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam String title,
        @RequestParam LocalDate date,
        @RequestParam(required = false) LocalTime time,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) Integer notifyBeforeMinutes
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        Reminder reminder = reminderRepository.findById(id)
            .orElseThrow();

        if (!reminder.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("権限がありません");
        }

        boolean dateTimeChanged =
            !reminder.getDate().equals(date) ||
            (reminder.getTime() != null && !reminder.getTime().equals(time));

        reminder.setTitle(title);
        reminder.setDate(date);
        reminder.setTime(time);
        reminder.setDescription(description);
        reminder.setNotified(false);

        if (notifyBeforeMinutes != null) {
            reminder.setNotifyBeforeMinutes(notifyBeforeMinutes);
        }

        // ★ 日時 or 通知設定が変わったら再通知可能にする
        if (dateTimeChanged) {
            reminder.setNotified(false);
        }

        reminderRepository.save(reminder);
        return "redirect:/calendar";
    }
// return "redirect:/dashboard";

    
    @PostMapping("/{id}/delete")
    public String deleteReminder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow();

        // ログインユーザー本人の予定かチェック
        if (!reminder.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("権限がありません");
        }

        reminderRepository.delete(reminder);

        return "redirect:/calendar";
    }
 
    
    

    
}
