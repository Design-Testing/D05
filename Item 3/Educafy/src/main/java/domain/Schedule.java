
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "teacher")
})
public class Schedule extends DomainEntity {

	private Collection<Boolean>	monday;
	private Collection<Boolean>	tuesday;
	private Collection<Boolean>	wednesday;
	private Collection<Boolean>	thursday;
	private Collection<Boolean>	friday;

	//Relaciones
	private Teacher				teacher;


	@ElementCollection
	public Collection<Boolean> getMonday() {
		return this.monday;
	}

	public void setMonday(final Collection<Boolean> monday) {
		this.monday = monday;
	}

	@ElementCollection
	public Collection<Boolean> getTuesday() {
		return this.tuesday;
	}

	public void setTuesday(final Collection<Boolean> tuesday) {
		this.tuesday = tuesday;
	}

	@ElementCollection
	public Collection<Boolean> getWednesday() {
		return this.wednesday;
	}

	public void setWednesday(final Collection<Boolean> wednesday) {
		this.wednesday = wednesday;
	}

	@ElementCollection
	public Collection<Boolean> getThursday() {
		return this.thursday;
	}

	public void setThursday(final Collection<Boolean> thursday) {
		this.thursday = thursday;
	}

	@ElementCollection
	public Collection<Boolean> getFriday() {
		return this.friday;
	}

	public void setFriday(final Collection<Boolean> friday) {
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
