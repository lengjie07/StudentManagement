package raisetech.studentmanagement.controller.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentCourseDetail;
import raisetech.studentmanagement.domain.StudentDetail;

/**
 * 受講生とコース情報を組み合わせて受講生詳細に変換するConverter
 * 受講生情報、コース情報、申し込み状況を結び付けて、受講生詳細を生成する
 */
@Component
public class StudentConverter {

  /**
   * 受講生情報リスト、コース情報リスト、申し込み状況リストを元に、
   * 受講生詳細情報のリストを生成する
   * 1つのコース情報に対して1つの申し込み状況が存在する
   * 1人の受講生に対して複数のコース詳細が存在する可能性がある
   *
   * @param studentList 受講生情報リスト
   * @param studentCourseList コース情報リスト
   * @param courseApplicationStatusList 申し込み状況リスト
   * @return 受講生詳細リスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList,
      List<CourseApplicationStatus> courseApplicationStatusList) {

    // 申し込み状況リストをコースIDをキーとするマップに変換
    Map<Integer, CourseApplicationStatus> statusMap = mapCourseApplicationStatuses(
        courseApplicationStatusList);

    // 受講生ごとにコース情報と申し込み状況を組み合わせて、受講生詳細を作成
    List<StudentDetail> studentDetailList = new ArrayList<>();
    for (Student student : studentList) {
      createStudentDetail(student, studentCourseList, statusMap, studentDetailList);
    }
    return studentDetailList;
  }

  /**
   * 申し込み状況リストをコースIDをキーとするマップに変換
   * コースIDによる申し込み状況の検索を高速化するため
   *
   * @param courseApplicationStatusList 申し込み状況リスト
   * @return コースIDをキー、申し込み状況を値とするマップ
   */
  private static Map<Integer, CourseApplicationStatus> mapCourseApplicationStatuses(
      List<CourseApplicationStatus> courseApplicationStatusList) {
    Map<Integer, CourseApplicationStatus> statusMap = new HashMap<>();
    for (CourseApplicationStatus status : courseApplicationStatusList) {
      statusMap.put(status.getCourseId(), status);
    }
    return statusMap;
  }

  /**
   * 受講生詳細を作成し、コース情報と申し込み状況を追加する
   * 受講生に紐づいたコース情報と申し込み状況を設定して、受講生詳細リストに追加する
   *
   * @param student 受講生情報
   * @param studentCourseList コース情報リスト
   * @param statusMap コースIDをキー、申し込み状況を値とするマップ
   * @param studentDetailList 受講生詳細リスト
   */
  private static void createStudentDetail(Student student, List<StudentCourse> studentCourseList,
      Map<Integer, CourseApplicationStatus> statusMap, List<StudentDetail> studentDetailList) {
    // 受講生詳細オブジェクトを作成し、受講生情報を設定
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    // 受講生に紐づくコース詳細情報をリスト化
    List<StudentCourseDetail> studentCourseDetailList = new ArrayList<>();
    for (StudentCourse studentCourse : studentCourseList) {
      // 受講生に紐づくコース情報をフィルタリング
      if (student.getId() == studentCourse.getStudentId()) {
        // コースIDから申し込み状況を取得
        CourseApplicationStatus courseApplicationStatus = statusMap.get(studentCourse.getId());

        // コース詳細オブジェクトを作成し、コース情報と申し込み状況を設定
        StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
        studentCourseDetail.setStudentCourse(studentCourse);
        studentCourseDetail.setCourseApplicationStatus(courseApplicationStatus);

        // コース詳細リストに追加
        studentCourseDetailList.add(studentCourseDetail);
      }
    }
    // 受講生詳細にコース詳細を設定
    studentDetail.setStudentCourseDetailList(studentCourseDetailList);
    // 受講生詳細リストに追加
    studentDetailList.add(studentDetail);
  }
}
