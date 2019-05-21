
package controllers.student;

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

import services.AssesmentService;
import services.LessonService;
import services.StudentService;
import controllers.AbstractController;
import domain.Assesment;
import domain.Lesson;
import domain.Student;
import forms.AssesmentForm;

@Controller
@RequestMapping("/assesment/student")
public class AssesmentStudentController extends AbstractController {

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private LessonService		lessonService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int lessonId) {
		ModelAndView result;
		final Assesment assesment = this.assesmentService.create();
		result = this.createEditModelAndView(assesment, lessonId);
		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myAssesments", method = RequestMethod.GET)
	public ModelAndView myAssesments(@RequestParam final int lessonId) {
		final ModelAndView result;
		final Collection<Assesment> assesments;
		Lesson lesson;

		assesments = this.assesmentService.findAllByStudentPrincipal();
		lesson = this.lessonService.findOne(lessonId);

		if (lesson != null) {
			result = new ModelAndView("assesment/list");
			result.addObject("assesments", assesments);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		result.addObject("lang", this.lang);
		result.addObject("rol", "student");
		result.addObject("requetURI", "assesment/student/myAssesments.do");
		result.addObject("principalID", this.studentService.findByPrincipal().getId());

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int assesmentId) {
		final ModelAndView result;
		final Assesment assesment;
		final Student student;

		assesment = this.assesmentService.findOne(assesmentId);
		student = this.studentService.findByPrincipal();

		result = new ModelAndView("assesment/display");
		result.addObject("assesment", assesment);
		result.addObject("student", student);
		result.addObject("studentId", student.getId());
		result.addObject("rol", "student");
		result.addObject("lang", this.lang);

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int assesmentId, @RequestParam final int lessonId) {
		ModelAndView result;
		Assesment assesment;

		assesment = this.assesmentService.findOne(assesmentId);

		final Student student = this.studentService.findByPrincipal();

		if (assesment.getStudent().equals(student))
			result = this.createEditModelAndView(assesment, lessonId);
		else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final AssesmentForm assesmentForm, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String paramLessonId;
		Integer lessonId;

		paramLessonId = request.getParameter("lessonId");
		lessonId = paramLessonId.isEmpty() ? null : Integer.parseInt(paramLessonId);

		final Assesment assesment = this.assesmentService.reconstruct(assesmentForm, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(assesment, lessonId);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.assesmentService.save(assesment, lessonId);
				result = this.myAssesments(lessonId);
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(assesment, lessonId, "commit.assesment.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(assesment, lessonId, "commit.assesment.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Assesment assesment, final int lessonId) {
		ModelAndView result;
		result = this.createEditModelAndView(assesment, lessonId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Assesment assesment, final int lessonId, final String messageCode) {
		Assert.notNull(assesment);
		final ModelAndView result;

		result = new ModelAndView("assesment/edit");
		result.addObject("assesmentForm", this.constructPruned(assesment));
		result.addObject("lessonId", lessonId);
		result.addObject("message", messageCode);

		return result;
	}

	public AssesmentForm constructPruned(final Assesment assesment) {
		final AssesmentForm pruned = new AssesmentForm();

		pruned.setId(assesment.getId());
		pruned.setVersion(assesment.getVersion());
		pruned.setScore(assesment.getScore());
		pruned.setComment(assesment.getComment());

		return pruned;
	}

}
