
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TimePeriodRepository;
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
		Assert.isTrue(this.teacherService.findTeacherByReservation(timePeriod.getReservation().getId()).equals(principal), "Esta Acción solo la pueden hacer los profesores.");
		final Integer tramoHorario = timePeriod.getEndHour() - timePeriod.getStartHour();
		Assert.isTrue(tramoHorario == 1, "Los tramos horarios deben ser de 1 hora.");
		final Collection<Reservation> reservations = this.reservationService.findAllReservationByTeacher(principal.getUserAccount().getId());
		Assert.isTrue(!this.setTimePeriod(timePeriod) == true, "Este tramo horario ya ha sido escogido.");
		final Collection<TimePeriod> timePeriods = this.findByReservation(timePeriod.getReservation().getId());
		Schedule schedule = this.scheduleService.findScheduleByTeacher(this.teacherService.findByPrincipal());
		Assert.isTrue(timePeriod.getReservation().getStatus().equals("PENDING"), "No puede añadir un timePeriod si la reserva está en Final");
		schedule = this.setScheduleTrue(timePeriod);
		if (timePeriod.getId() != 0) {
			final TimePeriod t = this.findOne(timePeriod.getId());
			this.setScheduleFalse(t);
			Assert.isTrue(reservations.contains(timePeriod.getReservation()), "No puedes modificar un periodo de tiempo que no sea de su reserva.");
		}
		final TimePeriod tPeriod = this.timePeriodRepository.save(timePeriod);
		Assert.notNull(tPeriod);
		return tPeriod;
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
		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		if (timePeriod.getDayNumber() == 1) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newMonday.set(i, true);
		} else if (timePeriod.getDayNumber() == 2) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newTuesday.set(i - 1, true);
		} else if (timePeriod.getDayNumber() == 3) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newWednesday.set(i - 1, true);
		} else if (timePeriod.getDayNumber() == 4) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newThursday.set(i - 1, true);
		} else if (timePeriod.getDayNumber() == 5)
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newFriday.set(i - 1, true);
		schedule.setMonday(newMonday);
		schedule.setTuesday(newTuesday);
		schedule.setWednesday(newWednesday);
		schedule.setThursday(newThursday);
		schedule.setFriday(newFriday);
		this.scheduleService.save(schedule);
		return schedule;
	}

	private Schedule setScheduleFalse(final TimePeriod timePeriod) {
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		if (timePeriod.getDayNumber() == 1) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newMonday.set(i, false);
		} else if (timePeriod.getDayNumber() == 2) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newTuesday.set(i, false);
		} else if (timePeriod.getDayNumber() == 3) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newWednesday.set(i, false);
		} else if (timePeriod.getDayNumber() == 4) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newThursday.set(i, false);
		} else if (timePeriod.getDayNumber() == 5)
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8)
					newFriday.set(i, false);

		schedule.setMonday(newMonday);
		schedule.setTuesday(newTuesday);
		schedule.setWednesday(newWednesday);
		schedule.setThursday(newThursday);
		schedule.setFriday(newFriday);
		this.scheduleService.save(schedule);
		return schedule;
	}

	public Schedule setScheduleFalse(final Collection<TimePeriod> timePeriods, final Teacher teacher) {
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		for (final TimePeriod timePeriod : timePeriods)

			if (timePeriod.getDayNumber() == 1) {
				for (int i = 0; i < 14; i++)
					if (timePeriod.getStartHour() == i + 8)
						newMonday.set(i, false);
			} else if (timePeriod.getDayNumber() == 2) {
				for (int i = 0; i < 14; i++)
					if (timePeriod.getStartHour() == i + 8)
						newTuesday.set(i, false);
			} else if (timePeriod.getDayNumber() == 3) {
				for (int i = 0; i < 14; i++)
					if (timePeriod.getStartHour() == i + 8)
						newWednesday.set(i, false);
			} else if (timePeriod.getDayNumber() == 4) {
				for (int i = 0; i < 14; i++)
					if (timePeriod.getStartHour() == i + 8)
						newThursday.set(i, false);
			} else if (timePeriod.getDayNumber() == 5)
				for (int i = 0; i < 14; i++)
					if (timePeriod.getStartHour() == i + 8)
						newFriday.set(i, false);
		schedule.setMonday(newMonday);
		schedule.setTuesday(newTuesday);
		schedule.setWednesday(newWednesday);
		schedule.setThursday(newThursday);
		schedule.setFriday(newFriday);
		this.scheduleService.save2(schedule, teacher);
		return schedule;
	}
	private Boolean setTimePeriod(final TimePeriod timePeriod) {
		Boolean result = false;
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		if (timePeriod.getDayNumber() == 1) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8) {
					final List<Boolean> res = (List<Boolean>) schedule.getMonday();
					result = res.get(i);
				}
		} else if (timePeriod.getDayNumber() == 2) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8) {
					final List<Boolean> res = (List<Boolean>) schedule.getTuesday();
					result = res.get(i);
				}
		} else if (timePeriod.getDayNumber() == 3) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8) {
					final List<Boolean> res = (List<Boolean>) schedule.getWednesday();
					result = res.get(i);
				}
		} else if (timePeriod.getDayNumber() == 4) {
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8) {
					final List<Boolean> res = (List<Boolean>) schedule.getThursday();
					result = res.get(i);
				}
		} else if (timePeriod.getDayNumber() == 5)
			for (int i = 0; i < 14; i++)
				if (timePeriod.getStartHour() == i + 8) {
					final List<Boolean> res = (List<Boolean>) schedule.getFriday();
					result = res.get(i);
				}
		return result;
	}

	public Integer timePeriodFree(final Schedule schedule) {
		Integer contador = 0;
		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
		for (final Boolean b : newMonday)
			if (b == false)
				contador++;
		for (final Boolean b : newTuesday)
			if (b == false)
				contador++;
		for (final Boolean b : newWednesday)
			if (b == false)
				contador++;
		for (final Boolean b : newThursday)
			if (b == false)
				contador++;
		for (final Boolean b : newFriday)
			if (b == false)
				contador++;

		return contador;
	}

	public Collection<TimePeriod> suggestTimePeriod(final int reservationId) {
		final Collection<TimePeriod> suggests = new ArrayList<>();
		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Teacher teacher = this.teacherService.findTeacherByReservation(reservationId);
		final Schedule schedule = this.scheduleService.findScheduleByTeacher(teacher);
		Assert.notNull(schedule);
		final Collection<TimePeriod> timePeriods = this.findByReservation(reservationId);
		Assert.isTrue(reservation.getHoursWeek() > timePeriods.size(), "El número de tramos horarios debe ser menor o igual a las horas semanales solicitadas.");
		Assert.isTrue(this.timePeriodFree(schedule) >= reservation.getHoursWeek(), "El número de tramos horarios libres debe ser mayor o igual a las horas semanales solicitadas.");

		final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
		final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
		final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
		final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
		final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();

		if (newMonday.contains(false))
			for (int i = 0; i < newMonday.size(); i++)
				if (!newMonday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(1);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newMonday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newTuesday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newTuesday.size(); i++)
				if (!newTuesday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(2);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newTuesday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newWednesday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newWednesday.size(); i++)
				if (!newWednesday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(3);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newWednesday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newThursday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newThursday.size(); i++)
				if (!newThursday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(4);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
					suggest.setReservation(reservation);
					final TimePeriod saved = this.timePeriodRepository.save(suggest);
					suggests.add(saved);

					newThursday.set(i, true);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
		if (newFriday.contains(false) && reservation.getHoursWeek() > suggests.size())
			for (int i = 0; i < newFriday.size(); i++)
				if (!newFriday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(5);
					suggest.setStartHour(i + 8);
					suggest.setEndHour(i + 9);
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

	public void flush() {
		this.timePeriodRepository.flush();
	}
}
