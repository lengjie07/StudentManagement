package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  /*
    初期のstudentsテーブル
    ('桐ヶ谷和人', 'キリガヤカズト', 'キリト', 'kirito@sao.com', '埼玉', 17, '男性'),
    ('結城明日奈', 'ユウキアスナ', 'アスナ', 'asuna@sao.com', '東京', 18, '女性'),
    ('綾野珪子', 'アヤノケイコ', 'シリカ', 'silica@sao.com', '神奈川', 15, '女性'),
    ('篠崎里香', 'シノザキリカ', 'リズベット', 'lisbeth@sao.com', '東京', 18, '女性'),
    ('壺井遼太郎', 'ツボイリョウタロウ', 'クライン', 'klein@sao.com', '千葉', 27, '男性');
   */

  /*
   * 初期のstudents_coursesテーブル
   * (1, 'java', ' 2024-10-07 00:00:00', '2025-10-07 00:00:00'),
   * (1, 'aws', '2024-11-07 00:00:00', '2025-11-07 00:00:00'),
   * (2, 'デザイン', '2024-09-30 00:00:00', '2025-09-30 00:00:00'),
   * (3, 'java', '2024-10-04 00:00:00', '2025-10-04 00:00:00'),
   * (4, 'aws', '2024-05-18 00:00:00', '2025-05-18 00:00:00'),
   * (5, 'webマーケティング', '2024-12-01 00:00:00', '2025-12-01 00:00:00');
   */

  @Test
  void 受講生情報の全件検索が行えること() {
    List<Student> actual = sut.searchStudent();

    assertThat(actual.size()).isEqualTo(5);
    assertThat(actual).extracting("fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "結城明日奈", "綾野珪子", "篠崎里香",
            "壺井遼太郎");
  }

  @Test
  void コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourse();

    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual).extracting("courseName")
        .containsExactlyInAnyOrder("java", "aws", "デザイン", "java", "aws", "webマーケティング");
  }

  @Test
  void IDで指定した受講生情報の検索が行えること() {
    int id = 1;
    Student actual = sut.findStudentById(id);

    assertThat(actual.getFullName()).isEqualTo("桐ヶ谷和人");
  }

  @Test
  void 受講生IDで指定した受講生のコース情報検索が行えること() {
    int id = 1;
    List<StudentCourse> actual = sut.findStudentCoursesByStudentId(id);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.getFirst().getStudentId()).isEqualTo(id);
    assertThat(actual.get(1).getStudentId()).isEqualTo(id);
  }

  @Test
  void 新規受講生情報の登録が行えること() {
    Student student = new Student();
    student.setFullName("桐ヶ谷直葉");
    student.setFurigana("キリガヤスグハ");
    student.setNickname("リーファ");
    student.setEmailAddress("leafa@sao.com");
    student.setArea("埼玉");
    student.setAge(16);
    student.setGender("女性");

    sut.insertStudent(student);

    List<Student> actual = sut.searchStudent();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 新規コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourseName("java");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    sut.insertStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse();
    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void 受講生情報の更新が行えること() {
    Student student = sut.findStudentById(1);
    student.setNickname("黒の剣士"); // 元のニックネームは"キリト"

    sut.updateStudent(student);

    Student actual = sut.findStudentById(1);
    assertThat(actual.getNickname()).isEqualTo("黒の剣士");
  }

  @Test
  void コース情報の更新が行えること() {
    StudentCourse studentCourse = sut.findStudentCoursesByStudentId(1).getFirst();
    studentCourse.setCourseName("デザイン"); // 元のコース名は"java"

    sut.updateStudentCourse(studentCourse);

    StudentCourse actual = sut.findStudentCoursesByStudentId(1).getFirst();
    assertThat(actual.getCourseName()).isEqualTo("デザイン");
  }
}
