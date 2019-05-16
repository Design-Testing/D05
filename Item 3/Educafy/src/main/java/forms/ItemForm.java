
package forms;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachURL;
import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class ItemForm extends DomainEntity {

	private String				name;
	private String				description;
	private Collection<String>	links;
	private String				photo;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@ElementCollection
	@EachURL
	public Collection<String> getLinks() {
		return this.links;
	}

	public void setLinks(final Collection<String> links) {
		this.links = links;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

}
