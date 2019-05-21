
package controllers.teacher;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AssesmentService;
import services.TeacherService;
import controllers.AbstractController;
import domain.Assesment;
import domain.Teacher;

@Controller
@RequestMapping("/assesment/teacher")
public class AssesmentTeacherController extends AbstractController {

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private TeacherService		teacherService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int assesmentId) {
		final ModelAndView result;
		final Assesment assesment;
		final Teacher teacher;

		assesment = this.assesmentService.findOne(assesmentId);
		teacher = this.teacherService.findByPrincipal();

		result = new ModelAndView("assesment/display");
		result.addObject("assesment", assesment);
		result.addObject("teacher", teacher);
		result.addObject("teacherId", teacher.getId());
		result.addObject("rol", "teacher");
		result.addObject("lang", this.lang);

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myAssesments", method = RequestMethod.GET)
	public ModelAndView myAssesments() {
		final ModelAndView result;
		final Collection<Assesment> assesments;

		assesments = this.assesmentService.findAllByTeacherPrincipal();

		result = new ModelAndView("assesment/list");
		result.addObject("assesments", assesments);
		result.addObject("lang", this.lang);
		result.addObject("rol", "teacher");
		result.addObject("requetURI", "assesment/teacher/myAssesments.do");
		result.addObject("principalID", this.teacherService.findByPrincipal().getId());

		return result;
	}

}
