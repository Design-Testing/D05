
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Curriculum extends DomainEntity {

	private PersonalRecord					personalRecord;
	private Collection<EducationRecord>		educationRecords;
	private Collection<MiscellaneousRecord>	miscellaneousRecords;
	private Teacher							teacher;
	private String							ticker;


	@Valid
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	public PersonalRecord getPersonalRecord() {
		return this.personalRecord;
	}

	public void setPersonalRecord(final PersonalRecord personalRecord) {
		this.personalRecord = personalRecord;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<EducationRecord> getEducationRecords() {
		return this.educationRecords;
	}

	public void setEducationRecords(final Collection<EducationRecord> educationRecords) {
		this.educationRecords = educationRecords;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}

	public void setMiscellaneousRecords(final Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

	@ManyToOne(optional = false)
	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[0-9]{6}[A-Z]{5}$")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

}
