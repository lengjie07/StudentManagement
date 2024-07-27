package raisetech.studentmanagement;

import java.sql.Timestamp;
import lombok.Getter;

@Getter
public class StudentCourse {

  private String id;
  private String studentId;
  private String courseName;
  private Timestamp startDate;
  private Timestamp endDate;
}
