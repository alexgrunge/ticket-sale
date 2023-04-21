package justcloud.tickets.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Municipality extends BaseModel {

  @ManyToOne private State state;

  @OneToMany(mappedBy = "municipality")
  private List<Settlement> settlements;

  private String name;

  /** @return the state */
  public State getState() {
    return state;
  }

  /** @param state the state to set */
  public void setState(State state) {
    this.state = state;
  }

  /** @return the settlements */
  public List<Settlement> getSettlements() {
    return settlements;
  }

  /** @param settlements the settlements to set */
  public void setSettlements(List<Settlement> settlements) {
    this.settlements = settlements;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }
}
