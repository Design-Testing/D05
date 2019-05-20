
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class PersonalRecord extends DomainEntity {

	private String	fullName;
	private String	statement;
	private String	github;
	private String	linkedin;
	private String	photo;
	private Boolean	isCertified;
	private Boolean	isDraft;


	@NotBlank
	@SafeHtml
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
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

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotNull
	public Boolean getIsCertified() {
		return this.isCertified;
	}

	public void setIsCertified(final Boolean isCertified) {
		this.isCertified = isCertified;
	}

	@NotNull
	public Boolean getIsDraft() {
		return this.isDraft;
	}

	public void setIsDraft(final Boolean isDraft) {
		this.isDraft = isDraft;
	}

}
