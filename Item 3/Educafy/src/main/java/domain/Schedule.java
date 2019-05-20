
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.PROPERTY)
public class Schedule extends DomainEntity {

	private int[]	monday;
	private int[]	tuesday;
	private int[]	wednesday;
	private int[]	thursday;
	private int[]	friday;

	//Relaciones
	private Teacher	teacher;


	public int[] getMonday() {
		return this.monday;
	}

	public void setMonday(final int[] monday) {
		this.monday = monday;
	}

	public int[] getTuesday() {
		return this.tuesday;
	}

	public void setTuesday(final int[] tuesday) {
		this.tuesday = tuesday;
	}

	public int[] getWednesday() {
		return this.wednesday;
	}

	public void setWednesday(final int[] wednesday) {
		this.wednesday = wednesday;
	}

	public int[] getThursday() {
		return this.thursday;
	}

	public void setThursday(final int[] thursday) {
		this.thursday = thursday;
	}

	public int[] getFriday() {
		return this.friday;
	}

	public void setFriday(final int[] friday) {
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
