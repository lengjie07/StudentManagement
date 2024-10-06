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
 */
@Component
public class StudentConverter {

  /**
   * コース情報に紐づく申し込み状況をマッピングしてコース詳細を作成する
   * 受講生に紐づくコース詳細をマッピングして受講生詳細リストを作成する
   * 1つのコース情報に対して1つの申し込み状況が存在する
   * 1人の受講生に対して複数のコース詳細が存在する可能性がある
   *
   * @param students 受講生情報
   * @param studentCourses コース情報
   * @param courseApplicationStatuses 申し込み状況
   * @return 受講生詳細リスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourse> studentCourses, List<CourseApplicationStatus> courseApplicationStatuses) {

    // CourseApplicationStatusをCourseIdでマッピング
    Map<Integer, CourseApplicationStatus> statusMap = mapCourseApplicationStatuses(
        courseApplicationStatuses);

    // StudentIdごとにコース情報と申し込み状況を組み合わせる
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      createStudentDetail(studentCourses, student, statusMap, studentDetails);
    }
    return studentDetails;
  }

  private static Map<Integer, CourseApplicationStatus> mapCourseApplicationStatuses(
      List<CourseApplicationStatus> courseApplicationStatuses) {
    Map<Integer, CourseApplicationStatus> statusMap = new HashMap<>();
    for (CourseApplicationStatus status : courseApplicationStatuses) {
      statusMap.put(status.getCourseId(), status);
    }
    return statusMap;
  }

  private static void createStudentDetail(List<StudentCourse> studentCourses, Student student,
      Map<Integer, CourseApplicationStatus> statusMap, List<StudentDetail> studentDetails) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    // 受講生に紐づくコース情報をリスト化
    List<StudentCourseDetail> studentCourseDetails = new ArrayList<>();
    for (StudentCourse studentCourse : studentCourses) {
      if (student.getId() == studentCourse.getStudentId()) {
        CourseApplicationStatus courseApplicationStatus = statusMap.get(studentCourse.getId());

        StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
        studentCourseDetail.setStudentCourse(studentCourse);
        studentCourseDetail.setCourseApplicationStatus(courseApplicationStatus);

        studentCourseDetails.add(studentCourseDetail);
      }
    }
    studentDetail.setStudentCourseDetails(studentCourseDetails);
    studentDetails.add(studentDetail);
  }
}
