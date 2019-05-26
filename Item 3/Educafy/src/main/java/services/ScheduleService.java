
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ScheduleRepository;
import domain.Reservation;
import domain.Schedule;
import domain.Teacher;
import domain.TimePeriod;

@Service
@Transactional
public class ScheduleService {

	@Autowired
	private ScheduleRepository	scheduleRepository;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private TimePeriodService	timePeriodService;

	@Autowired
	private ReservationService	reservationService;


	public Schedule create() {
		final Schedule schedule = new Schedule();

		final List<Boolean> monday = new ArrayList<>();
		monday.add(0, false);
		monday.add(1, false);
		monday.add(2, false);
		monday.add(3, false);
		monday.add(4, false);
		monday.add(5, false);
		monday.add(6, false);
		monday.add(7, false);
		monday.add(8, false);
		monday.add(9, false);
		monday.add(10, false);
		monday.add(11, false);
		monday.add(12, false);
		monday.add(13, false);
		monday.add(14, false);
		monday.add(15, false);
		monday.add(16, false);
		monday.add(17, false);
		monday.add(18, false);
		monday.add(19, false);
		monday.add(20, false);
		monday.add(21, false);
		monday.add(22, false);
		monday.add(23, false);

		schedule.setMonday(monday);

		final List<Boolean> tuesday = new ArrayList<>();
		tuesday.add(0, false);
		tuesday.add(1, false);
		tuesday.add(2, false);
		tuesday.add(3, false);
		tuesday.add(4, false);
		tuesday.add(5, false);
		tuesday.add(6, false);
		tuesday.add(7, false);
		tuesday.add(8, false);
		tuesday.add(9, false);
		tuesday.add(10, false);
		tuesday.add(11, false);
		tuesday.add(12, false);
		tuesday.add(13, false);
		tuesday.add(14, false);
		tuesday.add(15, false);
		tuesday.add(16, false);
		tuesday.add(17, false);
		tuesday.add(18, false);
		tuesday.add(19, false);
		tuesday.add(20, false);
		tuesday.add(21, false);
		tuesday.add(22, false);
		tuesday.add(23, false);
		schedule.setTuesday(tuesday);

		final List<Boolean> wednesday = new ArrayList<>();
		wednesday.add(0, false);
		wednesday.add(1, false);
		wednesday.add(2, false);
		wednesday.add(3, false);
		wednesday.add(4, false);
		wednesday.add(5, false);
		wednesday.add(6, false);
		wednesday.add(7, false);
		wednesday.add(8, false);
		wednesday.add(9, false);
		wednesday.add(10, false);
		wednesday.add(11, false);
		wednesday.add(12, false);
		wednesday.add(13, false);
		wednesday.add(14, false);
		wednesday.add(15, false);
		wednesday.add(16, false);
		wednesday.add(17, false);
		wednesday.add(18, false);
		wednesday.add(19, false);
		wednesday.add(20, false);
		wednesday.add(21, false);
		wednesday.add(22, false);
		wednesday.add(23, false);
		schedule.setWednesday(wednesday);

		final List<Boolean> thursday = new ArrayList<>();
		thursday.add(0, false);
		thursday.add(1, false);
		thursday.add(2, false);
		thursday.add(3, false);
		thursday.add(4, false);
		thursday.add(5, false);
		thursday.add(6, false);
		thursday.add(7, false);
		thursday.add(8, false);
		thursday.add(9, false);
		thursday.add(10, false);
		thursday.add(11, false);
		thursday.add(12, false);
		thursday.add(13, false);
		thursday.add(14, false);
		thursday.add(15, false);
		thursday.add(16, false);
		thursday.add(17, false);
		thursday.add(18, false);
		thursday.add(19, false);
		thursday.add(20, false);
		thursday.add(21, false);
		thursday.add(22, false);
		thursday.add(23, false);
		schedule.setThursday(thursday);

		final List<Boolean> friday = new ArrayList<>();
		friday.add(0, false);
		friday.add(1, false);
		friday.add(2, false);
		friday.add(3, false);
		friday.add(4, false);
		friday.add(5, false);
		friday.add(6, false);
		friday.add(7, false);
		friday.add(8, false);
		friday.add(9, false);
		friday.add(10, false);
		friday.add(11, false);
		friday.add(12, false);
		friday.add(13, false);
		friday.add(14, false);
		friday.add(15, false);
		friday.add(16, false);
		friday.add(17, false);
		friday.add(18, false);
		friday.add(19, false);
		friday.add(20, false);
		friday.add(21, false);
		friday.add(22, false);
		friday.add(23, false);
		schedule.setFriday(friday);

		return schedule;
	}

	public Schedule findOne(final int scheduleId) {
		Assert.isTrue(scheduleId != 0);
		final Schedule result = this.scheduleRepository.findOne(scheduleId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Schedule> findAll() {
		Collection<Schedule> res = new ArrayList<>();
		res = this.scheduleRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Schedule save(final Schedule schedule) {
		Assert.notNull(schedule);
		final Teacher principal = this.teacherService.findByPrincipal();
		final Schedule result;

		if (schedule.getId() == 0)
			schedule.setTeacher(principal);
		else
			Assert.isTrue(schedule.getTeacher().equals(principal), "No puede actualizar un horario que no es suyo.");
		result = this.scheduleRepository.save(schedule);
		return result;

	}

	/* ========================= OTHER METHODS =========================== */

	public Schedule findScheduleByTeacher(final Teacher teacher) {
		Schedule res;
		res = this.scheduleRepository.findeScheduleByTeacher(teacher.getUserAccount().getId());
		return res;
	}

	public Schedule createForNewTeacher(final Teacher teacher) {
		Schedule result;

		result = this.create();
		result.setTeacher(teacher);

		final Schedule saved = this.scheduleRepository.save(result);

		return saved;
	}

	public Schedule updateSchedule(final int scheduleId) {
		final Schedule result = this.scheduleRepository.findOne(scheduleId);

		final Collection<TimePeriod> timePeriods = this.timePeriodService.findTimePeriodsByTeacher(result.getTeacher().getId());

		for (final TimePeriod t : timePeriods)
			if (t.getDayNumber() == 1) {
				final List<Boolean> newMonday = (List<Boolean>) result.getMonday();
				newMonday.set(t.getStartHour(), true);
				result.setMonday(newMonday);
			} else if (t.getDayNumber() == 2) {
				final List<Boolean> newTuesday = (List<Boolean>) result.getTuesday();
				newTuesday.set(t.getStartHour(), true);
				result.setTuesday(newTuesday);
			} else if (t.getDayNumber() == 3) {
				final List<Boolean> newWednesday = (List<Boolean>) result.getWednesday();
				newWednesday.set(t.getStartHour(), true);
				result.setWednesday(newWednesday);
			} else if (t.getDayNumber() == 4) {
				final List<Boolean> newThursday = (List<Boolean>) result.getThursday();
				newThursday.set(t.getStartHour(), true);
				result.setThursday(newThursday);
			} else {
				final List<Boolean> newFriday = (List<Boolean>) result.getFriday();
				newFriday.set(t.getStartHour(), true);
				result.setFriday(newFriday);
			}

		final Schedule saved = this.scheduleRepository.save(result);
		return saved;
	}

	public Collection<TimePeriod> suggestTimePeriod(final int reservationId) {
		final Collection<TimePeriod> suggests = new ArrayList<>();
		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Teacher teacher = this.teacherService.findTeacherByReservation(reservationId);
		final Schedule schedule = this.findScheduleByTeacher(teacher);

		if (schedule.getMonday().contains(false))
			for (int i = 0; i < schedule.getMonday().size(); i++) {
				final List<Boolean> newMonday = (List<Boolean>) schedule.getMonday();
				if (!newMonday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(1);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);

					suggests.add(suggest);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (schedule.getTuesday().contains(false))
			for (int i = 0; i < schedule.getTuesday().size(); i++) {
				final List<Boolean> newTuesday = (List<Boolean>) schedule.getTuesday();
				if (!newTuesday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(2);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);

					suggests.add(suggest);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (schedule.getWednesday().contains(false))
			for (int i = 0; i < schedule.getWednesday().size(); i++) {
				final List<Boolean> newWednesday = (List<Boolean>) schedule.getWednesday();
				if (!newWednesday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(3);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);

					suggests.add(suggest);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (schedule.getThursday().contains(false))
			for (int i = 0; i < schedule.getThursday().size(); i++) {
				final List<Boolean> newThursday = (List<Boolean>) schedule.getThursday();
				if (!newThursday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(4);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);

					suggests.add(suggest);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}
		else if (schedule.getFriday().contains(false))
			for (int i = 0; i < schedule.getFriday().size(); i++) {
				final List<Boolean> newFriday = (List<Boolean>) schedule.getFriday();
				if (!newFriday.get(i)) {
					final TimePeriod suggest = new TimePeriod();
					suggest.setDayNumber(5);
					suggest.setStartHour(i);
					suggest.setEndHour(i + 1);

					suggests.add(suggest);

					if (reservation.getHoursWeek() == suggests.size())
						break;
				}
			}

		return suggests;
	}
}
