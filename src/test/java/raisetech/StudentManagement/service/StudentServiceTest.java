package raisetech.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentCourseDetail;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  private final int studentId = 12345;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の全件検索でリポジトリとコンバータの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<CourseApplicationStatus> courseApplicationStatusList = new ArrayList<>();
    when(repository.searchStudent()).thenReturn(studentList);
    when(repository.searchStudentCourse()).thenReturn(studentCourseList);
    when(repository.searchCourseApplicationStatus()).thenReturn(courseApplicationStatusList);

    sut.searchStudentList();

    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentCourse();
    verify(repository, times(1)).searchCourseApplicationStatus();
    verify(converter, times(1)).convertStudentDetailList(studentList, studentCourseList,
        courseApplicationStatusList);
  }

  @Test
  void IDで指定した受講生詳細の検索でリポジトリの処理が適切に呼び出せていること() {
    Student student = createStudent();
    List<StudentCourse> studentCourseList = createStudentCourseList();
    CourseApplicationStatus courseApplicationStatus = createCourseApplicationStatus();

    when(repository.findStudentById(studentId)).thenReturn(student);
    when(repository.findStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);
    when(repository.findCourseApplicationStatusByCourseId(
        studentCourseList.getFirst().getId())).thenReturn(courseApplicationStatus);

    StudentDetail expected = createStudentDetail(student, studentCourseList,
        courseApplicationStatus);

    StudentDetail actual = sut.searchStudent(studentId);

    verify(repository, times(1)).findStudentById(studentId);
    verify(repository, times(1)).findStudentCoursesByStudentId(studentId);
    verify(repository, times(1)).findCourseApplicationStatusByCourseId(
        studentCourseList.getFirst().getId());

    assertThat(actual.getStudent().getId()).isEqualTo(expected.getStudent().getId());
    assertThat(actual.getStudentCourseDetailList().size()).isEqualTo(
        expected.getStudentCourseDetailList().size());
    assertThat(actual.getStudentCourseDetailList().getFirst().getStudentCourse().getId())
        .isEqualTo(
            expected.getStudentCourseDetailList().getFirst().getStudentCourse().getId());
    assertThat(
        actual.getStudentCourseDetailList().getFirst().getCourseApplicationStatus().getId())
        .isEqualTo(
            expected.getStudentCourseDetailList().getFirst().getCourseApplicationStatus().getId());
  }

  @Test
  void 新規受講生の登録でリポジトリの処理が適切に呼び出せていること() {
    Student student = createStudent();
    List<StudentCourse> studentCourseList = createStudentCourseList();
    CourseApplicationStatus courseApplicationStatus = createCourseApplicationStatus();
    StudentDetail studentDetail = createStudentDetail(student, studentCourseList,
        courseApplicationStatus);

    sut.registerStudentWithCourse(studentDetail);

    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(1)).insertStudentCourse(studentCourseList.getFirst());
    verify(repository, times(1)).insertCourseApplicationStatus(courseApplicationStatus);
  }

  @Test
  void 受講生詳細の更新でリポジトリの処理が適切に呼び出せていること() {
    Student student = createStudent();
    List<StudentCourse> studentCourseList = createStudentCourseList();
    CourseApplicationStatus courseApplicationStatus = createCourseApplicationStatus();
    StudentDetail studentDetail = createStudentDetail(student, studentCourseList,
        courseApplicationStatus);

    sut.updateStudentWithCourses(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourseList.getFirst());
    verify(repository, times(1)).updateCourseApplicationStatus(courseApplicationStatus);
  }

  /**
   * @return テスト用の受講生情報
   */
  private Student createStudent() {
    return new Student(studentId, "三宅崚介", "ミヤケリョウスケ", "Leng", "leng@example.com",
        "神奈川", 26, "男性", "備考", false);
  }

  /**
   * @return テスト用のコース情報
   */
  private List<StudentCourse> createStudentCourseList() {
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse = new StudentCourse(1, studentId, "java", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    studentCourseList.add(studentCourse);
    return studentCourseList;
  }

  /**
   * @return テスト用の申し込み状況
   */
  private static CourseApplicationStatus createCourseApplicationStatus() {
    return new CourseApplicationStatus(1, 1, "受講中");
  }

  /**
   * @param student テスト用の受講生情報
   * @param studentCourseList テスト用のコース情報
   * @param courseApplicationStatus テスト用の申し込み状況
   * @return テスト用の受講生詳細
   */
  private static StudentDetail createStudentDetail(Student student,
      List<StudentCourse> studentCourseList,
      CourseApplicationStatus courseApplicationStatus) {
    List<StudentCourseDetail> studentCourseDetailList = new ArrayList<>();
    StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
    studentCourseDetail.setStudentCourse(studentCourseList.getFirst());
    studentCourseDetail.setCourseApplicationStatus(courseApplicationStatus);
    studentCourseDetailList.add(studentCourseDetail);
    return new StudentDetail(student, studentCourseDetailList);
  }
}
