package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;

/**
 * 受講生テーブルとコース情報テーブルと紐づくリポジトリ
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生情報の全件検索
   *
   * @return 受講生情報リスト
   */
  List<Student> searchStudent();

  /**
   * コース情報の全件検索
   *
   * @return コース情報リスト
   */
  List<StudentCourse> searchStudentCourse();

  /**
   * コース申し込み状況の全件検索
   *
   * @return 申し込み状況リスト
   */
  List<CourseApplicationStatus> searchCourseApplicationStatus();

  /**
   * IDで指定した受講生情報の検索
   *
   * @param id　受講生ID
   * @return IDで指定した受講生情報
   */
  Student findStudentById(int id);

  /**
   * 受講生IDで指定した受講生のコース情報
   *
   * @param studentId 受講生ID
   * @return 受講生IDで指定した受講生のコース情報リスト
   */
  List<StudentCourse> findStudentCoursesByStudentId(int studentId);

  /**
   * コースIDで指定したコースの申し込み状況
   *
   * @param courseId コースID
   * @return コースIDで指定したコースの申し込み状況
   */
  CourseApplicationStatus findCourseApplicationStatusByCourseId(int courseId);

  /**
   * 新規受講生情報の登録
   * IDは自動採番
   * 受講生情報を登録する際にコース情報も一緒に行う
   *
   * @param student 受講生情報
   */
  void insertStudent(Student student);

  /**
   * 新規コース情報の登録
   * IDは自動採番
   * コース情報の登録は受講生情報の登録と一緒に行う
   *
   * @param studentCourse コース情報
   */
  void insertStudentCourse(StudentCourse studentCourse);

  /**
   * 新規申し込み状況の登録
   * IDは自動採番
   * 初期値はデフォルトで'仮申込'
   *
   * @param courseApplicationStatus 申し込み状況
   */
  void insertCourseApplicationStatus(CourseApplicationStatus courseApplicationStatus);

  /**
   * 受講生情報の更新
   *
   * @param student 受講生情報
   */
  void updateStudent(Student student);

  /**
   * コース情報の更新
   * コース名、開始日、終了日がnullの場合は更新しないようにする
   *
   * @param studentCourse コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 申し込み状況の更新
   *
   * @param courseApplicationStatus 申し込み状況
   */
  void updateCourseApplicationStatus(CourseApplicationStatus courseApplicationStatus);

  List<StudentDetail> searchStudentDetail();
}
