
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


	//	public TimePeriod create() {
	//		final TimePeriod tPeriod = new TimePeriod();
	//		final Reservation reservation = new Reservation();
	//		tPeriod.setReservation(reservation);
	//		return tPeriod;
	//	}

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
		Assert.isTrue(timePeriod.getReservation().getStatus().equals("PENDING"), "No puede añadir un timePeriod si la reserva está en Final");
		schedule = this.setScheduleTrue(timePeriod);
		if (timePeriod.getId() != 0) {
			final TimePeriod tPeriod = this.findOne(timePeriod.getId());
			this.setScheduleFalse(tPeriod);
			Assert.isTrue(reservations.contains(timePeriod.getReservation()), "No puedes modificar un periodo de tiempo que no sea de su reserva.");
		}
		final TimePeriod tPeriod = this.timePeriodRepository.save(timePeriod);
		Assert.notNull(tPeriod);
		return tPeriod;
	}

	//	public void delete(final TimePeriod timePeriod) {
	//		Assert.notNull(timePeriod);
	//		Assert.isTrue(timePeriod.getId() != 0);
	//		final Actor principal = this.actorService.findByPrincipal();
	//		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.TEACHER));
	//		Schedule schedule = this.scheduleService.findScheduleByTeacher(this.teacherService.findByPrincipal());
	//		schedule = this.setScheduleFalse(timePeriod);
	//		final TimePeriod result = this.findOne(timePeriod.getId());
	//		this.timePeriodRepository.delete(result);
	//	}

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

	public Collection<TimePeriod> suggestTimePeriod(final int reservationId) {
		final Collection<TimePeriod> suggests = new ArrayList<>();
		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Teacher teacher = this.teacherService.findTeacherByReservation(reservationId);
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);

		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> mond = newMonday.subList(8, 21);
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> tues = newTuesday.subList(8, 21);
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> wedn = newWednesday.subList(8, 21);
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> thurs = newThursday.subList(8, 21);
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		final List<Boolean> frid = newFriday.subList(8, 21);

		if (mond.contains(false))
			for (int i = 8; i < mond.size(); i++) {
				if (!mond.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(1);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newMonday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (tues.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 8; i < tues.size(); i++) {
				if (!tues.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(2);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newTuesday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (wedn.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 8; i < wedn.size(); i++) {
				if (!wedn.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(3);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newWednesday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (thurs.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 8; i < thurs.size(); i++) {
				if (!thurs.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(4);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newThursday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (frid.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 8; i < frid.size(); i++)
				if (!frid.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(5);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newFriday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}

		schedule.setMonday(newMonday);
		schedule.setTuesday(newTuesday);
		schedule.setWednesday(newWednesday);
		schedule.setThursday(newThursday);
		schedule.setFriday(newFriday);
		this.scheduleService.save(schedule);

		return suggests;
	}

}
