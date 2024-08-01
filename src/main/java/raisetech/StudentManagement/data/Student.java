package raisetech.studentmanagement.data;

import lombok.Getter;

@Getter
public class Student {

  private String id;
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
