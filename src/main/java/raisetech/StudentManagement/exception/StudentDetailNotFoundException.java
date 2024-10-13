package raisetech.studentmanagement.exception;

/**
 * 条件を指定した受講生の検索で該当する受講生が見つからなかった際の例外クラス
 */
public class StudentDetailNotFoundException extends RuntimeException{

  public StudentDetailNotFoundException(String message) {
    super(message);
  }
}
