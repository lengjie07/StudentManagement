package raisetech.studentmanagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentCourseDetail;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

/**
 * 受講生情報を扱うサービス
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repsitory, StudentConverter converter) {
    this.repository = repsitory;
    this.converter = converter;
  }

  /**
   * 受講生詳細の全件検索 取得した受講生情報リストとコース情報リストをConverterで組み合わせて受講生詳細リストを返す
   *
   * @return 受講生詳細リスト
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> students = repository.searchStudent();
    List<StudentCourse> studentCourses = repository.searchStudentCourse();
    List<CourseApplicationStatus> courseApplicationStatuses = repository.searchCourseApplicationStatus();
    return converter.convertStudentDetails(students, studentCourses, courseApplicationStatuses);
  }

  /**
   * IDで指定した受講生詳細の検索
   *
   * @param id 受講生ID
   * @return IDで指定した受講生詳細
   */
  public StudentDetail searchStudent(int id) {
    Student student = repository.findStudentById(id);
    List<StudentCourse> studentCourses = repository.findStudentCoursesByStudentId(id);
    List<CourseApplicationStatus> courseApplicationStatuses = new ArrayList<>();
    for (StudentCourse studentCourse : studentCourses) {
      CourseApplicationStatus status = repository.findCourseApplicationStatusByCourseId(
          studentCourse.getId());
      if (status != null) {
        courseApplicationStatuses.add(status);
      }
    }
    List<StudentCourseDetail> studentCourseDetails = new ArrayList<>();
    for (StudentCourse studentCourse : studentCourses) {
      CourseApplicationStatus courseApplicationStatus = repository.findCourseApplicationStatusByCourseId(
          studentCourse.getId());
      StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
      studentCourseDetail.setStudentCourse(studentCourse);
      studentCourseDetail.setCourseApplicationStatus(courseApplicationStatus);
      studentCourseDetails.add(studentCourseDetail);
    }
    return new StudentDetail(student, studentCourseDetails);
  }

  /**
   * 新規受講生の登録
   * コース情報と申し込み状況も一緒に登録する
   *
   * @param studentDetail 受講生詳細
   * @return 登録した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudentWithCourse(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    // 受講生を登録
    repository.insertStudent(student);

    // コース情報を登録
    studentDetail.getStudentCourseDetails().forEach(studentCourseDetail -> {
      StudentCourse studentCourse = studentCourseDetail.getStudentCourse();
      initStudentCourse(studentCourse, student);
      repository.insertStudentCourse(studentCourse);

      // 申し込み状況を登録
      CourseApplicationStatus courseApplicationStatus = studentCourseDetail.getCourseApplicationStatus();
      if (courseApplicationStatus != null) {
        courseApplicationStatus.setCourseId(studentCourse.getId());
        repository.insertCourseApplicationStatus(courseApplicationStatus);
      }
    });
    return studentDetail;
  }

  /**
   * 受講生情報を登録する際の初期情報
   * 受講生ID、開始日、終了日
   *
   * @param studentCourse コース情報
   * @param student       受講生情報
   */
  private void initStudentCourse(StudentCourse studentCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(student.getId()); // 登録した受講生のIDをセット
    studentCourse.setStartDate(now); // 開始日を設定
    studentCourse.setEndDate(now.plusYears(1)); // 終了日を開始日の1年後に設定
  }

  /**
   * 受講生詳細の更新
   * 受講生情報とコース情報、申し込み状況の更新を一緒に行う
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudentWithCourses(StudentDetail studentDetail) {
    // 受講生情報を更新
    repository.updateStudent(studentDetail.getStudent());

    // コース情報を更新
    studentDetail.getStudentCourseDetails()
        .forEach(studentCourseDetail -> {
          repository.updateStudentCourse(
              studentCourseDetail.getStudentCourse());

          // 申し込み状況を更新
          CourseApplicationStatus courseApplicationStatus = studentCourseDetail.getCourseApplicationStatus();
          if (courseApplicationStatus != null) {
            repository.updateCourseApplicationStatus(courseApplicationStatus);
          }
        });
  }
}
