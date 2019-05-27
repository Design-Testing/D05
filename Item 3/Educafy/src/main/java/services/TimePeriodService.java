
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TimePeriodRepository;
import security.Authority;
import domain.Actor;
import domain.Reservation;
import domain.Teacher;
import domain.TimePeriod;

@Service
@Transactional
public class TimePeriodService {

	@Autowired
	private TimePeriodRepository	timePeriodRepository;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ReservationService		reservationService;


	public TimePeriod create() {
		final TimePeriod tPeriod = new TimePeriod();
		final Reservation reservation = new Reservation();
		tPeriod.setReservation(reservation);
		return tPeriod;
	}

	public TimePeriod findOne(final int tPeriodId) {
		Assert.isTrue(tPeriodId != 0);
		final TimePeriod res = this.timePeriodRepository.findOne(tPeriodId);
		Assert.notNull(res);
		return res;
	}

	public Collection<TimePeriod> findAll() {
		Collection<TimePeriod> res = new ArrayList<>();
		res = this.timePeriodRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public TimePeriod save(final TimePeriod timePeriod) {
		Assert.notNull(timePeriod);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.TEACHER), "Esta Acción solo la pueden hacer los profesores.");
		final Integer tramoHorario = timePeriod.getEndHour() - timePeriod.getStartHour();
		Assert.isTrue(tramoHorario == 1, "Los tramos horarios deben ser de 1 hora.");
		final Collection<Reservation> reservations = this.reservationService.findAllReservationByTeacher(principal.getUserAccount().getId());
		if (timePeriod.getId() != 0)
			Assert.isTrue(reservations.contains(timePeriod.getReservation()), "No puedes modificar un periodo de tiempo que no sea de su reserva.");
		final TimePeriod tPeriod = this.timePeriodRepository.save(timePeriod);
		Assert.notNull(tPeriod);
		return tPeriod;
	}
	public void delete(final TimePeriod timePeriod) {
		Assert.notNull(timePeriod);
		Assert.isTrue(timePeriod.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.TEACHER));
		final TimePeriod result = this.findOne(timePeriod.getId());
		this.timePeriodRepository.delete(result);
	}

	public Collection<TimePeriod> findByReservation(final Integer reservationId) {
		final Collection<TimePeriod> res = this.timePeriodRepository.findByReservation(reservationId);
		Assert.notNull(res);
		return res;
	}

	public Collection<TimePeriod> findTimePeriodsByTeacher(final int teacherId) {
		Collection<TimePeriod> res;
		final Teacher teacher = this.teacherService.findOne(teacherId);
		res = this.timePeriodRepository.findTimePeriodsByTeacher(teacher.getUserAccount().getId());
		return res;
	}
	public void deleteInBatch(final Collection<TimePeriod> timePeriods) {
		this.timePeriodRepository.deleteInBatch(timePeriods);

	}

}
