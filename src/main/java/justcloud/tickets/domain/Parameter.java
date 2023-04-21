package justcloud.tickets.domain;

import java.util.Date;
import javax.persistence.*;

@Entity
public class Parameter {

  @Id
  @Column(updatable = false)
  private String name;

  private String value;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true, updatable = false)
  private Date dateCreated;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true)
  private Date lastUpdated;

  @Version private Long version;

  @PrePersist
  public void prePersistBase() {
    dateCreated = new Date();
    lastUpdated = dateCreated;
  }

  @PreUpdate
  public void preUpdateBase() {
    lastUpdated = new Date();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /** @return the dateCreated */
  public Date getDateCreated() {
    return dateCreated;
  }

  /** @param dateCreated the dateCreated to set */
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  /** @return the lastUpdated */
  public Date getLastUpdated() {
    return lastUpdated;
  }

  /** @param lastUpdated the lastUpdated to set */
  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /** @return the version */
  public Long getVersion() {
    return version;
  }

  /** @param version the version to set */
  public void setVersion(Long version) {
    this.version = version;
  }
}
