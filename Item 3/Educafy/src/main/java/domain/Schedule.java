
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.PROPERTY)
public class Schedule extends DomainEntity {

	private int[]	lunes;
	private int[]	martes;
	private int[]	miercoles;
	private int[]	jueves;
	private int[]	viernes;

	//Relaciones
	private Teacher	teacher;


	public int[] getLunes() {
		return this.lunes;
	}

	public void setLunes(final int[] lunes) {
		this.lunes = lunes;
	}

	public int[] getMartes() {
		return this.martes;
	}

	public void setMartes(final int[] martes) {
		this.martes = martes;
	}

	public int[] getMiercoles() {
		return this.miercoles;
	}

	public void setMiercoles(final int[] miercoles) {
		this.miercoles = miercoles;
	}

	public int[] getJueves() {
		return this.jueves;
	}

	public void setJueves(final int[] jueves) {
		this.jueves = jueves;
	}

	public int[] getViernes() {
		return this.viernes;
	}

	public void setViernes(final int[] viernes) {
		this.viernes = viernes;
	}

	@ManyToOne(optional = false)
	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}

}
