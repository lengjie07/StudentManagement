package raisetech.studentmanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生情報")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  private int id;

  @NotBlank
  private String fullName;

  @NotBlank
  private String furigana;

  private String nickname;

  @NotBlank
  @Email
  private String emailAddress;

  private String area;

  @NotNull
  private Integer age;

  private String gender;
  private String remark;
  private boolean isDeleted;
}
