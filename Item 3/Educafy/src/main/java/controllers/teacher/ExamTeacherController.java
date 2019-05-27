
package controllers.teacher;

import java.util.Collection;

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
import controllers.student.ReservationStudentController;
import domain.Actor;
import domain.Exam;
import domain.Question;
import domain.Reservation;

@Controller
@RequestMapping("/exam/teacher")
public class ExamTeacherController extends AbstractController {

	@Autowired
	private ReservationService				reservationService;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private ExamService						examService;

	@Autowired
	private ReservationStudentController	reservationStudentController;

	@Autowired
	private ReservationTeacherController	reservationTeacherController;

	@Autowired
	private QuestionService					questionService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int reservationId) {
		ModelAndView result;
		final Reservation reservation = this.reservationService.findOne(reservationId);
		final Exam exam = this.examService.create();
		result = this.createEditModelAndView(exam, reservationId);
		result.addObject("reservation", reservation);
		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int examId, @RequestParam final int reservationId) {
		ModelAndView result;
		Exam exam;
		Reservation reservation;

		exam = this.examService.findOne(examId);
		reservation = this.reservationService.findOne(reservationId);

		if (reservation.getStatus().equals("FINAL") && (exam.getStatus().equals("PENDING") || exam.getStatus().equals("SUBMITTED")))
			result = this.createEditModelAndView(exam, reservationId);
		else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createSave(@Valid final Exam exam, final BindingResult binding, final HttpServletRequest request) {
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
				result = this.display(exam.getId());
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(exam, reservationId, "commit.exam.create.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(exam, reservationId, "commit.exam.create.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int examId) {
		final ModelAndView result;
		Exam exam;
		final Actor principal;
		final Collection<Question> questions;

		exam = this.examService.findOne(examId);
		principal = this.actorService.findByPrincipal();
		questions = this.questionService.findQuestionsByExam(examId);

		if (this.actorService.checkAuthority(principal, "STUDENT"))
			exam = this.examService.toInprogressMode(examId);

		result = new ModelAndView("exam/display");
		result.addObject("exam", exam);
		result.addObject("questions", questions);
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
				result = this.reservationStudentController.myReservations();
			} catch (final Throwable oops) {
				String errormsg = "exam.submitted.error";
				result = this.reservationStudentController.myReservations();
				if (!(exam.getStatus().equals("INPROGRESS")))
					errormsg = "exam.submitted.error";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// TO EVALUATED --------------------------------------------------------

	@RequestMapping(value = "/evaluated", method = RequestMethod.POST, params = "evaluate")
	public ModelAndView evaluatedMode(@Valid final Exam exam, final BindingResult binding, final int reservationId) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(exam, reservationId);
			result.addObject("msg", "exam.evaluated.error");
		} else
			try {
				this.examService.toEvaluatedMode(exam.getId());
				result = this.reservationTeacherController.myReservations();
			} catch (final Throwable oops) {
				final String errormsg = "exam.evaluated.error";
				result = this.createEditModelAndView(exam, reservationId, errormsg);
			}

		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int examId) {
		ModelAndView result;
		final Exam exam = this.examService.findOne(examId);
		try {
			this.examService.delete(exam);
			result = this.reservationTeacherController.myReservations();
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

	protected ModelAndView createEditModelAndView(final Exam exam, final int reservationId) {
		ModelAndView result;
		result = this.createEditModelAndView(exam, reservationId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Exam exam, final int reservationId, final String messageCode) {
		Assert.notNull(exam);
		final ModelAndView result;

		final Reservation reservation = this.reservationService.findOne(reservationId);

		result = new ModelAndView("exam/edit1");
		result.addObject("exam", exam);
		result.addObject("reservation", reservation);
		result.addObject("message", messageCode);
		return result;
	}

}
