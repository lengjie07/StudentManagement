package raisetech.studentmanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
    List<StudentDetail> actual = sut.searchStudentList(); // actual：実行結果

    // 検証
    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentCourse();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    // 後処理
    // 例) DBを元に戻す
  }
}
