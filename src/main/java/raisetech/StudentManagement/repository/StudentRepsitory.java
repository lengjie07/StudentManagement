package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
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

  @Insert("INSERT INTO students (full_name, furigana, nickname, email_address, area, age, gender, remark, is_deleted) " +
      "VALUES (#{fullName}, #{furigana}, #{nickname}, #{emailAddress}, #{area}, #{age}, #{gender}, #{remark}, #{isDeleted})")
  void insertStudent(Student student);
}
