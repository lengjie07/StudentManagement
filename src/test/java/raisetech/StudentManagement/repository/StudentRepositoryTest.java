package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生情報の全件検索が行えること() {
    List<Student> actual = sut.searchStudent();

    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 新規受講生情報の登録が行えること() {
    Student student = new Student();
    student.setFullName("桐ヶ谷直葉");
    student.setFurigana("キリガヤスグハ");
    student.setNickname("リーファ");
    student.setEmailAddress("leafa@sao.com");
    student.setArea("埼玉");
    student.setAge(16);
    student.setGender("女性");

    sut.insertStudent(student);

    List<Student> actual = sut.searchStudent();

    assertThat(actual.size()).isEqualTo(6);
  }
}
