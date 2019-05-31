
package controllers;

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
import controllers.student.ReservationStudentController;
import controllers.teacher.ReservationTeacherController;
import domain.Exam;
import domain.Reservation;

@Controller
@RequestMapping("/exam")
public class ExamController extends AbstractController {

	@Autowired
	private ReservationService				reservationService;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private ExamService						examService;

	@Autowired
	private QuestionService					questionService;

	@Autowired
	private ReservationStudentController	reservationStudentController;

	@Autowired
	private ReservationTeacherController	reservationTeacherController;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int reservationId) {
		ModelAndView result;
		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Exam exam = this.examService.create();
		result = this.createEditModelAndView(exam);
		result.addObject("reservation", reservation);
		return result;
	}

	// CREATESAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "create")
	public ModelAndView createSave(@Valid final Exam exam, final BindingResult binding, @RequestParam final int reservationId) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(exam);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.examService.save(exam, reservationId);
				result = this.reservationTeacherController.display(reservationId);
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(exam, "commit.exam.create.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(exam, "commit.exam.create.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int examId) {
		final ModelAndView result;
		Exam exam;
		exam = this.examService.findOne(examId);
		result = new ModelAndView("exam/display");
		result.addObject("exam", exam);
		result.addObject("questions", exam.getQuestions());
		result.addObject("requestURI", "exam/display.do");
		result.addObject("lang", this.lang);

		return result;
	}

	// TO SUBMITTED --------------------------------------------------------

	@RequestMapping(value = "/submitted", method = RequestMethod.GET)
	public ModelAndView submitted(@RequestParam final int examId) {
		ModelAndView result;
		final Exam exam = this.examService.findOne(examId);

		if (exam == null) {
			result = this.reservationStudentController.myReservations();
			result.addObject("msg", "exam.submitted.error");
		} else
			try {
				this.examService.toSubmittedMode(examId);
				result = this.reservationStudentController.display(exam.getReservation().getId());
			} catch (final Throwable oops) {
				String errormsg = "exam.submitted.error";
				result = this.reservationStudentController.myReservations();
				if (!(exam.getStatus().equals("INPROGRESS")))
					errormsg = "exam.submitted.error";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// TO INPROGRESS --------------------------------------------------------

	@RequestMapping(value = "/inprogress", method = RequestMethod.GET)
	public ModelAndView inProgress(@RequestParam final int examId) {
		ModelAndView result;
		final Exam exam = this.examService.findOne(examId);

		if (exam == null) {
			result = this.reservationTeacherController.myReservations();
			result.addObject("msg", "exam.inprogress.error");
		} else
			try {
				this.examService.toInprogressMode(examId);
				result = this.reservationTeacherController.display(exam.getReservation().getId());
			} catch (final Throwable oops) {
				String errormsg = "exam.inprogress.error";
				result = this.reservationTeacherController.myReservations();
				if (!(exam.getStatus().equals("PENDING")))
					errormsg = "exam.inprogress.error";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// TO EVALUATED --------------------------------------------------------

	@RequestMapping(value = "/evaluated", method = RequestMethod.POST, params = "evaluate")
	public ModelAndView evaluatedMode(@Valid final Exam exam, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(exam);
			result.addObject("msg", "exam.evaluated.error");
		} else
			try {
				this.examService.toEvaluatedMode(exam);
				result = this.reservationTeacherController.display(exam.getReservation().getId());
			} catch (final Throwable oops) {
				final String errormsg = "exam.evaluated.error";
				result = this.createEditModelAndView(exam, errormsg);
			}

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int examId) {
		ModelAndView result;
		Exam exam;
		exam = this.examService.findOne(examId);
		final Reservation reservation = exam.getReservation();
		if (exam.getStatus().equals("PENDING") || exam.getStatus().equals("SUBMITTED")) {
			result = this.createEditModelAndView(exam);
			result.addObject("reservation", reservation);
		} else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Exam exam, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(exam);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.examService.save(exam, exam.getReservation().getId());
				result = this.display(exam.getId());
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(exam, "commit.exam.save.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(exam, "commit.exam.save.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int examId) {
		ModelAndView result;
		final Exam exam = this.examService.findOne(examId);
		final Reservation reservation = exam.getReservation();
		try {
			this.examService.delete(exam);
			result = this.reservationTeacherController.display(reservation.getId());
		} catch (final Throwable oops) {
			String errormsg = "exam.delete.error";
			result = this.reservationTeacherController.myReservations();
			if (!exam.getStatus().equals("PENDING"))
				errormsg = "exam.delete.no.pending";
			result.addObject("msg", errormsg);
		}

		return result;

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Exam exam) {
		ModelAndView result;
		result = this.createEditModelAndView(exam, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Exam exam, final String messageCode) {
		Assert.notNull(exam);
		final ModelAndView result;

		result = new ModelAndView("exam/edit1");
		result.addObject("exam", exam);
		result.addObject("message", messageCode);
		return result;
	}

}
