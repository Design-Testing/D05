
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.ReservationRepository;
import security.Authority;
import domain.Actor;
import domain.Reservation;
import domain.Student;
import domain.Teacher;
import domain.TimePeriod;

@Service
@Transactional
public class ReservationService {

	@Autowired
	private ReservationRepository	reservationRepository;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private StudentService			studentService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private LessonService			lessonService;

	@Autowired
	private ExamService				examService;

	@Autowired
	private TimePeriodService		timePeriodService;

	@Autowired
	private Validator				validator;

	@Autowired
	private MessageService			messageService;


	public Reservation create() {
		final Reservation reservation = new Reservation();
		final Student principal = this.studentService.findByPrincipal();
		reservation.setStudent(principal);
		reservation.setStatus("PENDING");
		final Date moment = new Date(System.currentTimeMillis() - 1);
		reservation.setMoment(moment);
		return reservation;
	}

	public Reservation findOne(final int reservationId) {
		Assert.isTrue(reservationId != 0);
		final Reservation result = this.reservationRepository.findOne(reservationId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Reservation> findAll() {
		Collection<Reservation> res = new ArrayList<>();
		res = this.reservationRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Collection<Reservation> findAllReservationByLesson(final int lessonId) {
		final Collection<Reservation> res = this.reservationRepository.findAllReservationByLesson(lessonId);
		return res;
	}

	public Collection<Reservation> findAllReservationByStudent(final int studentId) {
		final Collection<Reservation> res = this.reservationRepository.findAllReservationByStudent(studentId);
		return res;
	}

	public Collection<Reservation> findAllReservationByTeacher(final int teacherId) {
		final Collection<Reservation> res = this.reservationRepository.findAllReservationByTeacher(teacherId);
		return res;
	}

	public Collection<Reservation> findAllByStudent() {
		Collection<Reservation> res = new ArrayList<>();
		final Student principal = this.studentService.findByPrincipal();
		res = this.reservationRepository.findAllReservationByStudent(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Reservation> findAllByTeacher() {
		Collection<Reservation> res = new ArrayList<>();
		final Teacher principal = this.teacherService.findByPrincipal();
		res = this.reservationRepository.findAllReservationByTeacher(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Reservation save(final Reservation reservation) {
		Assert.notNull(reservation);
		final Reservation result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.lessonService.findAllLessonsByTeacher(principal.getUserAccount().getId()).contains(reservation.getLesson()) || reservation.getStudent().equals(principal),
			"No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		if (reservation.getId() == 0) {
			Assert.isTrue(this.actorService.checkAuthority(principal, Authority.STUDENT), "Las reservas solo pueden ser creadas por estudiantes");
			Assert.notNull(reservation.getHoursWeek(), "Debe indicar las horas semanales que desea.");
			Assert.isTrue(reservation.getStatus().equals("PENDING"));
			reservation.setCost(reservation.getHoursWeek() * reservation.getLesson().getPrice());
		} else if (reservation.getStatus().equals("FINAL"))
			Assert.notNull(reservation.getCreditCard());
		else if (reservation.getStatus().equals("REVIEWING"))
			Assert.notNull(reservation.getExplanation(), "Debe indicar una explicacion.");
		else if (reservation.getStatus().equals("ACCEPTED"))
			reservation.setExplanation("");
		else if (reservation.getStatus().equals("REJECTED"))
			Assert.notNull(reservation.getExplanation(), "Debe indicar una explicacion.");
		result = this.reservationRepository.save(reservation);
		return result;

	}

	public void delete(final Reservation reservation) {
		Assert.notNull(reservation);
		Assert.isTrue(reservation.getId() != 0);
		Assert.isTrue(reservation.getStatus().equals("FINAL"));
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.belongsToTeacher(principal, reservation) || reservation.getStudent().equals(principal), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		final Collection<TimePeriod> periods = this.timePeriodService.findByReservation(reservation.getId());
		final Reservation retrieved = this.findOne(reservation.getId());
		final Teacher teacher = retrieved.getLesson().getTeacher();
		this.timePeriodService.setScheduleFalse(periods, teacher);
		this.timePeriodService.deleteInBatch(periods);
		this.examService.deleteInBatch(this.examService.findAllExamsByReservation(reservation.getId()));
		this.reservationRepository.delete(retrieved);
		this.messageService.notifyReservationDeleted(retrieved);
	}
	/* ========================= OTHER METHODS =========================== */

	public Boolean belongsToTeacher(final Actor principal, final Reservation reservation) {
		return this.lessonService.findAllLessonsByTeacher(principal.getUserAccount().getId()).contains(reservation.getLesson());
	}

	public Reservation toReviewingMode(final Reservation reservation) {
		Assert.notNull(reservation);
		final Student student = this.studentService.findByPrincipal();
		final Reservation result;
		Assert.isTrue(reservation.getStudent().equals(student), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("ACCEPTED"), "Para poner una Reserva en Pendiente debe de estar anteriormente Aceptada.");
		reservation.setStatus("REVIEWING");
		result = this.save(reservation);
		return reservation;
	}

	public Reservation toAcceptedMode(final Reservation reservation) {
		Assert.notNull(reservation);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Reservation result;
		final Collection<TimePeriod> periods = this.timePeriodService.findByReservation(reservation.getId());
		Assert.isTrue(this.lessonService.findAllLessonsByTeacher(teacher.getUserAccount().getId()).contains(reservation.getLesson()), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("PENDING") || reservation.getStatus().equals("REVIEWING"), "Esta Reserva no puede ser aceptada.");
		Assert.isTrue(periods.size() == reservation.getHoursWeek(), "Una reserva no puede ser aceptada si no tiene los mismo timePeriods que hoursWeek solicitadas. ");
		reservation.setStatus("ACCEPTED");
		result = this.save(reservation);
		return result;
	}

	public Reservation toRejectedMode(final Reservation reservation) {
		Assert.notNull(reservation);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Reservation result;
		Assert.isTrue(this.lessonService.findAllLessonsByTeacher(teacher.getUserAccount().getId()).contains(reservation.getLesson()), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("PENDING") || reservation.getStatus().equals("ACCEPTED") || reservation.getStatus().equals("REVIEWING"), "Para poner una Reserva en Rechaza debe de estar anteriormente Aceptada o Pendiente.");
		reservation.setStatus("REJECTED");
		result = this.save(reservation);
		return reservation;
	}

	public Reservation toFinalMode(final int reservationId) {
		final Reservation reservation = this.findOne(reservationId);
		Assert.notNull(reservation);
		final Student student = this.studentService.findByPrincipal();
		final Reservation result;
		Assert.isTrue(reservation.getStudent().equals(student), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("ACCEPTED"), "Para poner una Reserva en Final debe de estar anteriormente Aceptada.");
		reservation.setStatus("FINAL");
		result = this.save(reservation);
		return result;
	}

	public Collection<Reservation> findAllByCreditCard(final int creditCardId) {
		Assert.isTrue(creditCardId != 0);
		final Collection<Reservation> res = this.reservationRepository.findAllByCreditCard(creditCardId);
		Assert.notNull(res);
		return res;
	}

	public Double findPendingReservationRatio() {
		Double res = this.reservationRepository.findPendingReservationRatio();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double findAcceptedReservationRatio() {
		Double res = this.reservationRepository.findAcceptedReservationRatio();
		if (res == null)
			res = 0.0;
		return res;
	}

	public Double findRejectedReservationRatio() {
		Double res = this.reservationRepository.findRejectedReservationRatio();
		if (res == null)
			res = 0.0;
		return res;
	}

	public Double findFinalOverRejectedReservationRatio() {
		Double res = this.reservationRepository.findFinalOverRejectedReservationRatio();
		if (res == null)
			res = 0.0;
		return res;
	}

	public Double[] getStatisticsOfPassExams() {
		final Double[] res = this.reservationRepository.getStatisticsOfPassExams();
		Assert.notNull(res);
		return res;
	}

	public Double[] getStatisticsOfWeeklyCost() {
		final Double[] res = this.reservationRepository.getStatisticsOfWeeklyCost();
		Assert.notNull(res);
		return res;
	}

}
