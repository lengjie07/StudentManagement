package raisetech.studentmanagement.data;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private int id;
  private int studentId;
  private String courseName;
  private Timestamp startDate;
  private Timestamp endDate;
}
