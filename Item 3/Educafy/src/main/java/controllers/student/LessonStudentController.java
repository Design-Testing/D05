
package controllers.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import services.LessonService;
import services.StudentService;
import controllers.AbstractController;

@Controller
@RequestMapping("/lesson/student")
public class LessonStudentController extends AbstractController {

	@Autowired
	private LessonService	lessonService;

	@Autowired
	private StudentService	studentService;

	final String			lang	= LocaleContextHolder.getLocale().getLanguage();

	// LIST --------------------------------------------------------

	//	@RequestMapping(value = "/myLessons", method = RequestMethod.GET)
	//	public ModelAndView myLessons() {
	//		final ModelAndView result;
	//		final Collection<Lesson> lessons;
	//
	//		lessons = this.lessonService.findAllByStudent();
	//
	//		result = new ModelAndView("lesson/list");
	//		result.addObject("lessons", lessons);
	//		result.addObject("lang", this.lang);
	//		result.addObject("rol", "student");
	//		result.addObject("requetURI", "lesson/student/myLessons.do");
	//		result.addObject("principalID", this.studentService.findByPrincipal().getId());
	//
	//		return result;
	//	}

}
