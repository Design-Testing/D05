
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LessonRepository;
import domain.Lesson;
import domain.Reservation;
import domain.Teacher;

@Service
@Transactional
public class LessonService {

	@Autowired
	private LessonRepository	lessonRepository;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private ReservationService	reservationService;


	public Lesson create() {
		final Lesson lesson = new Lesson();
		final Teacher principal = this.teacherService.findByPrincipal();
		lesson.setTeacher(principal);
		final String ticker = this.generateTicker(principal.getName());
		lesson.setTicker(ticker);
		lesson.setIsDraft(true);

		return lesson;
	}

	public Lesson findOne(final int lessonId) {
		Assert.isTrue(lessonId != 0);
		final Lesson result = this.lessonRepository.findOne(lessonId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Lesson> findAll() {
		Collection<Lesson> res = new ArrayList<>();
		res = this.lessonRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Lesson save(final Lesson lesson) {
		Assert.notNull(lesson);
		final Teacher principal = this.teacherService.findByPrincipal();
		final Lesson result;

		if (lesson.getId() != 0) {
			Assert.isTrue(lesson.getTeacher().equals(principal));
			Assert.isTrue(lesson.getIsDraft(), "No puede modificar una posición que ya no esta en DRAFT MODE.");
		}
		result = this.lessonRepository.save(lesson);
		return result;

	}

	public void delete(final Lesson lesson) {
		Assert.notNull(lesson);
		Assert.isTrue(lesson.getId() != 0);
		final Lesson retrieved = this.findOne(lesson.getId());
		final Teacher principal = this.teacherService.findByPrincipal();
		Assert.isTrue(retrieved.getTeacher().equals(principal));
		final List<Reservation> reservations = (List<Reservation>) this.reservationService.findAllReservationByLesson(lesson.getId());
		Assert.isTrue(reservations.isEmpty(), "No puede borrar una lesson que tenga reservations.");
		this.lessonRepository.delete(retrieved);
	}

	/* ========================= OTHER METHODS =========================== */

	public Collection<Lesson> findAllByPrincipal() {
		Collection<Lesson> res = new ArrayList<>();
		final Teacher principal = this.teacherService.findByPrincipal();
		res = this.lessonRepository.findAllLessonByTeacherId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Lesson toFinalMode(final int lessonId) {
		final Lesson lesson = this.findOne(lessonId);
		Assert.notNull(lesson);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Lesson result;
		Assert.isTrue(lesson.getTeacher().equals(teacher), "No puede ejecutar ninguna acción sobre una lesson que no le pertenece.");
		Assert.isTrue(lesson.getIsDraft(), "Para poner una position en FINAL MODE debe de estar anteriormente en DRAFT MODE.");
		lesson.setIsDraft(false);
		result = this.lessonRepository.save(lesson);
		return result;
	}

	public Collection<Lesson> findPositions(final String keyword, final Double minSalary, final Double maxSalary, final Date minDeadline, final Date maxDeadline) {
		final Collection<Lesson> res = this.lessonRepository.findLessons(keyword, minSalary, maxSalary, minDeadline, maxDeadline);
		Assert.notNull(res);
		return res;
	}

	//TODO: Revisar ticker
	private String generateTicker(final String companyName) {
		String res = "";
		final Integer n1 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n2 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n3 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n4 = (int) Math.floor(Math.random() * 9 + 1);
		final String word = companyName.substring(0, 4).toUpperCase();
		final String ticker = word + '-' + n1 + n2 + n3 + n4;
		res = ticker;

		final Collection<Lesson> less = this.lessonRepository.getLessonWithTicker(ticker);
		if (!less.isEmpty())
			this.generateTicker(companyName);
		return res;
	}
}
