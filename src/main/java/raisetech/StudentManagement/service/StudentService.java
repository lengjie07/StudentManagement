package raisetech.studentmanagement.service;

import java.util.List;
import java.util.stream.Collectors;
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
    // 年齢が30代の人のみを抽出する
    // 抽出したリストをコントローラーに返す
    List<Student> students = repsitory.searchStudent();
    return students.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .collect(Collectors.toList());
  }

  public List<StudentCourse> searchStudentCourseList() {
    // 「Javaコース」のコース情報のみを抽出する
    // 抽出したリストをコントローラーに返す
    List<StudentCourse> studentCourses = repsitory.searchStudentCourse();
    return studentCourses.stream()
        .filter(studentCourse -> "Java".equalsIgnoreCase(studentCourse.getCourseName()))
        .collect(Collectors.toList());
  }
}
