
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReservationRepository;
import domain.Actor;
import domain.Exam;
import domain.Reservation;
import domain.Student;
import domain.Teacher;

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
	private Validator				validator;


	public Reservation create() {
		final Reservation reservation = new Reservation();
		final Student principal = this.studentService.findByPrincipal();
		reservation.setStudent(principal);
		reservation.setExams(new ArrayList<Exam>());
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

	public Reservation save(final Reservation reservation, final BindingResult binding) {
		Assert.notNull(reservation);
		final Reservation result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.lessonService.findAllLessonsByTeacher(principal.getUserAccount().getId()).contains(reservation.getLesson()) || reservation.getStudent().equals(principal),
			"No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		if (reservation.getId() == 0) {
			Assert.isTrue(principal.getUserAccount().getAuthorities().contains("STUDENT"), "Las reservas solo pueden ser creadas por estudiantes");
			Assert.notNull(reservation.getHoursWeek(), "Debe indicar las horas semanales que desea.");
			Assert.isTrue(reservation.getStatus().equals("PENDING"));
			reservation.setCost(reservation.getHoursWeek() * reservation.getLesson().getPrice());
		} else if (reservation.getStatus().equals("FINAL")) {
			Assert.isTrue(!this.findOne(reservation.getId()).getStatus().equals("FINAL"), "No se puede modificar una reserva que se encuentre en modo FINAL");
			Assert.notNull(reservation.getCreditCard());
		} else if (reservation.getStatus().equals("REVIEWING"))
			Assert.notNull(reservation.getExplanation(), "Debe indicar una explicacion.");
		else if (reservation.getStatus().equals("ACCEPTED"))
			reservation.setExplanation("");
		else if (reservation.getStatus().equals("REJECTED"))
			Assert.notNull(reservation.getExplanation(), "Debe indicar una explicacion.");

		this.validator.validate(reservation, binding);
		if (binding.hasErrors())
			throw new ValidationException();
		result = this.reservationRepository.save(reservation);
		return result;

	}

	public void delete(final Reservation reservation) {
		Assert.notNull(reservation);
		Assert.isTrue(reservation.getId() != 0);
		Assert.isTrue(reservation.getStatus().equals("FINAL"));
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.belongsToTeacher(principal, reservation) || reservation.getStudent().equals(principal), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		final Reservation retrieved = this.findOne(reservation.getId());
		this.reservationRepository.delete(retrieved);
	}

	/* ========================= OTHER METHODS =========================== */

	public Boolean belongsToTeacher(final Actor principal, final Reservation reservation) {
		return this.lessonService.findAllLessonsByTeacher(principal.getUserAccount().getId()).contains(reservation.getLesson());
	}

	public Reservation toReviewingMode(final int reservationId) {
		final Reservation reservation = this.findOne(reservationId);
		Assert.notNull(reservation);
		final Student student = this.studentService.findByPrincipal();
		final Reservation result;
		Assert.isTrue(reservation.getStudent().equals(student), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("ACCEPTED"), "Para poner una Reserva en Pendiente debe de estar anteriormente Aceptada.");
		reservation.setStatus("REVIEWING");
		result = this.reservationRepository.save(reservation);
		return result;
	}

	public Reservation toAcceptedMode(final int reservationId) {
		final Reservation reservation = this.findOne(reservationId);
		Assert.notNull(reservation);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Reservation result;
		Assert.isTrue(this.lessonService.findAllLessonsByTeacher(teacher.getUserAccount().getId()).contains(reservation.getLesson()), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("PENDING") || reservation.getStatus().equals("REVIEWING"), "Esta Reserva no puede ser aceptada.");
		reservation.setStatus("ACCEPTED");
		result = this.reservationRepository.save(reservation);
		return result;
	}

	public Reservation toRejectedMode(final int reservationId) {
		final Reservation reservation = this.findOne(reservationId);
		Assert.notNull(reservation);
		final Teacher teacher = this.teacherService.findByPrincipal();
		//final Reservation result;
		Assert.isTrue(this.lessonService.findAllLessonsByTeacher(teacher.getUserAccount().getId()).contains(reservation.getLesson()), "No puede ejecutar ninguna acción sobre una reservation que no le pertenece.");
		Assert.isTrue(reservation.getStatus().equals("PENDING") || reservation.getStatus().equals("ACCEPTED") || reservation.getStatus().equals("REVIEWING"), "Para poner una Reserva en Rechaza debe de estar anteriormente Aceptada o Pendiente.");
		reservation.setStatus("REJECTED");
		//result = this.reservationRepository.save(reservation);
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
		result = this.reservationRepository.save(reservation);
		return result;
	}

	public Collection<Reservation> findAllByCreditCard(final int creditCardId) {
		Assert.isTrue(creditCardId != 0);
		final Collection<Reservation> res = this.reservationRepository.findAllByCreditCard(creditCardId);
		Assert.notNull(res);
		return res;
	}

}
