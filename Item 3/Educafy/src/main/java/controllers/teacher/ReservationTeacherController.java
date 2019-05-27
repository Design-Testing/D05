
package controllers.teacher;

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
import services.TeacherService;
import services.TimePeriodService;
import controllers.AbstractController;
import domain.Reservation;
import domain.Teacher;
import domain.TimePeriod;

@Controller
@RequestMapping("/reservation/teacher")
public class ReservationTeacherController extends AbstractController {

	@Autowired
	private ReservationService	reservationService;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private TimePeriodService	timePeriodService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int reservationId) {
		final ModelAndView result;
		final Reservation reservation;
		final Teacher teacher;

		reservation = this.reservationService.findOne(reservationId);
		teacher = this.teacherService.findByPrincipal();
		final Collection<TimePeriod> periods = this.timePeriodService.findByReservation(reservationId);

		result = new ModelAndView("reservation/display");
		result.addObject("reservation", reservation);
		result.addObject("periods", periods);
		result.addObject("requestURI", "reservation/teacher/display.do");
		result.addObject("studentId", reservation.getStudent().getId());
		result.addObject("rol", "teacher");
		result.addObject("lang", this.lang);

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myReservations", method = RequestMethod.GET)
	public ModelAndView myReservations() {
		final ModelAndView result;
		final Collection<Reservation> reservations;

		reservations = this.reservationService.findAllByTeacher();

		result = new ModelAndView("reservation/list");
		result.addObject("reservations", reservations);
		result.addObject("lang", this.lang);
		result.addObject("rol", "teacher");
		result.addObject("requestURI", "reservation/teacher/myReservations.do");
		result.addObject("principalID", this.teacherService.findByPrincipal().getId());

		return result;
	}

	// TO ACCEPTED --------------------------------------------------------

	@RequestMapping(value = "/accepted", method = RequestMethod.GET)
	public ModelAndView acceptedMode(@RequestParam final int reservationId) {
		ModelAndView result;
		final Reservation reservation = this.reservationService.findOne(reservationId);

		if (reservation == null) {
			result = this.myReservations();
			result.addObject("msg", "reservations.accepted.error");
		} else
			try {
				this.reservationService.toAcceptedMode(reservationId);
				result = this.myReservations();
			} catch (final Throwable oops) {
				String errormsg = "reservation.accepted.error";
				result = this.myReservations();
				if (!(reservation.getStatus().equals("PENDING") || reservation.getStatus().equals("REVIEWING")))
					errormsg = "reservation.accepted.error";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// TO REJECTED --------------------------------------------------------

	@RequestMapping(value = "/rejected", method = RequestMethod.POST, params = "reject")
	public ModelAndView rejectedMode(@Valid Reservation reservation, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(reservation);
			result.addObject("msg", "reservations.rejected.error");
		} else
			try {
				reservation = this.reservationService.toRejectedMode(reservation.getId());
				result = new ModelAndView("redirect:myReservations.do");
			} catch (final Throwable oops) {
				final String errormsg = "reservation.rejected.error";
				result = this.createEditModelAndView(reservation, errormsg);
			}

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reservationId) {
		ModelAndView result;
		Reservation reservation;

		reservation = this.reservationService.findOne(reservationId);

		final Teacher teacher = this.teacherService.findByPrincipal();

		if ((!reservation.getStatus().equals("FINAL") && this.reservationService.belongsToTeacher(teacher, reservation)))
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
				this.reservationService.save(reservation);
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

		result = new ModelAndView("reservation/edit2");
		result.addObject("reservation", reservation);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "reservation/teacher/rejected.do");
		return result;
	}

}
