// パッケージ宣言：このコントローラの論理的な配置先
package com.example.reservationsystem.controller;
import java.time.LocalDate;

// 認証済みユーザの principal（UserDetails）を引き当てるためのアノテーション
import org.springframework.security.core.annotation.AuthenticationPrincipal;
// Spring Security が扱うユーザ情報の標準インタフェース
import org.springframework.security.core.userdetails.UserDetails;
// MVC のコントローラであることを示す
import org.springframework.stereotype.Controller;
// テンプレートに値を渡すためのモデル
import org.springframework.ui.Model;
// GET リクエストのハンドラマッピング
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// User エンティティ：現在ログイン中のユーザ情報を取得・判定に使用
import com.example.reservationsystem.entity.User;
import com.example.reservationsystem.repository.ReminderRepository;
// 予約検索のための JPA リポジトリ（ダッシュボード表示データ取得に使用）
import com.example.reservationsystem.repository.ReservationRepository;
// ユーザ検索のための JPA リポジトリ（メールで User を引く）
import com.example.reservationsystem.repository.UserRepository;
// このクラスが MVC コントローラであることを表明
@Controller
public class DashboardController {
// ユーザ情報へアクセスするためのリポジトリ（メールから User を取得）
private final UserRepository userRepository;
// 予約情報へアクセスするためのリポジトリ（役割別に表示内容を切り替える）
private final ReservationRepository reservationRepository;
private final ReminderRepository reminderRepository;


// 依存コンポーネント（リポジトリ）をコンストラクタで受け取り、DI する
public DashboardController(UserRepository userRepository, 
		                   ReservationRepository reservationRepository,
		                   ReminderRepository reminderRepository) {
// フィールド userRepository に代入
this.userRepository = userRepository;
// フィールド reservationRepository に代入
this.reservationRepository = reservationRepository;

this.reminderRepository = reminderRepository;
}

//ダッシュボードのルート。ログイン後の遷移先
@GetMapping("/dashboard")
public String dashboard(
    @AuthenticationPrincipal UserDetails userDetails,
    @RequestParam(required = false) Integer year,
    @RequestParam(required = false) Integer month,
    Model model
) {
    User currentUser = userRepository
        .findByEmail(userDetails.getUsername())
        .orElseThrow();
    
    model.addAttribute("user", currentUser);
    
    
    
    LocalDate today = LocalDate.now();
    model.addAttribute("today", today);
    
    int targetYear = (year != null) ? year : today.getYear();
    int targetMonth = (month != null) ? month : today.getMonthValue();

    LocalDate start = LocalDate.of(targetYear, targetMonth, 1);
    LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
    
    

    if (currentUser.getRole().equals("CUSTOMER")) {

        model.addAttribute(
            "reminders",
            reminderRepository.findByUserAndDateBetween(currentUser, start, end)
        );

        return "customer_dashboard";
    }

    return "redirect:/login";
}



}