package raisetech.studentmanagement.exception;

/**
 * ControllerのgetStudentメソッドで存在しないIDがリクエストされた際の例外クラス
 */
public class StudentNotFoundException extends RuntimeException{

  public StudentNotFoundException(int id) {
    super("受講生ID: "+ id + "が見つかりません。");
  }
}
