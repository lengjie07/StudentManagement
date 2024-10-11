package raisetech.studentmanagement.domain;

import java.time.LocalDateTime;
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
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String status;
}
