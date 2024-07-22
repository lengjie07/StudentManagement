package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  private String name = "Ryosuke Miyake";
  private String age = "25";

  // Mapの追加
  private Map<String, String> student = new HashMap<>();

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/studentInfo")
  public String getStudentInfo() {
    return name + " " + age + "歳";
  }

  @PostMapping("/studentInfo")
  public void setStudentInfo(@RequestParam String name, @RequestParam String age){
    this.name = name;
    this.age = age;
  }

  @PostMapping("/studentName")
  public void updateStudentName(@RequestParam String name){
    this.name = name;
  }

  // Mapの中身を返す
  @GetMapping("/studentMap")
  public Map<String, String> getStudents() {
    return student;
  }

  // Mapの中身を更新
  @PostMapping("/studentMap")
  public void updateStudent(@RequestParam String studentNumber, @RequestParam String studentName) {
    student.put(studentNumber, studentName);
  }
}
