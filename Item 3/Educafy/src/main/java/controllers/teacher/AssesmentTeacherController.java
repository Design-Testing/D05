
package controllers.teacher;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AssesmentService;
import services.TeacherService;
import controllers.AbstractController;
import domain.Assesment;

@Controller
@RequestMapping("/assesment/teacher")
public class AssesmentTeacherController extends AbstractController {

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private TeacherService		teacherService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


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
