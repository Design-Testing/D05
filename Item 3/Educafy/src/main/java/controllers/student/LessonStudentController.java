
package controllers.student;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/lesson/student")
public class LessonStudentController extends AbstractController {

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private AssesmentService	assesmentService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myLessons", method = RequestMethod.GET)
	public ModelAndView myLessons() {
		final ModelAndView result;
		final Collection<Lesson> lessons;

		lessons = this.lessonService.findAllByStudent();

		result = new ModelAndView("lesson/list");
		result.addObject("lessons", lessons);
		result.addObject("lang", this.lang);
		result.addObject("rol", "student");
		result.addObject("requetURI", "lesson/student/myLessons.do");
		result.addObject("principalID", this.studentService.findByPrincipal().getId());

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int lessonId) {
		final ModelAndView result;
		final Lesson lesson;
		final Student student;
		Collection<Assesment> assesments;

		lesson = this.lessonService.findOne(lessonId);
		student = this.studentService.findByPrincipal();
		assesments = this.assesmentService.findAllAssesmentByLesson(lessonId);

		result = new ModelAndView("lesson/display");
		result.addObject("lesson", lesson);
		result.addObject("assesments", assesments);
		result.addObject("student", student);
		result.addObject("studentId", student.getId());
		result.addObject("rol", "student");
		result.addObject("lang", this.lang);

		return result;
	}

}
