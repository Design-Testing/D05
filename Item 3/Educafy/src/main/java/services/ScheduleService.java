
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ScheduleRepository;
import domain.Schedule;
import domain.Teacher;

@Service
@Transactional
public class ScheduleService {

	@Autowired
	private ScheduleRepository	scheduleRepository;

	@Autowired
	private TeacherService		teacherService;


	public Schedule create() {
		final Schedule schedule = new Schedule();
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

		if (schedule.getId() == 0) {
			final List<String> monday = new ArrayList<>();
			schedule.setMonday(monday);

			final List<String> tuesday = new ArrayList<>();
			schedule.setTuesday(tuesday);

			final List<String> wednesday = new ArrayList<>();
			schedule.setWednesday(wednesday);

			final List<String> thursday = new ArrayList<>();
			schedule.setThursday(thursday);

			final List<String> friday = new ArrayList<>();
			schedule.setFriday(friday);

			schedule.setTeacher(principal);
		} else
			Assert.isTrue(schedule.getTeacher().equals(principal), "No puede actualizar un horario que no es suyo.");
		result = this.scheduleRepository.save(schedule);
		return result;

	}

	/* ========================= OTHER METHODS =========================== */

}
