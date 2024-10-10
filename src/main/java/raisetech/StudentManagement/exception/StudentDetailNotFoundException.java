package raisetech.studentmanagement.exception;

/**
 * ControllerのgetStudentメソッドで存在しないIDがリクエストされた際の例外クラス
 */
public class StudentDetailNotFoundException extends RuntimeException{

  public StudentDetailNotFoundException(String message) {
    super(message);
  }
}
