
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Subject extends DomainEntity {

	private String	nameEn;
	private String	nameEs;
	private String	descriptionEn;
	private String	descriptionEs;
	private String	level;


	@NotBlank
	@SafeHtml
	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(final String nameEn) {
		this.nameEn = nameEn;
	}

	@NotBlank
	@SafeHtml
	public String getNameEs() {
		return this.nameEs;
	}

	public void setNameEs(final String nameEs) {
		this.nameEs = nameEs;
	}

	@NotBlank
	@SafeHtml
	public String getDescriptionEn() {
		return this.descriptionEn;
	}

	public void setDescriptionEn(final String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	@NotBlank
	@SafeHtml
	public String getDescriptionEs() {
		return this.descriptionEs;
	}

	public void setDescriptionEs(final String descriptionEs) {
		this.descriptionEs = descriptionEs;
	}

	@NotBlank
	@SafeHtml
	public String getLevel() {
		return this.level;
	}

	public void setLevel(final String level) {
		this.level = level;
	}

}
