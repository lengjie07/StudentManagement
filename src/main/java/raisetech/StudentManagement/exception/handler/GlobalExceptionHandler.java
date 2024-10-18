package raisetech.studentmanagement.exception.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import raisetech.studentmanagement.exception.ErrorResponse;
import raisetech.studentmanagement.exception.StudentDetailNotFoundException;
import raisetech.studentmanagement.exception.StudentNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * ControllerのgetStudentメソッドで存在しないIDがリクエストされた際の例外をハンドリングする
   *
   * @param ex StudentNotFoundException
   * @return "error": "受講生ID: idが見つかりません。"
   */
  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleStudentNotFoundException(StudentNotFoundException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * ControllerのsearchStudentDetailメソッドで検索条件に一致する受講生が見つからなかった際の例外をハンドリングする
   *
   * @param ex StudentDetailNotFoundException
   * @return "error": "該当する受講生が見つかりませんでした。"
   */
  @ExceptionHandler(StudentDetailNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleStudentDetailNotFoundException(StudentDetailNotFoundException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * バリデーションの結果、BAD_REQUESTが発生した際の例外をハンドリングする
   *
   * @param ex handleMethodArgumentNotValidException
   * @return カラム名とデフォルトのエラーメッセージ
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage()));

    ErrorResponse errorResponse = new ErrorResponse("Validation failed", errors);
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
