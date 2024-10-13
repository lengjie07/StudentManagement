package raisetech.studentmanagement.domain;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.CourseApplicationStatus;
import raisetech.studentmanagement.data.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDetail {

  @Valid
  StudentCourse studentCourse;

  @Valid
  CourseApplicationStatus courseApplicationStatus;
}
