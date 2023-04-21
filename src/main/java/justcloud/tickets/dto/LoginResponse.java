package justcloud.tickets.dto;

public class LoginResponse {
  private boolean valid;

  public LoginResponse(boolean valid) {
    this.valid = valid;
  }

  public boolean isValid() {
    return valid;
  }
}
