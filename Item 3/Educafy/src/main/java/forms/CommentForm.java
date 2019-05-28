
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class CommentForm extends DomainEntity {

	private String	text;


	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
