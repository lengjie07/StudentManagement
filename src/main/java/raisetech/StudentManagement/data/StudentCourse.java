package raisetech.studentmanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "コース情報")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {

  private int id;
  private int studentId;

  @NotBlank
  private String courseName;

  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
