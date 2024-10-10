package raisetech.studentmanagement.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSearchCriteria {
  private String fullName;
  private String furigana;
  private String nickname;
  private String emailAddress;
  private String area;
  private Integer age;
  private String gender;
  private String remark;
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
}
