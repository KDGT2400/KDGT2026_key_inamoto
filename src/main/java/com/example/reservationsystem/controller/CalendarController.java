// パッケージ宣言：このクラスの属するパッケージを指定
package com.example.reservationsystem.controller;
//日付型（年-月-日）
import java.time.LocalDate;
//時刻型（時:分:秒）
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
//一覧表示などで使うコレクション
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//日付・時刻のフォーマットをリクエストパラメータに適用するアノテーション
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
//MVC のコントローラクラスであることを示す
import org.springframework.stereotype.Controller;
//テンプレートへ値を受け渡すためのコンテナ
import org.springframework.ui.Model;
//ルーティング系アノテーション（HTTP メソッドやパスをマッピング）
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.reservationsystem.entity.Reminder;
import com.example.reservationsystem.entity.User;
import com.example.reservationsystem.repository.ReminderRepository;
import com.example.reservationsystem.repository.UserRepository;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    public CalendarController(ReminderRepository reminderRepository,
                              UserRepository userRepository) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showCalendar(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        Model model
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        LocalDate now = LocalDate.now();
        int y = (year != null) ? year : now.getYear();
        int m = (month != null) ? month : now.getMonthValue();

        YearMonth yearMonth = YearMonth.of(y, m);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<Reminder> reminders =
            reminderRepository.findByUserAndDateBetween(user, firstDay, lastDay);

        Map<LocalDate, List<Reminder>> reminderMap =
            reminders.stream().collect(Collectors.groupingBy(Reminder::getDate));

        List<List<LocalDate>> weeks = new ArrayList<>();
        List<LocalDate> week = new ArrayList<>();

        int startOffset = firstDay.getDayOfWeek().getValue();
        startOffset = (startOffset == 7) ? 0 : startOffset;

        for (int i = 0; i < startOffset; i++) {
            week.add(null);
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            week.add(LocalDate.of(y, m, day));
            if (week.size() == 7) {
                weeks.add(week);
                week = new ArrayList<>();
            }
        }

        if (!week.isEmpty()) {
            while (week.size() < 7) {
                week.add(null);
            }
            weeks.add(week);
        }

        User currentUser = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();
        
        
        model.addAttribute("weeks", weeks);
        model.addAttribute("year", y);
        model.addAttribute("month", m);
        model.addAttribute("reminderMap", reminderMap);
        model.addAttribute("reminders", reminders);
        model.addAttribute("user", currentUser);


        return "calendar";
    }


    @PostMapping("/new")
    public String createReminder(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam String title,
        @RequestParam(required = false) String description,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
        @RequestParam(required = false) Integer notifyBeforeMinutes
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setTitle(title);
        reminder.setDescription(description);
        reminder.setDate(date);
        reminder.setTime(time);
        reminder.setNotifyBeforeMinutes(
            notifyBeforeMinutes != null ? notifyBeforeMinutes : 0
        );
        reminder.setNotified(false);

        reminderRepository.save(reminder);
        return "redirect:/calendar";
    }


    
}
