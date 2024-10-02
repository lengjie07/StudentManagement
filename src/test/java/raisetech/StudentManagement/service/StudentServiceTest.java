package raisetech.studentmanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut; // sut：テスト対象

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の全件検索でリポジトリとコンバータの処理が適切に呼び出せていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchStudent()).thenReturn(studentList);
    when(repository.searchStudentCourse()).thenReturn(studentCourseList);


    // 実行
    sut.searchStudentList();

    // 検証
    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentCourse();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    // 後処理
    // 例) DBを元に戻す
  }

  @Test
  void IDで指定した受講生詳細の検索でリポジトリの処理が適切に呼び出せていること() {
    int id = 12345;
    Student student = new Student();
    student.setId(id);
    List<StudentCourse> studentCourseList = new ArrayList<>();

    when(repository.findStudentById(id)).thenReturn(student);
    when(repository.findStudentCoursesByStudentId(id)).thenReturn(studentCourseList);

    StudentDetail expected = new StudentDetail(student, studentCourseList); // expected：期待値

    StudentDetail actual = sut.searchStudent(id);// actual：実行結果

    verify(repository, times(1)).findStudentById(id);
    verify(repository, times(1)).findStudentCoursesByStudentId(id);
    Assertions.assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
  }

  @Test
  void 新規受講生の登録でリポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudentWithCourse(studentDetail);

    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(1)).insertStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細の更新でリポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudentWithCourses(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }
}
