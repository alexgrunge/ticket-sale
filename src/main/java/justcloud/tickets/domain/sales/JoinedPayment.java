package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import justcloud.tickets.domain.*;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.domain.tickets.TripAdvance;
import justcloud.tickets.domain.tickets.TripExpense;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
public class JoinedPayment extends BaseModel {

  private boolean payed = false;
  private Date payDate;

  private String paymentNumber;

  @ManyToOne private SalesTerminal paymentTerminal;

  @ManyToOne private PaymentShift paymentShift;

  @ManyToOne private Employee driver1;

  @ManyToOne private Employee driver2;

  @OneToMany(mappedBy = "joinedPayment")
  private List<Trip> trips;

  @Column(name = "driver1_amount")
  private BigDecimal driver1Amount;

  @Column(name = "driver2_amount")
  private BigDecimal driver2Amount;

  @Column(name = "driver1_expenses")
  private BigDecimal driver1Expenses;

  @Column(name = "driver2_expenses")
  private BigDecimal driver2Expenses;

  @Column(name = "driver1_expenses_advance")
  private BigDecimal driver1ExpensesAdvance;

  @Column(name = "driver2_expenses_advance")
  private BigDecimal driver2ExpensesAdvance;

  @Column(name = "driver1_earnings")
  private BigDecimal driver1Earnings;

  @Column(name = "driver2_earnings")
  private BigDecimal driver2Earnings;

  @Column(name = "driver1_loans")
  private BigDecimal driver1Loans;

  @Column(name = "driver2_loans")
  private BigDecimal driver2Loans;

  @Column(name = "driver1_nominal")
  private BigDecimal driver1Nominal;

  @Column(name = "driver2_nominal")
  private BigDecimal driver2Nominal;

  @Column(name = "driver1_insurance")
  private BigDecimal driver1Insurance;

  @Column(name = "driver2_insurance")
  private BigDecimal driver2Insurance;

  @Column(name = "driver1_nominal_week")
  private String driver1NominalWeek;

  @Column(name = "driver2_nominal_week")
  private String driver2NominalWeek;

  @Column(name = "driver1_insurance_week")
  private String driver1InsuranceWeek;

  @Column(name = "driver2_insurance_week")
  private String driver2InsuranceWeek;

  @RestResource(path = "expenses")
  @OneToMany
  @JoinTable(
      name = "payment_trip_expense",
      joinColumns = @JoinColumn(name = "payment_id"),
      inverseJoinColumns = @JoinColumn(name = "trip_expense_id"))
  private List<TripExpense> expenses;

  @RestResource(path = "advances")
  @OneToMany
  @JoinTable(
      name = "payment_trip_advance",
      joinColumns = @JoinColumn(name = "payment_id"),
      inverseJoinColumns = @JoinColumn(name = "trip_advance_id"))
  private List<TripAdvance> advances;

  @ManyToOne private PaymentCashCheckpoint paymentCashCheckpoint;

  public boolean isPayed() {
    return payed;
  }

  public void setPayed(boolean payed) {
    this.payed = payed;
  }

  public Date getPayDate() {
    return payDate;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }

  public SalesTerminal getPaymentTerminal() {
    return paymentTerminal;
  }

  public void setPaymentTerminal(SalesTerminal paymentTerminal) {
    this.paymentTerminal = paymentTerminal;
  }

  public PaymentShift getPaymentShift() {
    return paymentShift;
  }

  public void setPaymentShift(PaymentShift paymentShift) {
    this.paymentShift = paymentShift;
  }

  public Employee getDriver1() {
    return driver1;
  }

  public void setDriver1(Employee driver1) {
    this.driver1 = driver1;
  }

  public Employee getDriver2() {
    return driver2;
  }

  public void setDriver2(Employee driver2) {
    this.driver2 = driver2;
  }

  public List<Trip> getTrips() {
    return trips;
  }

  public void setTrips(List<Trip> trips) {
    this.trips = trips;
  }

  public BigDecimal getDriver1Expenses() {
    return driver1Expenses;
  }

  public void setDriver1Expenses(BigDecimal driver1Expenses) {
    this.driver1Expenses = driver1Expenses;
  }

  public BigDecimal getDriver2Expenses() {
    return driver2Expenses;
  }

  public void setDriver2Expenses(BigDecimal driver2Expenses) {
    this.driver2Expenses = driver2Expenses;
  }

  public BigDecimal getDriver1ExpensesAdvance() {
    return driver1ExpensesAdvance;
  }

  public void setDriver1ExpensesAdvance(BigDecimal driver1ExpensesAdvance) {
    this.driver1ExpensesAdvance = driver1ExpensesAdvance;
  }

  public BigDecimal getDriver2ExpensesAdvance() {
    return driver2ExpensesAdvance;
  }

  public void setDriver2ExpensesAdvance(BigDecimal driver2ExpensesAdvance) {
    this.driver2ExpensesAdvance = driver2ExpensesAdvance;
  }

  public BigDecimal getDriver1Earnings() {
    return driver1Earnings;
  }

  public void setDriver1Earnings(BigDecimal driver1Earnings) {
    this.driver1Earnings = driver1Earnings;
  }

  public BigDecimal getDriver2Earnings() {
    return driver2Earnings;
  }

  public void setDriver2Earnings(BigDecimal driver2Earnings) {
    this.driver2Earnings = driver2Earnings;
  }

  public BigDecimal getDriver1Loans() {
    return driver1Loans;
  }

  public void setDriver1Loans(BigDecimal driver1Loans) {
    this.driver1Loans = driver1Loans;
  }

  public BigDecimal getDriver2Loans() {
    return driver2Loans;
  }

  public void setDriver2Loans(BigDecimal driver2Loans) {
    this.driver2Loans = driver2Loans;
  }

  public BigDecimal getDriver1Nominal() {
    return driver1Nominal;
  }

  public void setDriver1Nominal(BigDecimal driver1Nominal) {
    this.driver1Nominal = driver1Nominal;
  }

  public BigDecimal getDriver2Nominal() {
    return driver2Nominal;
  }

  public void setDriver2Nominal(BigDecimal driver2Nominal) {
    this.driver2Nominal = driver2Nominal;
  }

  public BigDecimal getDriver1Insurance() {
    return driver1Insurance;
  }

  public void setDriver1Insurance(BigDecimal driver1Insurance) {
    this.driver1Insurance = driver1Insurance;
  }

  public BigDecimal getDriver2Insurance() {
    return driver2Insurance;
  }

  public void setDriver2Insurance(BigDecimal driver2Insurance) {
    this.driver2Insurance = driver2Insurance;
  }

  public String getDriver1NominalWeek() {
    return driver1NominalWeek;
  }

  public void setDriver1NominalWeek(String driver1NominalWeek) {
    this.driver1NominalWeek = driver1NominalWeek;
  }

  public String getDriver2NominalWeek() {
    return driver2NominalWeek;
  }

  public void setDriver2NominalWeek(String driver2NominalWeek) {
    this.driver2NominalWeek = driver2NominalWeek;
  }

  public String getDriver1InsuranceWeek() {
    return driver1InsuranceWeek;
  }

  public void setDriver1InsuranceWeek(String driver1InsuranceWeek) {
    this.driver1InsuranceWeek = driver1InsuranceWeek;
  }

  public String getDriver2InsuranceWeek() {
    return driver2InsuranceWeek;
  }

  public void setDriver2InsuranceWeek(String driver2InsuranceWeek) {
    this.driver2InsuranceWeek = driver2InsuranceWeek;
  }

  public List<TripExpense> getExpenses() {
    return expenses;
  }

  public void setExpenses(List<TripExpense> expenses) {
    this.expenses = expenses;
  }

  public List<TripAdvance> getAdvances() {
    return advances;
  }

  public void setAdvances(List<TripAdvance> advances) {
    this.advances = advances;
  }

  public PaymentCashCheckpoint getPaymentCashCheckpoint() {
    return paymentCashCheckpoint;
  }

  public void setPaymentCashCheckpoint(PaymentCashCheckpoint paymentCashCheckpoint) {
    this.paymentCashCheckpoint = paymentCashCheckpoint;
  }

  public BigDecimal getDriver1Amount() {
    return driver1Amount;
  }

  public void setDriver1Amount(BigDecimal driver1Amount) {
    this.driver1Amount = driver1Amount;
  }

  public BigDecimal getDriver2Amount() {
    return driver2Amount;
  }

  public void setDriver2Amount(BigDecimal driver2Amount) {
    this.driver2Amount = driver2Amount;
  }

  public String getPaymentNumber() {
    return paymentNumber;
  }

  public void setPaymentNumber(String paymentNumber) {
    this.paymentNumber = paymentNumber;
  }
}
