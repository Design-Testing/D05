
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TimePeriodRepository;
import security.Authority;
import domain.Actor;
import domain.Reservation;
import domain.Schedule;
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

	@Autowired
	private ScheduleService			scheduleService;


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
		Assert.isTrue(!this.setTimePeriod(timePeriod) == true, "Este tramo horario ya ha sido escogido.");
		final Collection<TimePeriod> timePeriods = this.findByReservation(timePeriod.getReservation().getId());
		Assert.isTrue(timePeriod.getReservation().getHoursWeek() > timePeriods.size(), "El número de tramos horarios debe ser menor o igual a las horas semanales solicitadas.");
		Schedule schedule = this.scheduleService.findScheduleByTeacher(this.teacherService.findByPrincipal());
		schedule = this.setScheduleTrue(timePeriod);
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
		Schedule schedule = this.scheduleService.findScheduleByTeacher(this.teacherService.findByPrincipal());
		schedule = this.setScheduleFalse(timePeriod);
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
	private Schedule setScheduleTrue(final TimePeriod timePeriod) {
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		if (timePeriod.getDayNumber() == 1) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getMonday();
					res.set(i - 1, true);
				}
		} else if (timePeriod.getDayNumber() == 2) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getTuesday();
					res.set(i - 1, true);
				}
		} else if (timePeriod.getDayNumber() == 3) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getWednesday();
					res.set(i - 1, true);
				}
		} else if (timePeriod.getDayNumber() == 4) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getThursday();
					res.set(i - 1, true);
				}
		} else if (timePeriod.getDayNumber() == 5)
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getFriday();
					res.set(i - 1, true);
				}
		return schedule;
	}

	private Schedule setScheduleFalse(final TimePeriod timePeriod) {
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		if (timePeriod.getDayNumber() == 1) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getMonday();
					res.set(i - 1, false);
				}
		} else if (timePeriod.getDayNumber() == 2) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getTuesday();
					res.set(i - 1, false);
				}
		} else if (timePeriod.getDayNumber() == 3) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getWednesday();
					res.set(i - 1, false);
				}
		} else if (timePeriod.getDayNumber() == 4) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getThursday();
					res.set(i - 1, false);
				}
		} else if (timePeriod.getDayNumber() == 5)
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getFriday();
					res.set(i - 1, false);
				}
		return schedule;
	}

	private Boolean setTimePeriod(final TimePeriod timePeriod) {
		Boolean result = false;
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		if (timePeriod.getDayNumber() == 1) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getMonday();
					result = res.get(i - 1);
				}
		} else if (timePeriod.getDayNumber() == 2) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getTuesday();
					result = res.get(i - 1);
				}
		} else if (timePeriod.getDayNumber() == 3) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getWednesday();
					result = res.get(i - 1);
				}
		} else if (timePeriod.getDayNumber() == 4) {
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getThursday();
					result = res.get(i - 1);
				}
		} else if (timePeriod.getDayNumber() == 5)
			for (int i = 0; i < 24; i++)
				if (timePeriod.getEndHour() == i) {
					final List<Boolean> res = (List<Boolean>) schedule.getFriday();
					result = res.get(i - 1);
				}
		return result;
	}

}
