
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class PersonalData extends DomainEntity {

	private String	fullName;
	private String	statement;
	private String	github;
	private String	phone;
	private String	linkedin;


	@NotBlank
	@SafeHtml
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@NotBlank
	@SafeHtml
	@Pattern(regexp = "((\\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8})||''")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@NotBlank
	@URL
	@SafeHtml
	public String getLinkedin() {
		return this.linkedin;
	}

	public void setLinkedin(final String linkedin) {
		this.linkedin = linkedin;
	}

	@NotBlank
	@SafeHtml
	public String getStatement() {
		return this.statement;
	}

	public void setStatement(final String statement) {
		this.statement = statement;
	}

	@NotBlank
	@URL
	@SafeHtml
	public String getGithub() {
		return this.github;
	}

	public void setGithub(final String github) {
		this.github = github;
	}

}
