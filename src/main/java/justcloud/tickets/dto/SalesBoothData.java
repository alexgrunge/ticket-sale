package justcloud.tickets.dto;

import java.util.Date;

/** Created by iamedu on 6/26/16. */
public class SalesBoothData extends SalesTerminalData {

  private String latestShiftId;
  private String salesPersonName;
  private Date startDate;

  public String getLatestShiftId() {
    return latestShiftId;
  }

  public void setLatestShiftId(String latestShiftId) {
    this.latestShiftId = latestShiftId;
  }

  public String getSalesPersonName() {
    return salesPersonName;
  }

  public void setSalesPersonName(String salesPersonName) {
    this.salesPersonName = salesPersonName;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
}
