package justcloud.tickets.dto;

public class StoreRequestData {

  private String place;
  private String name;
  private String lastNames;
  private String email;
  private String mobilePhone;
  private String telco;

  /** @return the place */
  public String getPlace() {
    return place;
  }

  /** @param place the place to set */
  public void setPlace(String place) {
    this.place = place;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the lastNames */
  public String getLastNames() {
    return lastNames;
  }

  /** @param lastNames the lastNames to set */
  public void setLastNames(String lastNames) {
    this.lastNames = lastNames;
  }

  /** @return the email */
  public String getEmail() {
    return email;
  }

  /** @param email the email to set */
  public void setEmail(String email) {
    this.email = email;
  }

  /** @return the mobilePhone */
  public String getMobilePhone() {
    return mobilePhone;
  }

  /** @param mobilePhone the mobilePhone to set */
  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  /** @return the telco */
  public String getTelco() {
    return telco;
  }

  /** @param telco the telco to set */
  public void setTelco(String telco) {
    this.telco = telco;
  }
}
