package raisetech.studentmanagement.controller.converter;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentCourseDetail;
import raisetech.studentmanagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }
  
  @Test
  void 受講生リストとコース情報リストを組み合わせて受講生詳細リストに変換できること() {
    // テスト用の受講生情報
    Student student1 = new Student(1, "氏名", "フリガナ", "ニックネーム", "test@sample.com", "地域",
        99, "性別", "備考", false);
    Student student2 = new Student(2, "氏名", "フリガナ", "ニックネーム", "test@sample.com", "地域",
        99, "性別", "備考", false);

    // テスト用のコース情報
    StudentCourse course1 = new StudentCourse(1, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course2 = new StudentCourse(2, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course3 = new StudentCourse(3, 2, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    // テスト用の申し込み状況
    CourseApplicationStatus status1 = new CourseApplicationStatus(1, 1, "仮申込");
    CourseApplicationStatus status2 = new CourseApplicationStatus(2, 2, "本申込");
    CourseApplicationStatus status3 = new CourseApplicationStatus(3, 3, "受講中");

    // 各情報をリスト化
    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = Arrays.asList(course1, course2, course3);
    List<CourseApplicationStatus> courseApplicationStatusList = Arrays.asList(status1, status2,
        status3);

    // 各リストを受講生詳細リストに変換
    List<StudentDetail> actual = sut.convertStudentDetailList(studentList, studentCourseList,
        courseApplicationStatusList);

    // 検証
    assertStudentDetailList(actual, student1, course1, course2, status1, status2, student2, course3, status3);
  }

  @Test
  void 重複して取得された受講生情報を一つの受講生詳細にまとめられること() {
    // テスト用の受講生情報
    Student student1 = new Student(1, "氏名", "フリガナ", "ニックネーム", "test@sample.com", "地域",
        99, "性別", "備考", false);
    Student student2 = new Student(2, "氏名", "フリガナ", "ニックネーム", "test@sample.com", "地域",
        99, "性別", "備考", false);

    // テスト用のコース情報
    StudentCourse course1 = new StudentCourse(1, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course2 = new StudentCourse(2, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course3 = new StudentCourse(3, 2, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    // テスト用の申し込み状況
    CourseApplicationStatus status1 = new CourseApplicationStatus(1, 1, "仮申込");
    CourseApplicationStatus status2 = new CourseApplicationStatus(2, 2, "本申込");
    CourseApplicationStatus status3 = new CourseApplicationStatus(3, 3, "受講中");

    // StudentCourseDetailを作成
    StudentCourseDetail courseDetail1 = new StudentCourseDetail(course1, status1);
    StudentCourseDetail courseDetail2 = new StudentCourseDetail(course2, status2);
    StudentCourseDetail courseDetail3 = new StudentCourseDetail(course3, status3);

    // StudentCourseDetailをリスト化
    List<StudentCourseDetail> courseDetailList1 = new ArrayList<>();
    courseDetailList1.add(courseDetail1);
    List<StudentCourseDetail> courseDetailList2 = new ArrayList<>();
    courseDetailList2.add(courseDetail2);
    List<StudentCourseDetail> courseDetailList3 = new ArrayList<>();
    courseDetailList3.add(courseDetail3);

    // StudentDetailを作成
    StudentDetail detail1 = new StudentDetail(student1, courseDetailList1);
    StudentDetail detail2 = new StudentDetail(student1, courseDetailList2);
    StudentDetail detail3 = new StudentDetail(student2, courseDetailList3);

    // StudentDetailをリスト化
    List<StudentDetail> studentDetailList = new ArrayList<>();
    studentDetailList.add(detail1);
    studentDetailList.add(detail2);
    studentDetailList.add(detail3);

    // 変換処理を実行
    List<StudentDetail> actual = sut.convertSearchedStudentDetailList(studentDetailList);

    // 検証
    assertStudentDetailList(actual, student1, course1, course2, status1, status2, student2, course3,
        status3);
  }

  // 共通の検証メソッド
  private void assertStudentDetailList(List<StudentDetail> actual, Student student1, StudentCourse course1,
      StudentCourse course2, CourseApplicationStatus status1, CourseApplicationStatus status2,
      Student student2, StudentCourse course3, CourseApplicationStatus status3) {
    // 受講生詳細リストのサイズを検証
    assertThat(actual.size()).isEqualTo(2);

    // 1人目の受講生詳細の中身を検証
    assertThat(actual.getFirst().getStudent()).isEqualTo(student1);
    List<StudentCourseDetail> studentCourseDetailList1 = actual.getFirst().getStudentCourseDetailList();
    assertThat(studentCourseDetailList1).extracting(StudentCourseDetail::getStudentCourse).contains(
        course1, course2);
    assertThat(studentCourseDetailList1).extracting(StudentCourseDetail::getCourseApplicationStatus).contains(
        status1, status2);

    // 2人目の受講生詳細の中身を検証
    assertThat(actual.get(1).getStudent()).isEqualTo(student2);
    List<StudentCourseDetail> studentCourseDetailList2 = actual.get(1).getStudentCourseDetailList();
    assertThat(studentCourseDetailList2).extracting(StudentCourseDetail::getStudentCourse).contains(
        course3);
    assertThat(studentCourseDetailList2).extracting(StudentCourseDetail::getCourseApplicationStatus).contains(
        status3);
  }
}
