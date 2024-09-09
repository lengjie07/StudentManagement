package raisetech.studentmanagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repsitory) {
    this.repository = repsitory;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudent();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourse();
  }

  @Transactional
  public void registerStudentWithCourse(StudentDetail studentDetail) {
    // 受講生を登録
    repository.insertStudent(studentDetail.getStudent());

    // コース情報を登録
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId()); // 登録した受講生のIDをセット
      studentCourse.setStartDate(LocalDateTime.now()); // 開始日を設定
      studentCourse.setEndDate(LocalDateTime.now().plusYears(1)); // 終了日を開始日の1年後に設定
      repository.insertStudentCourse(studentCourse);
    }
  }

  public Student findStudentById(int id){
    return repository.findStudentById(id);
  }

  public List<StudentCourse> findStudentCoursesByStudentId(int studentId){
    return repository.findStudentCoursesByStudentId(studentId);
  }
}

