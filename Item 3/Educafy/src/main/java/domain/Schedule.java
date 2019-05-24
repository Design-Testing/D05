
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.PROPERTY)
public class Schedule extends DomainEntity {

	private Collection<String>	monday;
	private Collection<String>	tuesday;
	private Collection<String>	wednesday;
	private Collection<String>	thursday;
	private Collection<String>	friday;

	//Relaciones
	private Teacher				teacher;


	@ElementCollection
	public Collection<String> getMonday() {
		return this.monday;
	}

	public void setMonday(final Collection<String> monday) {
		this.monday = monday;
	}

	@ElementCollection
	public Collection<String> getTuesday() {
		return this.tuesday;
	}

	public void setTuesday(final Collection<String> tuesday) {
		this.tuesday = tuesday;
	}

	@ElementCollection
	public Collection<String> getWednesday() {
		return this.wednesday;
	}

	public void setWednesday(final Collection<String> wednesday) {
		this.wednesday = wednesday;
	}

	@ElementCollection
	public Collection<String> getThursday() {
		return this.thursday;
	}

	public void setThursday(final Collection<String> thursday) {
		this.thursday = thursday;
	}

	@ElementCollection
	public Collection<String> getFriday() {
		return this.friday;
	}

	public void setFriday(final Collection<String> friday) {
		this.friday = friday;
	}

	@ManyToOne(optional = false)
	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}

}
