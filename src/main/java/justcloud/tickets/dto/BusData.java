package justcloud.tickets.dto;

public class BusData {

  private String id;
  private String brand;
  private String busNumber;
  private String plates;

  private DriverData driver1;
  private DriverData driver2;

  private Boolean wifi;
  private Boolean cafeteria;
  private Boolean bathroom;
  private Boolean headphones;

  private ServiceTypeData serviceType;

  private String status;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the brand */
  public String getBrand() {
    return brand;
  }

  /** @param brand the brand to set */
  public void setBrand(String brand) {
    this.brand = brand;
  }

  /** @return the busNumber */
  public String getBusNumber() {
    return busNumber;
  }

  /** @param busNumber the busNumber to set */
  public void setBusNumber(String busNumber) {
    this.busNumber = busNumber;
  }

  /** @return the plates */
  public String getPlates() {
    return plates;
  }

  /** @param plates the plates to set */
  public void setPlates(String plates) {
    this.plates = plates;
  }

  /** @return the driver1 */
  public DriverData getDriver1() {
    return driver1;
  }

  /** @param driver1 the driver1 to set */
  public void setDriver1(DriverData driver1) {
    this.driver1 = driver1;
  }

  /** @return the driver2 */
  public DriverData getDriver2() {
    return driver2;
  }

  /** @param driver2 the driver2 to set */
  public void setDriver2(DriverData driver2) {
    this.driver2 = driver2;
  }

  /** @return the serviceType */
  public ServiceTypeData getServiceType() {
    return serviceType;
  }

  /** @param serviceType the serviceType to set */
  public void setServiceType(ServiceTypeData serviceType) {
    this.serviceType = serviceType;
  }

  /** @return the status */
  public String getStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getWifi() {
    return wifi;
  }

  public void setWifi(Boolean wifi) {
    this.wifi = wifi;
  }

  public Boolean getCafeteria() {
    return cafeteria;
  }

  public void setCafeteria(Boolean cafeteria) {
    this.cafeteria = cafeteria;
  }

  public Boolean getBathroom() {
    return bathroom;
  }

  public void setBathroom(Boolean bathroom) {
    this.bathroom = bathroom;
  }

  public Boolean getHeadphones() {
    return headphones;
  }

  public void setHeadphones(Boolean headphones) {
    this.headphones = headphones;
  }
}
