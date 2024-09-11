package raisetech.studentmanagement.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public String getStudentCourseList(Model model) {
    model.addAttribute("studentCourseList", service.searchStudentCourseList());
    return "studentCourseList";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model){
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourses(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result){
    if (result.hasErrors()){
      return "registerStudent";
    }
    // 新規受講生情報とコース情報を登録
    service.registerStudentWithCourse(studentDetail);
    return "redirect:/studentList";
  }

  @GetMapping("/editStudent/{id}")
  public String editStudent(@PathVariable int id, Model model){
    Student student = service.findStudentById(id);
    List<StudentCourse> studentCourses = service.findStudentCoursesByStudentId(id);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);

    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result){
    if (result.hasErrors()){
      return "updateStudent";
    }
    service.updateStudentWithCourses(studentDetail);
    return "redirect:/studentList";
  }
}
