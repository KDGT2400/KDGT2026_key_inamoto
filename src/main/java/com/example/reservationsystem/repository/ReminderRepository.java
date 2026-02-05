package com.example.reservationsystem.repository;
// 日付・時間での検索に使う型
import java.time.LocalDate;
// 結果が 0 or 1 件のときに便利な Optional
import java.util.List;

// Spring Data JPA のリポジトリ基底インターフェース
import org.springframework.data.jpa.repository.JpaRepository;

// 予約エンティティを扱うためのインポート
import com.example.reservationsystem.entity.Reminder;
import com.example.reservationsystem.entity.User;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByUserAndDateBetween(
        User user,
        LocalDate start,
        LocalDate end
    );
    List<Reminder> findByUserOrderByDateAscTimeAsc(User user);
    List<Reminder> findByNotifiedFalse();
}
