package raisetech.studentmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.StudentSearchCriteria;
import raisetech.studentmanagement.exception.StudentDetailNotFoundException;
import raisetech.studentmanagement.exception.StudentNotFoundException;
import raisetech.studentmanagement.service.StudentService;

/**
 * 受講生の検索、登録、更新などを行うREST APIとして実行されるController
 */
@Tag(name = "Student Management API", description = "受講生の検索、登録、更新などを行うAPI")
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の全件検索
   *
   * @return 受講生詳細リスト
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功"),
      @ApiResponse(responseCode = "500", description = "内部サーバーエラー")
  })
  @GetMapping("/students")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * IDで指定した受講生詳細の検索
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @Operation(summary = "受講生詳細検索", description = "指定したIDの受講生詳細を検索します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功"),
      @ApiResponse(responseCode = "404", description = "受講生が見つかりません")
  })
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(@PathVariable @NotNull int id) {
    StudentDetail studentDetail = service.searchStudent(id);
    if (studentDetail == null || studentDetail.getStudent() == null) {
      throw new StudentNotFoundException(id);
    }
    return studentDetail;
  }

  /**
   * 条件を指定した受講生詳細の検索
   * 検索条件はJson形式で渡す
   *
   * @param criteria 検索条件
   * @return 受講生詳細リスト
   */
  @Operation(summary = "受講生詳細絞り込み検索", description = "指定した条件で受講生詳細を検索します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "成功"),
      @ApiResponse(responseCode = "404", description = "受講生が見つかりません")
  })
  @GetMapping("/search")
  public ResponseEntity<List<StudentDetail>> searchStudentDetail(StudentSearchCriteria criteria) {
    List<StudentDetail> studentDetailList = service.searchStudentDetail(criteria);
    if (studentDetailList.isEmpty())
      throw new StudentDetailNotFoundException("該当する受講生が見つかりませんでした。");
    return ResponseEntity.ok(studentDetailList);
  }

  /**
   * 新規受講生の登録
   * コース情報と申し込み状況も一緒に登録する
   *
   * @param studentDetail 受講生詳細
   * @return 登録した受講生詳細
   */
  @Operation(summary = "受講生登録", description = "受講生とコースを登録します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "登録成功"),
      @ApiResponse(responseCode = "400", description = "不正なリクエスト")
  })
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.registerStudentWithCourse(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生詳細の更新
   * キャンセルフラグを更新して論理削除を行う
   * 受講生情報とコース情報、申し込み状況を一緒に更新する
   *
   * @param studentDetail 受講生詳細
   * @return 更新後の受講生詳細
   */
  @Operation(summary = "受講生更新", description = "受講生情報とコース情報を更新します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功"),
      @ApiResponse(responseCode = "400", description = "不正なリクエスト"),
      @ApiResponse(responseCode = "404", description = "受講生が見つかりません")
  })
  @PutMapping("/students")
  public ResponseEntity<StudentDetail> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    if (studentDetail == null || studentDetail.getStudent() == null) {
      return ResponseEntity.badRequest().build();
    }

    StudentDetail studentDetail1 = service.searchStudent(studentDetail.getStudent().getId());
    if (studentDetail1 == null || studentDetail1.getStudent() == null) {
      throw new StudentNotFoundException(studentDetail.getStudent().getId());
    }

    service.updateStudentWithCourses(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }
}
