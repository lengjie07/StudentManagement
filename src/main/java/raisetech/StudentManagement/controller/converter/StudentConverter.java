package raisetech.studentmanagement.controller.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.domain.StudentDetail;

/**
 * 受講生とコース情報を組み合わせて受講生詳細に変換するConverter
 * 受講生情報、コース情報、申し込み状況を結び付けて、受講生詳細を生成する
 */
@Component
public class StudentConverter {

  /**
   *　重複して取得された受講生情報を一つの受講生詳細にまとめる
   *
   * @param studentDetailList 受講生詳細リスト(生データ)
   * @return 重複をまとめた受講生詳細リスト
   */
  public List<StudentDetail> convertSearchedStudentDetailList(
      List<StudentDetail> studentDetailList) {
    Map<Integer, StudentDetail> studentDetailMap = new HashMap<>();

    studentDetailList.forEach(studentDetail -> {
      int studentId = studentDetail.getStudent().getId();
      if (studentDetailMap.containsKey(studentId)) {
        studentDetailMap.get(studentId).getStudentCourseDetailList()
            .addAll(studentDetail.getStudentCourseDetailList());
      } else {
        studentDetailMap.put(studentId, studentDetail);
      }
    });
    return new ArrayList<>(studentDetailMap.values());
  }
}
