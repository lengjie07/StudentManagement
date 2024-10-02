package raisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private final int id = 12345;

  @Test
  void 受講生詳細の全件検索を実行して空のリストが返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 存在するIDで指定した受講生詳細の検索で正常に返されること() throws Exception {
    Student student = new Student(
        id,
        "氏名",
        "フリガナ",
        "ニックネーム",
        "test@example.com",
        "地域",
        99,
        "性別",
        "備考",
        false);
    StudentDetail studentDetail =new StudentDetail(student, null);

    Mockito.when(service.searchStudent(id)).thenReturn(studentDetail);

    mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 存在しないIDで指定した受講生詳細の検索で404が返されること() throws Exception {
    Mockito.when(service.searchStudent(id)).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", id))
        .andExpect(status().isNotFound());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細ですべての値が正しく入力されているときにエラーが発生しないこと() {
    Student student = new Student(
        id,
        "氏名",
        "フリガナ",
        "ニックネーム",
        "test@example.com",
        "地域",
        99,
        "性別",
        "備考",
        false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細ですべての値がnullのときにNotBlankの数だけエラーが発生すること() {
    Student student = new Student(
        id,
        null, // 氏名　NotBlank
        null, // フリガナ　NotBlank
        null, // ニックネーム
        null, // メールアドレス　NotBlank
        null, // 地域
        null, // 年齢　NotBlank
        null, // 性別
        null, // 備考
        false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(4);
  }

  @Test
  void 受講生詳細でメールアドレスの形式が正しくないときにエラーが発生すること() {
    Student student = new Student(
        id,
        "氏名",
        "フリガナ",
        "ニックネーム",
        "メールアドレス", // 不正なメールアドレス
        "地域",
        99,
        "性別",
        "備考",
        false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("電子メールアドレスとして正しい形式にしてください");
  }
}
