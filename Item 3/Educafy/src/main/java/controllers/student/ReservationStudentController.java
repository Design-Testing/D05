
package controllers.student;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repositories.CreditCardRepository;
import services.ExamService;
import services.LessonService;
import services.ReservationService;
import services.StudentService;
import services.TimePeriodService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Exam;
import domain.Lesson;
import domain.Reservation;
import domain.Student;
import domain.TimePeriod;

@Controller
@RequestMapping("/reservation/student")
public class ReservationStudentController extends AbstractController {

	@Autowired
	private ReservationService		reservationService;

	@Autowired
	private LessonService			lessonService;

	@Autowired
	private ExamService				examService;

	@Autowired
	private StudentService			studentService;

	@Autowired
	private CreditCardRepository	creditCardRepository;

	@Autowired
	private TimePeriodService		timePeriodService;

	final String					lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int lessonId) {
		ModelAndView result;
		final Student principal = this.studentService.findByPrincipal();
		final Lesson lesson = this.lessonService.findOne(lessonId);
		final Reservation reservation = this.reservationService.create();
		final Collection<CreditCard> myCards = this.creditCardRepository.findAllByActorUserId(principal.getUserAccount().getId());
		reservation.setLesson(lesson);
		result = this.createEditModelAndView1(reservation);
		result.addObject("myCards", myCards);
		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int reservationId) {
		final ModelAndView result;
		final Reservation reservation;
		final Student student;

		reservation = this.reservationService.findOne(reservationId);
		student = this.studentService.findByPrincipal();
		final Collection<TimePeriod> periods = this.timePeriodService.findByReservation(reservationId);
		final Collection<Exam> exams = this.examService.findAllExamsByReservation(reservationId);
		result = new ModelAndView("reservation/display");
		result.addObject("reservation", reservation);
		result.addObject("periods", periods);
		result.addObject("exams", exams);
		result.addObject("requestURI", "reservation/student/display.do");
		result.addObject("student", student);
		result.addObject("studentId", reservation.getStudent().getId());
		result.addObject("rol", "student");
		result.addObject("lang", this.lang);

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myReservations", method = RequestMethod.GET)
	public ModelAndView myReservations() {
		final ModelAndView result;
		final Collection<Reservation> reservations;

		reservations = this.reservationService.findAllByStudent();

		result = new ModelAndView("reservation/list");
		result.addObject("reservations", reservations);
		result.addObject("lang", this.lang);
		result.addObject("rol", "student");
		result.addObject("requestURI", "reservation/student/myReservations.do");
		result.addObject("principalID", this.studentService.findByPrincipal().getId());

		return result;
	}

	// TO FINAL --------------------------------------------------------

	@RequestMapping(value = "/final", method = RequestMethod.GET)
	public ModelAndView finalMode(@RequestParam final int reservationId) {
		ModelAndView result;
		final Reservation reservation = this.reservationService.findOne(reservationId);

		if (reservation == null) {
			result = this.myReservations();
			result.addObject("msg", "reservations.final.error");
		} else
			try {
				this.reservationService.toFinalMode(reservationId);
				result = this.myReservations();
			} catch (final Throwable oops) {
				String errormsg = "reservation.final.error";
				result = this.myReservations();
				if (!reservation.getStatus().equals("ACCEPTED"))
					errormsg = "reservation.final.no.accepted";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// TO REVIEWING --------------------------------------------------------

	@RequestMapping(value = "/reviewing", method = RequestMethod.POST, params = "review")
	public ModelAndView reviewingMode(@Valid Reservation reservation, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView2(reservation);
			result.addObject("msg", "reservations.reviewing.error");
		} else
			try {
				reservation = this.reservationService.toReviewingMode(reservation.getId());
				result = new ModelAndView("redirect:myReservations.do");
			} catch (final Throwable oops) {
				final String errormsg = "reservation.reviewing.error";
				result = this.createEditModelAndView2(reservation, errormsg);
			}

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reservationId) {
		ModelAndView result;
		Reservation reservation;

		reservation = this.reservationService.findOne(reservationId);

		final Student student = this.studentService.findByPrincipal();

		if ((!reservation.getStatus().equals("FINAL") && reservation.getStudent().equals(student)))
			result = this.createEditModelAndView2(reservation);
		else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Reservation reservation, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView1(reservation);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.reservationService.save(reservation);
				result = this.myReservations();
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView1(reservation, "commit.reservation.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView1(reservation, "commit.reservation.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int reservationId) {
		ModelAndView result;
		final Reservation reservation = this.reservationService.findOne(reservationId);
		try {
			this.reservationService.delete(reservation);
			result = this.myReservations();
		} catch (final Throwable oops) {
			String errormsg = "reservation.delete.error";
			result = this.myReservations();
			if (!reservation.getStatus().equals("FINAL"))
				errormsg = "reservation.delete.no.final";
			result.addObject("msg", errormsg);
		}

		return result;

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView1(final Reservation reservation) {
		ModelAndView result;
		result = this.createEditModelAndView1(reservation, null);
		return result;
	}

	protected ModelAndView createEditModelAndView1(final Reservation reservation, final String messageCode) {
		Assert.notNull(reservation);
		final ModelAndView result;

		result = new ModelAndView("reservation/edit1");
		result.addObject("reservation", reservation);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Reservation reservation) {
		ModelAndView result;
		result = this.createEditModelAndView2(reservation, null);
		return result;
	}

	protected ModelAndView createEditModelAndView2(final Reservation reservation, final String messageCode) {
		Assert.notNull(reservation);
		final ModelAndView result;

		result = new ModelAndView("reservation/edit2");
		result.addObject("reservation", reservation);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "reservation/student/reviewing.do");

		return result;
	}

}
