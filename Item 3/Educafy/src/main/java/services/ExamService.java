
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ExamRepository;
import domain.Exam;
import domain.Question;
import domain.Reservation;
import domain.Student;

@Service
@Transactional
public class ExamService {

	@Autowired
	private ExamRepository		examRepository;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ReservationService	reservationService;

	@Autowired
	private QuestionService		questionService;


	public Exam create() {
		final Exam exam = new Exam();
		exam.setQuestions(new ArrayList<Question>());
		exam.setStatus("PENDING");
		return exam;
	}

	public Exam findOne(final int examId) {
		Assert.isTrue(examId != 0);
		final Exam result = this.examRepository.findOne(examId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Exam> findAll() {
		Collection<Exam> res = new ArrayList<>();
		res = this.examRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Collection<Exam> findAllExamsByStudent(final int studentId) {
		Collection<Exam> res = new ArrayList<>();
		res = this.examRepository.findAllExamsByStudent(studentId);
		Assert.notNull(res);
		return res;
	}

	public Exam createSave(final Exam exam, final Reservation reservation) {
		this.actorService.findByPrincipal();
		Assert.notNull(exam);
		final Exam result;
		Assert.isTrue(exam.getId() == 0);
		result = this.examRepository.save(exam);
		final Collection<Exam> exams = reservation.getExams();
		exams.add(result);
		reservation.setExams(exams);
		this.reservationService.save(reservation);
		return result;

	}
	public Exam save(final Exam exam) {
		Assert.notNull(exam);
		final Exam result;
		Assert.isTrue(exam.getId() != 0);
		result = this.examRepository.save(exam);
		return result;

	}

	public void delete(final Exam exam) {
		Assert.notNull(exam);
		Assert.isTrue(exam.getId() != 0);
		final Exam retrieved = this.findOne(exam.getId());
		this.teacherService.findByPrincipal();
		this.examRepository.delete(retrieved);
	}
	/* ========================= OTHER METHODS =========================== */

	public Exam toInprogressMode(final int examId) {
		final Exam exam = this.findOne(examId);
		Assert.notNull(exam);
		final Student student = this.studentService.findByPrincipal();
		final Exam result;
		Assert.isTrue(this.findAllExamsByStudent(student.getUserAccount().getId()).contains(exam), "No puede ejecutar ninguna acción sobre una exam que no le pertenece.");
		Assert.isTrue(exam.getStatus().equals("PENDING"), "Para poner una position en FINAL MODE debe de estar anteriormente en DRAFT MODE.");
		exam.setStatus("INPROGRESS");
		result = this.save(exam);
		return result;
	}

	public Exam toSubmittedMode(final int examId) {
		final Exam exam = this.findOne(examId);
		Assert.notNull(exam);
		final Student student = this.studentService.findByPrincipal();
		final Exam result;
		Assert.isTrue(this.findAllExamsByStudent(student.getUserAccount().getId()).contains(exam), "No puede ejecutar ninguna acción sobre una exam que no le pertenece.");
		Assert.isTrue(exam.getStatus().equals("INPROGRESS"), "Para poner una position en FINAL MODE debe de estar anteriormente en DRAFT MODE.");
		exam.setStatus("SUBMITTED");
		result = this.save(exam);
		return result;
	}

	public Exam toEvaluatedMode(final int examId) {
		final Exam exam = this.findOne(examId);
		Assert.notNull(exam);
		Assert.notNull(exam.getScore());
		this.teacherService.findByPrincipal();
		final Exam result;
		Assert.isTrue(exam.getStatus().equals("SUBMITTED"), "Para poner una position en FINAL MODE debe de estar anteriormente en DRAFT MODE.");
		exam.setStatus("EVALUATED");
		result = this.save(exam);
		return result;
	}

}
