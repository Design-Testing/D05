
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

import services.AssesmentService;
import services.LessonService;
import services.TeacherService;
import controllers.AbstractController;
import domain.Assesment;
import domain.Lesson;
import domain.Teacher;
import forms.LessonForm;

@Controller
@RequestMapping("/lesson/teacher")
public class LessonTeacherController extends AbstractController {

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private AssesmentService	assesmentService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Lesson lesson = this.lessonService.create();
		result = this.createEditModelAndView(lesson);
		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int lessonId) {
		final ModelAndView result;
		final Lesson lesson;
		final Teacher teacher;
		Collection<Assesment> assesments;

		lesson = this.lessonService.findOne(lessonId);
		teacher = this.teacherService.findByPrincipal();
		assesments = this.assesmentService.findAllAssesmentByLesson(lessonId);

		result = new ModelAndView("lesson/display");
		result.addObject("lesson", lesson);
		result.addObject("assesments", assesments);
		result.addObject("teacher", teacher);
		result.addObject("teacherId", lesson.getTeacher().getId());
		result.addObject("rol", "teacher");
		result.addObject("lang", this.lang);

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myLessons", method = RequestMethod.GET)
	public ModelAndView myLessons() {
		final ModelAndView result;
		final Collection<Lesson> lessons;

		lessons = this.lessonService.findAllByTeacher();

		result = new ModelAndView("lesson/list");
		result.addObject("lessons", lessons);
		result.addObject("lang", this.lang);
		result.addObject("rol", "teacher");
		result.addObject("requetURI", "lesson/teacher/myLessons.do");
		result.addObject("principalID", this.teacherService.findByPrincipal().getId());

		return result;
	}

	// TO FINAL MODE --------------------------------------------------------

	@RequestMapping(value = "/finalMode", method = RequestMethod.GET)
	public ModelAndView finalMode(@RequestParam final int lessonId) {
		ModelAndView result;
		final Lesson lesson = this.lessonService.findOne(lessonId);

		if (lesson == null) {
			result = this.myLessons();
			result.addObject("msg", "lesson.final.mode.error");
		} else
			try {
				this.lessonService.toFinalMode(lessonId);
				result = this.myLessons();
			} catch (final Throwable oops) {
				String errormsg = "lesson.final.mode.error";
				result = this.myLessons();
				if (!lesson.getIsDraft())
					errormsg = "lesson.final.no.draft";
				result.addObject("msg", errormsg);
			}

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int lessonId) {
		ModelAndView result;
		Lesson lesson;

		lesson = this.lessonService.findOne(lessonId);

		final Teacher teacher = this.teacherService.findByPrincipal();

		if ((lesson.getIsDraft() && lesson.getTeacher().equals(teacher)))
			result = this.createEditModelAndView(lesson);
		else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LessonForm lessonForm, final BindingResult binding) {
		ModelAndView result;

		final Lesson lesson = this.lessonService.reconstruct(lessonForm, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(lesson);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.lessonService.save(lesson);
				result = this.myLessons();
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(lesson, "commit.lesson.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(lesson, "commit.lesson.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int lessonId) {
		final Lesson lesson = this.lessonService.findOne(lessonId);
		this.lessonService.delete(lesson);
		return this.myLessons();

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Lesson lesson) {
		ModelAndView result;
		result = this.createEditModelAndView(lesson, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Lesson lesson, final String messageCode) {
		Assert.notNull(lesson);
		final ModelAndView result;

		result = new ModelAndView("lesson/edit");
		result.addObject("lessonForm", this.constructPruned(lesson)); //this.constructPruned(position)
		result.addObject("message", messageCode);

		return result;
	}

	public LessonForm constructPruned(final Lesson lesson) {
		final LessonForm pruned = new LessonForm();

		pruned.setId(lesson.getId());
		pruned.setVersion(lesson.getVersion());
		pruned.setTitle(lesson.getTitle());
		pruned.setDescription(lesson.getDescription());
		pruned.setPrice(lesson.getPrice());

		return pruned;
	}

}
