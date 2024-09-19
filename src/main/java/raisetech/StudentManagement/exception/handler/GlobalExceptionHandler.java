package raisetech.studentmanagement.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import raisetech.studentmanagement.exception.StudentNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * ControllerのgetStudentメソッドで存在しないIDがリクエストされた際の例外をハンドリングする
   * @param ex StudentNotFoundException
   * @return 受講生ID: "+ id + "が見つかりません。
   */
  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex){
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
