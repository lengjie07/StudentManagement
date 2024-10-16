INSERT INTO students (full_name, furigana, nickname, email_address, area, age, gender, remark)
VALUES
('桐ヶ谷和人', 'キリガヤカズト', 'キリト', 'kirito@sao.com', '埼玉', 17, '男性', 'テスト'),
('結城明日奈', 'ユウキアスナ', 'アスナ', 'asuna@sao.com', '東京', 18, '女性', 'test'),
('綾野珪子', 'アヤノケイコ', 'シリカ', 'silica@sao.com', '神奈川', 15, '女性', 'てすと'),
('篠崎里香', 'シノザキリカ', 'リズベット', 'lisbeth@sao.com', '東京', 18, '女性', 'テスト'),
('壺井遼太郎', 'ツボイリョウタロウ', 'クライン', 'klein@sao.com', '千葉', 27, '男性', 'test');

INSERT INTO students_courses (student_id, course_name, start_date, end_date)
VALUES
(1, 'java', '2024-10-07 00:00:00', '2025-10-07 00:00:00'),
(1, 'aws', '2024-11-07 00:00:00', '2025-11-07 00:00:00'),
(2, 'デザイン', '2024-09-30 00:00:00', '2025-09-30 00:00:00'),
(3, 'java', '2024-10-04 00:00:00', '2025-10-04 00:00:00'),
(4, 'aws', '2024-05-18 00:00:00', '2025-05-18 00:00:00'),
(5, 'webマーケティング', '2024-12-01 00:00:00', '2025-12-01 00:00:00');

INSERT INTO course_application_statuses (course_id, status)
VALUES
(1, '本申込'),
(2, '仮申込'),
(3, '受講中'),
(4, '受講中'),
(5, '受講終了'),
(6, '仮申込');
