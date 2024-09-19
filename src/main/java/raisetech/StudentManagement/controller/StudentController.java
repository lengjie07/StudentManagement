package raisetech.studentmanagement.controller;

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
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

/**
 *  受講生の検索、登録、更新などを行うREST APIとして実行されるController
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
  }

  /**
   * 受講生詳細の全件検索
   * @return 受講生詳細リスト
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * IDで指定した受講生詳細の検索
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable @NotNull int id) {
    return service.searchStudent(id);
  }

  /**
   * 新規受講生の登録
   * コース情報も一緒に登録する
   * @param studentDetail 受講生詳細
   * @return 登録した受講生詳細
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail){
    service.registerStudentWithCourse(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生詳細の更新
   * キャンセルフラグの更新も行う(論理削除)
   * 受講生情報とコース情報を遺書に更新する
   * @param studentDetail 受講生詳細
   * @return 更新が成功した旨をテキストで返す
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail){
    service.updateStudentWithCourses(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
