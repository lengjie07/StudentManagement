package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

/**
 * 受講生テーブルとコース情報テーブルと紐づくリポジトリ
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生情報の全件検索
   * @return 受講生情報リスト
   */
  List<Student> searchStudent();

  /**
   * コース情報の全件検索
   * @return コース情報リスト
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourse();

  /**
   * IDで指定した受講生情報の検索
   * @param id　受講生ID
   * @return IDで指定した受講生情報
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findStudentById(int id);

  /**
   * 受講生IDで指定した受講生のコース情報
   * @param studentId 受講生ID
   * @return 受講生IDで指定した受講生のコース情報リスト
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> findStudentCoursesByStudentId(int studentId);

  /**
   * 新規受講生情報の登録
   * IDは自動採番
   * 受講生情報を登録する際にコース情報も一緒に行う
   * @param student 受講生情報
   */
  @Insert("""
          INSERT INTO students (full_name, furigana, nickname, email_address, area, age, gender, remark, is_deleted)
          VALUES (#{fullName}, #{furigana}, #{nickname}, #{emailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)
          """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
    // 自動生成されたIDを取得
  void insertStudent(Student student);

  /**
   * 新規コース情報の登録
   * IDは自動採番
   * コース情報の登録は受講生情報の登録と一緒に行う
   * @param studentCourse コース情報
   */
  @Insert("""
          INSERT INTO students_courses (student_id, course_name, start_date, end_date)
          VALUES (#{studentId}, #{courseName}, #{startDate}, #{endDate})
          """)
  void insertStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生情報の更新
   * @param student 受講生情報
   */
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

  /**
   * コース情報の更新
   * コース名、開始日、終了日がnullの場合は更新しないようにする
   * @param studentCourse コース情報
   */
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
