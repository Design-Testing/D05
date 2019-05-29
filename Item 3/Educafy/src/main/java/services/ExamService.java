
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ExamRepository;
import security.Authority;
import domain.Actor;
import domain.Exam;
import domain.Question;
import domain.Reservation;
import domain.Student;
import domain.Teacher;

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
	private QuestionService		questionService;

	@Autowired
	private ReservationService	reservationService;


	public Exam create() {
		final Exam exam = new Exam();
		exam.setStatus("PENDING");
		exam.setQuestions(new ArrayList<Question>());
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

	public Collection<Exam> findAllExamsByReservation(final int reservationId) {
		Collection<Exam> res = new ArrayList<>();
		res = this.examRepository.findAllExamsByReservation(reservationId);
		return res;
	}

	public Exam save(final Exam exam, final int reservationId) {
		Assert.notNull(exam);
		Assert.isTrue(reservationId != 0);

		final Actor ppal = this.actorService.findByPrincipal();
		final Boolean isStudent = this.actorService.checkAuthority(ppal, Authority.STUDENT);
		final Boolean isTeacher = this.actorService.checkAuthority(ppal, Authority.TEACHER);

		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Exam result;

		if (exam.getId() == 0) {
			if (isTeacher) {
				Assert.isTrue(this.teacherService.findTeacherByReservation(reservationId).equals(ppal), "No puede crear un examen en una reserva que no es suya.");
				exam.setReservation(reservation);

			}
		} else {
			Assert.isTrue(exam.getReservation().equals(reservation), "El examen que quiere actualizar no se corresponde con la reserva indicada.");
			if (isStudent)
				Assert.isTrue(reservation.getStudent().equals(ppal), "No puede actualizar un exam que no pertenece a una de sus reservas.");
			else
				Assert.isTrue(this.teacherService.findTeacherByReservation(reservationId).equals(ppal), "No puede actualizar un examen que no pertenece a una reserva suya.");
		}

		result = this.examRepository.save(exam);
		return result;
	}

	public void delete(final Exam exam) {
		Assert.notNull(exam);
		Assert.isTrue(exam.getId() != 0);
		final Teacher principal = this.teacherService.findByPrincipal();
		Assert.isTrue(this.teacherService.findTeacherByReservation(exam.getReservation().getId()).equals(principal));
		final Exam retrieved = this.findOne(exam.getId());
		this.questionService.deleteInBatch(exam.getQuestions());
		this.examRepository.delete(retrieved);
	}
	/* ========================= OTHER METHODS =========================== */

	public Exam toInprogressMode(final int examId) {
		final Exam exam = this.findOne(examId);
		Assert.notNull(exam);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Exam result;
		final Reservation reservation = exam.getReservation();
		Assert.isTrue(this.teacherService.findTeacherByReservation(reservation.getId()).equals(teacher), "No puede ejecutar ninguna acción sobre un examen que no le pertenece.");
		Assert.isTrue(exam.getStatus().equals("PENDING"), "Para poner el estado de un examen en INPROGRESS debe de estar anteriormente en estado PENDING.");
		exam.setStatus("INPROGRESS");
		result = this.save(exam, exam.getReservation().getId());
		return result;
	}

	public Exam toSubmittedMode(final int examId) {
		final Exam exam = this.findOne(examId);
		Assert.notNull(exam);
		final Student student = this.studentService.findByPrincipal();
		final Exam result;
		Assert.isTrue(exam.getReservation().getStudent().equals(student), "No puede ejecutar ninguna acción sobre una exam que no le pertenece.");
		Assert.isTrue(exam.getStatus().equals("INPROGRESS"), "Para poner el estado de un examen en SUBMITTED debe de estar anteriormente en estado INPROGRESS.");
		exam.setStatus("SUBMITTED");
		result = this.save(exam, exam.getReservation().getId());
		return result;
	}

	public Exam toEvaluatedMode(final Exam exam) {
		Assert.notNull(exam);
		Assert.notNull(exam.getScore());
		this.teacherService.findByPrincipal();
		final Exam result;
		Assert.isTrue(exam.getStatus().equals("SUBMITTED"), "Para poner el estado de un examen en EVALUATED debe de estar anteriormente en estado SUBMITTED.");
		exam.setStatus("EVALUATED");
		result = this.save(exam, exam.getReservation().getId());
		return result;
	}

	public void deleteInBatch(final Collection<Exam> exams) {
		this.examRepository.deleteInBatch(exams);
	}

	public void flush() {
		this.examRepository.flush();
	}

}
