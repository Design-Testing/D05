
package controllers.teacher;

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
import controllers.AbstractController;
import controllers.ExamController;
import domain.Exam;
import domain.Question;

@Controller
@RequestMapping("/question")
public class QuestionTeacherController extends AbstractController {

	@Autowired
	private QuestionService					questionService;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private ExamService						examService;

	@Autowired
	private ExamController					examController;

	@Autowired
	private ReservationTeacherController	reservationTeacherController;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int questionId, @RequestParam final int examId) {
		ModelAndView result;
		final Question question;
		final Exam exam;
		question = this.questionService.findOne(questionId);
		exam = this.examService.findOne(examId);
		if (exam.getStatus().equals("PENDING")) {
			result = this.createEditModelAndView(question, examId);
			result.addObject("exam", exam);
		} else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Question question, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;

		String paramExamId;
		Integer examId;

		paramExamId = request.getParameter("examId");
		examId = paramExamId.isEmpty() ? null : Integer.parseInt(paramExamId);

		final Exam exam = this.examService.findOne(examId);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(question, examId);
			result.addObject("exam", exam);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.questionService.save(question, exam.getId());
				result = this.examController.display(exam.getId());
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(question, examId, "commit.question.create.error");
				result.addObject("exam", exam);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(question, examId, "commit.question.create.error");
				result.addObject("exam", exam);
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}
	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int questionId, @RequestParam final int examId) {
		final ModelAndView result;
		final Question question;
		final Exam exam;
		question = this.questionService.findOne(questionId);
		exam = this.examService.findOne(examId);
		result = new ModelAndView("question/display");
		result.addObject("question", question);
		result.addObject("exam", exam);
		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int questionId) {
		ModelAndView result;
		final Question question = this.questionService.findOne(questionId);
		try {
			this.questionService.delete(question);
			result = this.examController.display(question.getExam().getId());
		} catch (final Throwable oops) {
			String errormsg = "question.delete.error";
			result = this.examController.display(question.getExam().getId());
			if (!question.getExam().getStatus().equals("PENDING"))
				errormsg = "question.delete.no.pending";
			result.addObject("msg", errormsg);
		}

		return result;

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Question question, final int examId) {
		ModelAndView result;
		result = this.createEditModelAndView(question, examId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Question question, final int examId, final String messageCode) {
		Assert.notNull(question);
		final ModelAndView result;

		result = new ModelAndView("question/edit1");
		result.addObject("question", question);
		result.addObject("message", messageCode);
		return result;
	}

}
