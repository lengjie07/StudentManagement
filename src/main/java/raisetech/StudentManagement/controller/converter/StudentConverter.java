package raisetech.studentmanagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;

/**
 * 受講生とコース情報を組み合わせて受講生詳細に変換するConverter
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づくコース情報をマッピングする
   * コース情報は一人の受講生に対して複数存在する
   *
   * @param students 受講生リスト
   * @param studentCourses　コース情報リスト
   * @return 受講生詳細リスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourse> studentCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      List<StudentCourse> convertStudentCourses = studentCourses.stream()
          .filter(studentCourse -> student.getId() == studentCourse.getStudentId())
          .collect(Collectors.toList());
      studentDetail.setStudentCourses(convertStudentCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}
