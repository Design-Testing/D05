
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AssesmentRepository;
import domain.Assesment;
import domain.Lesson;
import domain.Student;
import domain.Teacher;

@Service
@Transactional
public class AssesmentService {

	@Autowired
	private AssesmentRepository	assesmentRepository;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private LessonService		lessonService;


	public Assesment create() {
		final Assesment assesment = new Assesment();
		return assesment;
	}

	public Assesment findOne(final int assesmentId) {
		Assert.isTrue(assesmentId != 0);
		final Assesment result = this.assesmentRepository.findOne(assesmentId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Assesment> findAll() {
		Collection<Assesment> res = new ArrayList<>();
		res = this.assesmentRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Assesment save(final Assesment assesment, final int lessonId) {
		Assert.notNull(assesment);
		Assert.isTrue(lessonId != 0);
		final Student principal = this.studentService.findByPrincipal();
		final Assesment result;

		if (assesment.getId() == 0) {
			assesment.setStudent(principal);
			final Lesson lesson = this.lessonService.findOne(lessonId);
			assesment.setLesson(lesson);
		}
		result = this.assesmentRepository.save(assesment);
		return result;

	}

	/* ========================= OTHER METHODS =========================== */

	public Collection<Assesment> findAllByPrincipal() {
		Collection<Assesment> res = new ArrayList<>();
		final Student principal = this.studentService.findByPrincipal();
		res = this.assesmentRepository.findAllAssesmentByStudentId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Lesson toFinalMode(final int lessonId) {
		final Lesson lesson = this.findOne(lessonId);
		Assert.notNull(lesson);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Lesson result;
		Assert.isTrue(lesson.getTeacher().equals(teacher), "No puede ejecutar ninguna acción sobre una lesson que no le pertenece.");
		Assert.isTrue(lesson.isDraft(), "Para poner una position en FINAL MODE debe de estar anteriormente en DRAFT MODE.");
		lesson.setDraft(false);
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
