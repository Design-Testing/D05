
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	private String	name;
	private String	description;
	private String	level;


	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

}
