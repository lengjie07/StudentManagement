package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

@Mapper
public interface StudentRepsitory {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourse();
}
