
package controllers.student;

import javax.servlet.http.HttpServletRequest;
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

import services.ActorService;
import services.ExamService;
import services.QuestionService;
import services.ReservationService;
import controllers.AbstractController;
import domain.Exam;
import domain.Reservation;

@Controller
@RequestMapping("/exam/student")
public class ExamStudentController extends AbstractController {

	@Autowired
	private ReservationService				reservationService;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private ExamService						examService;

	@Autowired
	private ReservationStudentController	reservationStudentController;

	@Autowired
	private QuestionService					questionService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int examId, @RequestParam final int reservationId) {
		ModelAndView result;
		Exam exam;
		Reservation reservation;

		exam = this.examService.findOne(examId);
		reservation = this.reservationService.findOne(reservationId);

		if (reservation.getStatus().equals("FINAL") && exam.getStatus().equals("INPROGRESS"))
			result = this.createEditModelAndView(exam, reservationId);
		else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Exam exam, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;

		String paramReservationId;
		Integer reservationId;

		paramReservationId = request.getParameter("reservationId");
		reservationId = paramReservationId.isEmpty() ? null : Integer.parseInt(paramReservationId);

		final Reservation reservation = this.reservationService.findOne(reservationId);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(exam, reservationId);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.examService.save(exam, reservation.getId());
				result = this.reservationStudentController.display(reservationId);
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(exam, reservationId, "commit.exam.create.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(exam, reservationId, "commit.exam.create.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// TO INPROGRESS --------------------------------------------------------

	@RequestMapping(value = "/inProgress", method = RequestMethod.GET)
	public ModelAndView inProgress(@RequestParam final int examId) {
		ModelAndView result;
		final Exam exam = this.examService.findOne(examId);

		if (exam == null) {
			result = this.reservationStudentController.myReservations();
			result.addObject("msg", "exam.submitted.error");
		} else
			try {
				this.examService.toInprogressMode(examId);
				result = this.reservationStudentController.myReservations();
			} catch (final Throwable oops) {
				String errormsg = "exam.inProgress.error";
				result = this.reservationStudentController.myReservations();
				if (!(exam.getStatus().equals("PENDING")))
					errormsg = "exam.inProgress.error";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Exam exam, final int reservationId) {
		ModelAndView result;
		result = this.createEditModelAndView(exam, reservationId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Exam exam, final int reservationId, final String messageCode) {
		Assert.notNull(exam);
		final ModelAndView result;

		result = new ModelAndView("exam/edit1");
		result.addObject("exam", exam);
		result.addObject("message", messageCode);
		return result;
	}

}
