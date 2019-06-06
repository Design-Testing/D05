
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class EducationRecord extends DomainEntity {

	private String	degree;
	private String	institution;
	private Integer	mark;
	private Date	startDate;
	private Date	endDate;
	private String	attachment;
	private Boolean	isCertified;
	private Boolean	isDraft;


	@NotBlank
	@SafeHtml
	public String getDegree() {
		return this.degree;
	}

	public void setDegree(final String degree) {
		this.degree = degree;
	}

	@NotBlank
	@SafeHtml
	public String getInstitution() {
		return this.institution;
	}

	public void setInstitution(final String institution) {
		this.institution = institution;
	}

	@NotNull
	@Range(min = 0, max = 10)
	public Integer getMark() {
		return this.mark;
	}

	public void setMark(final Integer mark) {
		this.mark = mark;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getStartDate() {

		return this.startDate;

	}

	public void setStartDate(final Date startDate) {

		this.startDate = startDate;

	}

	// Optional
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getEndDate() {

		return this.endDate;

	}

	public void setEndDate(final Date endDate) {

		this.endDate = endDate;

	}

	@URL
	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(final String attachment) {
		this.attachment = attachment;
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
