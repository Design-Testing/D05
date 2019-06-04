
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "reservation")
})
public class TimePeriod extends DomainEntity {

	private Integer		startHour;
	private Integer		endHour;
	private Integer		dayNumber;

	//Relaciones
	private Reservation	reservation;


	@Range(min = 8, max = 20)
	public Integer getStartHour() {
		return this.startHour;
	}

	public void setStartHour(final Integer startHour) {
		this.startHour = startHour;
	}

	@Range(min = 9, max = 21)
	public Integer getEndHour() {
		return this.endHour;
	}

	public void setEndHour(final Integer endHour) {
		this.endHour = endHour;
	}

	@Range(min = 1, max = 5)
	public Integer getDayNumber() {
		return this.dayNumber;
	}

	public void setDayNumber(final Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	@ManyToOne(optional = false)
	public Reservation getReservation() {
		return this.reservation;
	}

	public void setReservation(final Reservation reservation) {
		this.reservation = reservation;
	}

}
