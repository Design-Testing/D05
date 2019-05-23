
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.LessonRepository;
import domain.Lesson;
import domain.Reservation;
import domain.Teacher;
import forms.LessonForm;

@Service
@Transactional
public class LessonService {

	@Autowired
	private LessonRepository	lessonRepository;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private ReservationService	reservationService;

	@Autowired
	private Validator			validator;


	public Lesson create() {
		final Lesson lesson = new Lesson();
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

		if (lesson.getId() == 0) {
			lesson.setTeacher(principal);
			final String ticker = this.generateTicker(principal.getName());
			lesson.setTicker(ticker);
			lesson.setIsDraft(true);
		} else {
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

	public Collection<Lesson> findAllByTeacher() {
		Collection<Lesson> res = new ArrayList<>();
		final Teacher principal = this.teacherService.findByPrincipal();
		res = this.lessonRepository.findAllLessonByTeacherId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Lesson> findAllLessonsByTeacher(final int teacherId) {
		Collection<Lesson> res = new ArrayList<>();
		res = this.lessonRepository.findAllLessonByTeacherId(teacherId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Lesson> findAllBySubject(final int subjectId) {
		Collection<Lesson> res = new ArrayList<>();
		res = this.lessonRepository.findAllBySubject(subjectId);
		return res;
	}

	public Collection<Lesson> findAllFinalMode() {
		Collection<Lesson> res = new ArrayList<>();
		res = this.lessonRepository.findAllFinalMode();
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

	//TODO: Revisar ticker
	private String generateTicker(final String nameTeacher) {
		String res = "";
		final Integer n1 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n2 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n3 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n4 = (int) Math.floor(Math.random() * 9 + 1);
		final String word = nameTeacher.substring(0, 4).toUpperCase();
		final String ticker = word + '-' + n1 + n2 + n3 + n4;
		res = ticker;

		final Collection<Lesson> less = this.lessonRepository.getLessonWithTicker(ticker);
		if (!less.isEmpty())
			this.generateTicker(nameTeacher);
		return res;
	}

	public Lesson reconstruct(final LessonForm lessonForm, final BindingResult binding) {
		Lesson result;

		if (lessonForm.getId() == 0)
			result = this.create();
		else
			result = this.findOne(lessonForm.getId());

		result.setVersion(lessonForm.getVersion());
		result.setTitle(lessonForm.getTitle());
		result.setDescription(lessonForm.getDescription());
		result.setPrice(lessonForm.getPrice());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

}
