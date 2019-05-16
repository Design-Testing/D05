
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Curricula extends DomainEntity {

	private PersonalData					personalRecord;
	private Collection<EducationData>		educations;
	private Collection<PositionData>		positions;
	private Collection<MiscellaneousData>	miscellaneous;
	private Rooky							rooky;


	@Valid
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	public PersonalData getPersonalRecord() {
		return this.personalRecord;
	}

	public void setPersonalRecord(final PersonalData personalRecord) {
		this.personalRecord = personalRecord;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<EducationData> getEducations() {
		return this.educations;
	}

	public void setEducations(final Collection<EducationData> educations) {
		this.educations = educations;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<PositionData> getPositions() {
		return this.positions;
	}

	public void setPositions(final Collection<PositionData> positions) {
		this.positions = positions;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousData> getMiscellaneous() {
		return this.miscellaneous;
	}

	public void setMiscellaneous(final Collection<MiscellaneousData> miscellaneous) {
		this.miscellaneous = miscellaneous;
	}

	@ManyToOne(optional = true)
	public Rooky getRooky() {
		return this.rooky;
	}

	public void setRooky(final Rooky rooky) {
		this.rooky = rooky;
	}

}
