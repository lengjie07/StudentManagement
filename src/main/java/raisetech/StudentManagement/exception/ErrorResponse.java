package raisetech.studentmanagement.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {
  private String message;
  private Map<String, String> details;

  public ErrorResponse(String message, Map<String, String> details) {
    this.message = message;
    this.details = details;
  }
}
