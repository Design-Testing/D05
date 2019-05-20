
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AssesmentRepository;
import domain.Assesment;
import domain.Lesson;
import domain.Student;

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

}
