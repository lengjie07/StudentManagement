package raisetech.studentmanagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private int id;
  private String fullName;
  private String furigana;
  private String nickname;
  private String emailAddress;
  private String area;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}
