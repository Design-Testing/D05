
package controllers;

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
import services.ReservationService;
import domain.Assesment;
import domain.Lesson;
import domain.Reservation;

@Controller
@RequestMapping("/lesson")
public class LessonController extends AbstractController {

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private ReservationService	reservationService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int lessonId) {
		final ModelAndView result;
		final Lesson lesson;
		Collection<Assesment> assesments;
		Collection<Reservation> reservations;

		lesson = this.lessonService.findOne(lessonId);
		assesments = this.assesmentService.findAllAssesmentByLesson(lessonId);
		reservations = this.reservationService.findAllReservationByLesson(lessonId);

		if (lesson != null) {
			result = new ModelAndView("lesson/display");
			result.addObject("lesson", lesson);
			result.addObject("assesments", assesments);
			result.addObject("reservations", reservations);
			result.addObject("lang", this.lang);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/displayR", method = RequestMethod.GET)
	public ModelAndView displayR(@RequestParam final int lessonId, @RequestParam final String url) {
		final ModelAndView result;
		final Lesson lesson;
		Collection<Assesment> assesments;
		Collection<Reservation> reservations;

		lesson = this.lessonService.findOne(lessonId);
		assesments = this.assesmentService.findAllAssesmentByLesson(lessonId);
		reservations = this.reservationService.findAllReservationByLesson(lessonId);

		if (lesson != null) {
			result = new ModelAndView("lesson/display");
			result.addObject("lesson", lesson);
			result.addObject("assesments", assesments);
			result.addObject("reservations", reservations);
			result.addObject("backURL", url);
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

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myLessons", method = RequestMethod.GET)
	public ModelAndView myLessons(@RequestParam final int teacherId) {
		final ModelAndView result;
		final Collection<Lesson> lessons;

		lessons = this.lessonService.findAllLessonsByTeacher(teacherId);

		result = new ModelAndView("lesson/list");
		result.addObject("lessons", lessons);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "lesson/myLessons.do");

		return result;
	}

}
