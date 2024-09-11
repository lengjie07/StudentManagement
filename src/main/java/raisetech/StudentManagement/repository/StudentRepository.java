package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourse();

  @Insert("""
          INSERT INTO students (full_name, furigana, nickname, email_address, area, age, gender, remark, is_deleted)
          VALUES (#{fullName}, #{furigana}, #{nickname}, #{emailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)
          """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
    // 自動生成されたIDを取得
  void insertStudent(Student student);

  @Insert("""
          INSERT INTO students_courses (student_id, course_name, start_date, end_date)
          VALUES (#{studentId}, #{courseName}, #{startDate}, #{endDate})
          """)
  void insertStudentCourse(StudentCourse studentCourse);

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findStudentById(int id);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> findStudentCoursesByStudentId(int studentId);

  @Update("""
      UPDATE students
      SET
        full_name = #{fullName},
        furigana = #{furigana},
        nickname = #{nickname},
        email_address = #{emailAddress},
        area = #{area},
        age = #{age},
        gender = #{gender},
        remark = #{remark},
        is_deleted = #{isDeleted}
      WHERE id = #{id}
      """)
  void updateStudent(Student student);

  // コース名、開始日、終了日がnullの場合は更新しないようにする
  @Update("""
      <script>
        UPDATE students_courses
        <set>
          <if test='courseName != null'>
            course_name = #{courseName},
          </if>
          <if test='startDate != null'>
            start_date = #{startDate},
          </if>
          <if test='endDate != null'>
            end_date = #{endDate},
          </if>
        </set>
        WHERE id = #{id} AND student_id = #{studentId}
      </script>
      """)
  void updateStudentCourse(StudentCourse studentCourse);
}
