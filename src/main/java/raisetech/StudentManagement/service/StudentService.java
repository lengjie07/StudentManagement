package raisetech.studentmanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.repository.StudentRepsitory;

@Service
public class StudentService {

  private StudentRepsitory repsitory;

  @Autowired
  public StudentService(StudentRepsitory repsitory) {
    this.repsitory = repsitory;
  }

  public List<Student> searchStudentList() {
    return repsitory.searchStudent();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repsitory.searchStudentCourse();
  }
}
