
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LessonService;
import domain.Lesson;

@Controller
@RequestMapping("/lesson")
public class LessonController extends AbstractController {

	@Autowired
	private LessonService	lessonService;

	final String			lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int lessonId) {
		final ModelAndView result;
		final Lesson lesson;

		lesson = this.lessonService.findOne(lessonId);
		if (lesson != null) {
			result = new ModelAndView("lesson/display");
			result.addObject("lesson", lesson);
			result.addObject("lang", this.lang);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Lesson> lessons;

		lessons = this.lessonService.findAllFinalMode();

		result = new ModelAndView("lesson/list");
		result.addObject("lessons", lessons);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "lesson/list.do");

		return result;
	}

}
