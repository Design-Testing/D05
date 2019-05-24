
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

import services.ReservationService;
import services.StudentService;
import controllers.AbstractController;
import domain.Reservation;
import domain.Student;

@Controller
@RequestMapping("/reservation/student")
public class ReservationStudentController extends AbstractController {

	@Autowired
	private ReservationService	reservationService;

	@Autowired
	private StudentService		studentService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Reservation reservation = this.reservationService.create();
		result = this.createEditModelAndView(reservation);
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

		result = new ModelAndView("reservation/display");
		result.addObject("reservation", reservation);
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
		result.addObject("reservatins", reservations);
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

	@RequestMapping(value = "/reviewing", method = RequestMethod.GET)
	public ModelAndView reviewingMode(@RequestParam final int reservationId) {
		ModelAndView result;
		final Reservation reservation = this.reservationService.findOne(reservationId);

		if (reservation == null) {
			result = this.myReservations();
			result.addObject("msg", "reservations.reviewing.error");
		} else
			try {
				this.reservationService.toReviewingMode(reservationId);
				result = this.myReservations();
			} catch (final Throwable oops) {
				String errormsg = "reservation.reviewing.error";
				result = this.myReservations();
				if (!reservation.getStatus().equals("ACCEPTED"))
					errormsg = "reservation.reviewing.no.accepted";
				result.addObject("msg", errormsg);
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
			result = this.createEditModelAndView(reservation);
		else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Reservation reservation, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(reservation);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.reservationService.save(reservation, binding);
				result = this.myReservations();
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(reservation, "commit.reservation.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(reservation, "commit.reservation.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int reservationId) {
		final Reservation reservation = this.reservationService.findOne(reservationId);
		this.reservationService.delete(reservation);
		return this.myReservations();

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Reservation reservation) {
		ModelAndView result;
		result = this.createEditModelAndView(reservation, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Reservation reservation, final String messageCode) {
		Assert.notNull(reservation);
		final ModelAndView result;

		result = new ModelAndView("reservation/edit");
		result.addObject("reservation", reservation);
		result.addObject("message", messageCode);

		return result;
	}

}
