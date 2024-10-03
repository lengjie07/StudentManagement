package raisetech.studentmanagement.controller.converter;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
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
    StudentCourse course2 = new StudentCourse(3, 1, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse course3 = new StudentCourse(3, 2, "コース名", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));

    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = Arrays.asList(course1, course2, course3);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.size()).isEqualTo(2);

    StudentDetail studentDetail1 = actual.getFirst();
    assertThat(studentDetail1.getStudent()).isEqualTo(student1);
    assertThat(studentDetail1.getStudentCourses().size()).isEqualTo(2);
    assertThat(studentDetail1.getStudentCourses().get(0)).isEqualTo(course1);
    assertThat(studentDetail1.getStudentCourses().get(1)).isEqualTo(course2);

    StudentDetail studentDetail2 = actual.get(1);
    assertThat(studentDetail2.getStudent()).isEqualTo(student2);
    assertThat(studentDetail2.getStudentCourses().size()).isEqualTo(1);
    assertThat(studentDetail2.getStudentCourses().getFirst()).isEqualTo(course3);
  }
}
