package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.StudentSearchCriteria;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  /*
    初期のstudentsテーブル
    ('桐ヶ谷和人', 'キリガヤカズト', 'キリト', 'kirito@sao.com', '埼玉', 17, '男性', 'テスト'),
    ('結城明日奈', 'ユウキアスナ', 'アスナ', 'asuna@sao.com', '東京', 18, '女性', 'test'),
    ('綾野珪子', 'アヤノケイコ', 'シリカ', 'silica@sao.com', '神奈川', 15, '女性', 'てすと'),
    ('篠崎里香', 'シノザキリカ', 'リズベット', 'lisbeth@sao.com', '東京', 18, '女性', 'テスト'),
    ('壺井遼太郎', 'ツボイリョウタロウ', 'クライン', 'klein@sao.com', '千葉', 27, '男性', 'test')
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

  /*
   *初期のcourse_application_statusesテーブル
   * (1, '本申込'),
   * (2, '仮申込'),
   * (3, '受講中'),
   * (4, '受講中'),
   * (5, '受講終了'),
   * (6, '仮申込');
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
  void コース申し込み状況の全件検索が行えること() {
    List<CourseApplicationStatus> actual = sut.searchCourseApplicationStatus();

    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual).extracting("status")
        .containsExactlyInAnyOrder("本申込", "仮申込", "受講中", "受講中", "受講終了", "仮申込");
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
  void コースIDで指定したコースの申し込み状況の検索が行えること() {
    int id = 1;
    CourseApplicationStatus actual = sut.findCourseApplicationStatusByCourseId(id);

    assertThat(actual.getStatus()).isEqualTo("本申込");
  }

  @Test
  void 条件を指定した検索で何も指定しなかったとき全件を取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "桐ヶ谷和人", "結城明日奈", "綾野珪子", "篠崎里香",
            "壺井遼太郎");
  }

  @Test
  void 名前を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setFullName("桐ヶ谷");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "桐ヶ谷和人");
  }

  @Test
  void フリガナを指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setFurigana("キリガヤ");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "桐ヶ谷和人");
  }

  @Test
  void ニックネームを指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setNickname("キリト");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "桐ヶ谷和人");
  }

  @Test
  void メールアドレスを指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setEmailAddress("kirito");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "桐ヶ谷和人");
  }

  @Test
  void 地域を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setArea("東京");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("結城明日奈", "篠崎里香");
  }

  @Test
  void 年齢を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setAge(18);

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("結城明日奈", "篠崎里香");
  }

  @Test
  void 性別を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setGender("女性");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(3);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("結城明日奈", "綾野珪子", "篠崎里香");
  }

  @Test
  void 備考を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setRemark("test");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("結城明日奈", "壺井遼太郎");
  }

  @Test
  void コース名を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setCourseName("java");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "綾野珪子");
  }

  @Test
  void 開始日を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setStartDate(LocalDateTime.parse("2024-10-07T00:00:00")); // 設定した日付以降のデータが取得される

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(3);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "桐ヶ谷和人", "壺井遼太郎");
  }

  @Test
  void 修了日を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setEndDate(LocalDateTime.parse("2025-10-07T00:00:00")); // 設定した日付以前のデータが取得される

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(4);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "結城明日奈", "綾野珪子", "篠崎里香");
  }

  @Test
  void 申し込み状況を指定して検索したときに正しいデータが取得できること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setStatus("受講中");

    List<StudentDetail> actual = sut.searchStudentDetail(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).extracting("student.fullName")
        .containsExactlyInAnyOrder("結城明日奈", "綾野珪子");
  }

  @Test
  void 新規受講生情報の登録が行えること() {
    Student student = new Student(6, "桐ヶ谷直葉", "キリガヤスグハ", "リーファ", "leafa@sao.com",
        "埼玉", 16, "女性", "", false);

    sut.insertStudent(student);

    List<Student> actual = sut.searchStudent();

    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual).extracting("fullName")
        .containsExactlyInAnyOrder("桐ヶ谷和人", "結城明日奈", "綾野珪子", "篠崎里香",
            "壺井遼太郎", "桐ヶ谷直葉");
  }

  @Test
  void 新規コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse(7, 1, "デザイン", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    sut.insertStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse();
    assertThat(actual.size()).isEqualTo(7);
    assertThat(actual).extracting("courseName")
        .containsExactlyInAnyOrder("java", "aws", "デザイン", "java", "aws", "webマーケティング",
            "デザイン");
  }

  @Test
  void 新規申し込み状況の登録が行えること() {
    CourseApplicationStatus courseApplicationStatus = new CourseApplicationStatus(7, 7, "仮申込");

    sut.insertCourseApplicationStatus(courseApplicationStatus);

    List<CourseApplicationStatus> actual = sut.searchCourseApplicationStatus();
    assertThat(actual.size()).isEqualTo(7);
    assertThat(actual).extracting("status")
        .containsExactlyInAnyOrder("本申込","仮申込","受講中","受講中","受講終了","仮申込","仮申込");
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

  @Test
  void 申し込み状況の更新が行えること() {
    CourseApplicationStatus courseApplicationStatus = sut.findCourseApplicationStatusByCourseId(2);
    courseApplicationStatus.setStatus("本申込"); // 元のステータスは"仮申込"

    sut.updateCourseApplicationStatus(courseApplicationStatus);

    CourseApplicationStatus actual = sut.findCourseApplicationStatusByCourseId(2);
    assertThat(actual.getStatus()).isEqualTo("本申込");
  }
}
