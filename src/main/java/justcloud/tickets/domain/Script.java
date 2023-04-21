package justcloud.tickets.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Script extends BaseModel {

  private String name;

  private String language;

  private String description;

  @ManyToOne private ScriptCategory category;

  private String variables;

  @NotBlank private String body;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the language */
  public String getLanguage() {
    return language;
  }

  /** @param language the language to set */
  public void setLanguage(String language) {
    this.language = language;
  }

  /** @return the description */
  public String getDescription() {
    return description;
  }

  /** @param description the description to set */
  public void setDescription(String description) {
    this.description = description;
  }

  /** @return the category */
  public ScriptCategory getCategory() {
    return category;
  }

  /** @param category the category to set */
  public void setCategory(ScriptCategory category) {
    this.category = category;
  }

  /** @return the variables */
  public String getVariables() {
    return variables;
  }

  /** @param variables the variables to set */
  public void setVariables(String variables) {
    this.variables = variables;
  }

  /** @return the body */
  public String getBody() {
    return body;
  }

  /** @param body the body to set */
  public void setBody(String body) {
    this.body = body;
  }
}
