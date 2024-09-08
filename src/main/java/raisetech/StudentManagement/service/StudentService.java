package raisetech.studentmanagement.service;

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
  public void registerStudentWithCourse(StudentDetail studentDetail){
    // 受講生を登録
    Student student = studentDetail.getStudent();
    repository.insertStudent(student);

    //　登録された受講生のIDを取得
    int studentId = student.getId();

    // コース情報を登録
    List<StudentCourse> studentCourses = studentDetail.getStudentCourses();
    if (studentCourses != null){
      for (StudentCourse studentCourse : studentCourses){
        studentCourse.setStudentId(studentId);
        repository.insertStudentCourse(studentCourse);
      }
    }
  }
}
