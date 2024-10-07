package raisetech.studentmanagement.controller.converter;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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
    Student student1 = new Student(1, "氏名", "フリガナ", "ニックネーム", "test@sample.com", "地域",
        99, "性別", "備考", false);
    Student student2 = new Student(2, "氏名", "フリガナ", "ニックネーム", "test@sample.com", "地域",
        99, "性別", "備考", false);

    StudentCourse course1 = new StudentCourse(1, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course2 = new StudentCourse(2, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course3 = new StudentCourse(3, 2, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    CourseApplicationStatus status1 = new CourseApplicationStatus(1, 1, "仮申込");
    CourseApplicationStatus status2 = new CourseApplicationStatus(2, 2, "本申込");
    CourseApplicationStatus status3 = new CourseApplicationStatus(3, 3, "受講中");

    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = Arrays.asList(course1, course2, course3);
    List<CourseApplicationStatus> courseApplicationStatusList = Arrays.asList(status1, status2,
        status3);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        courseApplicationStatusList);

    assertThat(actual.size()).isEqualTo(2);

    assertThat(actual.getFirst().getStudent()).isEqualTo(student1);
    List<StudentCourseDetail> studentCourseDetails1 = actual.getFirst().getStudentCourseDetails();
    assertThat(studentCourseDetails1).extracting(StudentCourseDetail::getStudentCourse).contains(course1, course2);
    assertThat(studentCourseDetails1).extracting(StudentCourseDetail::getCourseApplicationStatus).contains(status1,status2);

    assertThat(actual.get(1).getStudent()).isEqualTo(student2);
    List<StudentCourseDetail> studentCourseDetails2 = actual.get(1).getStudentCourseDetails();
    assertThat(studentCourseDetails2).extracting(StudentCourseDetail::getStudentCourse).contains(course3);
    assertThat(studentCourseDetails2).extracting(StudentCourseDetail::getCourseApplicationStatus).contains(status3);
  }
}
