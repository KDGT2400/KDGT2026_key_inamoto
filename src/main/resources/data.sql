-- users テーブルに初期データを投入（顧客/スタッフ/管理者）
INSERT INTO users (name, email, password, role) VALUES
	-- 顧客 A（ログイン ID: customerA@example.com / 仮パスワード: password）
	('顧客 A', 'customerA@example.com', 'password', 'CUSTOMER'),
	-- スタッフ B（ログイン ID: staffB@example.com / 仮パスワード: STAFF_ROLE ※値がパスワード欄に入っている点に注意）
	('スタッフ B', 'staffB@example.com', 'STAFF_ROLE', 'STAFF'),
	-- 管理者 C（ログイン ID: adminC@example.com / 仮パスワード: adminpass）
	('管理者 C', 'adminC@example.com', 'adminpass', 'ADMIN');
-- 本日分のスタッフ B のシフトを投入（9:00〜17:00）
INSERT INTO shift (staff_id, record_date, start_time, end_time) VALUES
	-- サブクエリで staffB の ID を解決してから登録
	((SELECT id FROM users WHERE email = 'staffB@example.com'), CURRENT_DATE, '09:00:00', '17:00:00');