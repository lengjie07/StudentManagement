package raisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.domain.StudentCourseDetail;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private final int id = 12345;

  /**
   * 受講生詳細データの準備
   * 受講生情報は入力チェックにかからない
   * コース詳細情報は空
   */
  private StudentDetail createValidStudentDetail() {
    Student student = new Student(id, "三宅崚介", "ミヤケリョウスケ", "Leng", "miyake@example.com",
        "神奈川", 26, "男性", "備考", false);
    List<StudentCourseDetail> studentCourseDetailList = new ArrayList<>();
    return new StudentDetail(student, studentCourseDetailList);
  }

  @Test
  void 受講生詳細の全件検索を実行して空のリストが返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 存在するIDで指定した受講生詳細の検索で正常に返されること() throws Exception {
    StudentDetail studentDetail = createValidStudentDetail();

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
  void 条件に合致する受講生詳細が正常に返されること() throws Exception {
    List<StudentDetail> studentDetailList = new ArrayList<>();
    studentDetailList.add(createValidStudentDetail());

    when(service.searchStudentDetail(any())).thenReturn(studentDetailList);

    mockMvc.perform(MockMvcRequestBuilders.get("/search")
            .param("fullName", "三宅崚介")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentDetail(any());
  }

  @Test
  void 検索条件に合致する受講生が見つからない場合に404エラーが返されること() throws Exception {
    when(service.searchStudentDetail(any())).thenReturn(new ArrayList<>());

    mockMvc.perform(MockMvcRequestBuilders.get("/search")
            .param("fullName", "名無し")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(service, times(1)).searchStudentDetail(any());
  }

  @Test
  void 新規受講生の登録で正常に登録されること() throws Exception {
    StudentDetail studentDetail = createValidStudentDetail();

    mockMvc.perform(MockMvcRequestBuilders.post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudentWithCourse(any());
  }

  @Test
  void 新規受講生の登録でバリデーションエラーが発生した場合400エラーが返されること()
      throws Exception {
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
    List<StudentCourseDetail> studentCourseDetailList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseDetailList);

    mockMvc.perform(MockMvcRequestBuilders.post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).registerStudentWithCourse(any());
  }

  @Test
  void 受講生詳細の更新で正常に更新されること() throws Exception {
    StudentDetail studentDetail = createValidStudentDetail();

    when(service.searchStudent(id)).thenReturn(studentDetail);

    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudentWithCourses(any());
  }

  @Test
  void 受講生詳細の更新で存在しないIDの場合404エラーが返されること() throws Exception {
    StudentDetail studentDetail = createValidStudentDetail();

    when(service.searchStudent(id)).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isNotFound());

    verify(service, times(1)).searchStudent(id);
    verify(service, times(0)).updateStudentWithCourses(any());
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
  }
}
