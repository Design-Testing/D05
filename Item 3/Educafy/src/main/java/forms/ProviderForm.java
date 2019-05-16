
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class ProviderForm extends ActorForm {

	private String	providerMake;


	@NotBlank
	@SafeHtml
	public String getProviderMake() {
		return this.providerMake;
	}

	public void setProviderMake(final String providerMake) {
		this.providerMake = providerMake;
	}

}
