
package controllers.student;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LessonService;
import services.ReservationService;
import services.StudentService;
import controllers.AbstractController;
import domain.Reservation;
import domain.Student;

@Controller
@RequestMapping("/reservation/student")
public class ReservationStudentController extends AbstractController {

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private ReservationService	reservationService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Reservation reservation;
		reservation = this.reservationService.create();
		result = this.createEditModelAndView(reservation);
		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reservationId) {
		ModelAndView result;
		Reservation reservation;
		reservation = this.reservationService.findOne(reservationId);
		Assert.notNull(reservation);
		result = this.createEditModelAndView(reservation);
		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Reservation reservation, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(reservation);
		else
			try {
				this.reservationService.save(reservation);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(reservation, "reservation.commit.error");
			}
		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Reservation> reservations;
		final Student principal = this.studentService.findByPrincipal();
		reservations = this.reservationService.findAllReservationByStudent(principal.getId());

		result = new ModelAndView("reservation/list");
		result.addObject("reservations", reservations);
		result.addObject("lang", this.lang);
		result.addObject("rol", "student");
		result.addObject("requetURI", "reservation/student/list.do");

		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Reservation reservation, final BindingResult binding) {
		ModelAndView result;

		try {
			this.reservationService.delete(reservation);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(reservation, "reservation.commit.error");
		}
		return result;
	}

	//ANCILLARY METHOD

	protected ModelAndView createEditModelAndView(final Reservation reservation) {
		ModelAndView result;
		result = this.createEditModelAndView(reservation, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Reservation reservation, final String messageCode) {
		final ModelAndView result;
		result = new ModelAndView("reservation/edit");
		result.addObject("reservation", reservation);
		result.addObject("message", messageCode);
		return result;
	}
}
