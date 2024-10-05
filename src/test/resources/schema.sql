CREATE TABLE IF NOT EXISTS students
(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  furigana VARCHAR(100) NOT NULL,
  nickname VARCHAR(100),
  email_address VARCHAR(254) NOT NULL,
  area VARCHAR(100),
  age INT NOT NULL,
  gender VARCHAR(10),
  remark VARCHAR(100),
  is_deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  course_name VARCHAR(100) NOT NULL,
  start_date TIMESTAMP,
  end_date TIMESTAMP
);
