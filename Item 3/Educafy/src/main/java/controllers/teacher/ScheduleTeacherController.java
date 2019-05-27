
package controllers.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AssesmentService;
import services.ScheduleService;
import services.TeacherService;
import controllers.AbstractController;
import controllers.SubjectController;
import domain.Lesson;
import domain.Schedule;
import domain.Teacher;
import forms.LessonForm;

@Controller
@RequestMapping("/schedule/teacher")
public class ScheduleTeacherController extends AbstractController {

	@Autowired
	private ScheduleService		scheduleService;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private SubjectController	subjectController;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Schedule schedule = this.scheduleService.create();
		result = this.display(schedule.getId());
		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int teacherId) {
		final ModelAndView result;
		Teacher teacher;
		final Schedule schedule;

		teacher = this.teacherService.findOne(teacherId);
		schedule = this.scheduleService.findScheduleByTeacher(teacher);

		if (schedule != null) {
			result = new ModelAndView("schedule/display");
			result.addObject("schedule", schedule);
			result.addObject("lang", this.lang);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// MYSCHEDULE --------------------------------------------------------

	@RequestMapping(value = "/mySchedule", method = RequestMethod.GET)
	public ModelAndView mySchedule() {
		final ModelAndView result;
		Teacher teacher;
		final Schedule schedule;

		teacher = this.teacherService.findByPrincipal();
		schedule = this.scheduleService.findScheduleByTeacher(teacher);

		if (schedule != null) {
			result = new ModelAndView("schedule/display");
			result.addObject("schedule", schedule);
			result.addObject("lang", this.lang);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// SUGGEST --------------------------------------------------------

	//	// EDIT --------------------------------------------------------
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int lessonId, @RequestParam final int subjectId) {
	//		ModelAndView result;
	//		Lesson lesson;
	//
	//		lesson = this.lessonService.findOne(lessonId);
	//
	//		final Teacher teacher = this.teacherService.findByPrincipal();
	//
	//		if ((lesson.getIsDraft() && lesson.getTeacher().equals(teacher)))
	//			result = this.createEditModelAndView(lesson, subjectId);
	//		else
	//			result = new ModelAndView("redirect:misc/403");
	//
	//		return result;
	//	}
	//
	//	// SAVE --------------------------------------------------------
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	//	public ModelAndView save(@Valid final LessonForm lessonForm, final BindingResult binding, final HttpServletRequest request) {
	//		ModelAndView result;
	//		String paramSubjectId;
	//		Integer subjectId;
	//
	//		paramSubjectId = request.getParameter("subjectId");
	//		subjectId = paramSubjectId.isEmpty() ? null : Integer.parseInt(paramSubjectId);
	//
	//		if (binding.hasErrors()) {
	//			result = this.createEditModelAndView(lessonForm, subjectId);
	//			result.addObject("errors", binding.getAllErrors());
	//		} else
	//			try {
	//				final Lesson lesson = this.lessonService.reconstruct(lessonForm, binding);
	//				this.lessonService.save(lesson, subjectId);
	//				result = this.subjectController.display(subjectId);
	//			} catch (final ValidationException oops) {
	//				result = this.createEditModelAndView(lessonForm, subjectId, "commit.lesson.error");
	//			} catch (final Throwable oops) {
	//				result = this.createEditModelAndView(lessonForm, subjectId, "commit.lesson.error");
	//				result.addObject("errors", binding.getAllErrors());
	//			}
	//
	//		return result;
	//	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Lesson lesson, final int subjectId) {
		ModelAndView result;
		result = this.createEditModelAndView(lesson, subjectId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Lesson lesson, final int subjectId, final String messageCode) {
		Assert.notNull(lesson);
		final ModelAndView result;

		result = new ModelAndView("lesson/edit");
		result.addObject("lessonForm", this.constructPruned(lesson)); //this.constructPruned(position)
		result.addObject("subjectId", subjectId);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LessonForm lesson, final int subjectId) {
		ModelAndView result;
		result = this.createEditModelAndView(lesson, subjectId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LessonForm lessonForm, final int subjectId, final String messageCode) {
		Assert.notNull(lessonForm);
		final ModelAndView result;

		result = new ModelAndView("lesson/edit");
		result.addObject("lessonForm", lessonForm);
		result.addObject("subjectId", subjectId);
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
