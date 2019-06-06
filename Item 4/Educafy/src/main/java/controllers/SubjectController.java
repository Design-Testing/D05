
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigurationParametersService;
import services.LessonService;
import services.SubjectService;
import domain.Lesson;
import domain.Subject;

@Controller
@RequestMapping("/subject")
public class SubjectController extends AbstractController {

	@Autowired
	private SubjectService					subjectService;

	@Autowired
	private LessonService					lessonService;

	@Autowired
	private AdministratorService			administratorService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		this.administratorService.findByPrincipal();
		ModelAndView res;
		Subject subject;

		subject = this.subjectService.create();
		res = this.createEditModelAndView(subject);
		res.addObject("new", false);

		return res;

	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int subjectId) {
		this.administratorService.findByPrincipal();
		ModelAndView result;
		Subject subject;

		subject = this.subjectService.findOne(subjectId);
		if (subject != null) {
			result = this.createEditModelAndView(subject);
			result.addObject("id", subject.getId());
		} else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Subject subject, final BindingResult binding) {
		this.administratorService.findByPrincipal();
		ModelAndView res = null;
		//		final Actor principal = this.actorService.findByPrincipal();

		if (binding.hasErrors())
			res = this.createEditModelAndView(subject);

		else if (subject != null)

			try {
				this.subjectService.save(subject);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(subject, "general.commit.error");
			}

		return res;
	}

	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int subjectId) {
		this.administratorService.findByPrincipal();
		ModelAndView result;
		final Subject subject = this.subjectService.findOne(subjectId);

		try {
			this.subjectService.delete(subject);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(subject, "general.commit.error");
			result.addObject("id", subject.getId());
		}

		return result;
	}

	// DISPLAY --------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int subjectId) {
		final ModelAndView result;
		final Subject subject;
		final Collection<Lesson> lessons = this.lessonService.findAllBySubject(subjectId);

		subject = this.subjectService.findOne(subjectId);

		if (subject != null) {
			result = new ModelAndView("subject/display");
			result.addObject("subject", subject);
			result.addObject("lang", this.lang);
			result.addObject("lessons", lessons);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Subject> subjects;

		subjects = this.subjectService.findAll();

		result = new ModelAndView("subject/list");
		result.addObject("subjects", subjects);
		result.addObject("lang", this.lang);
		result.addObject("requestURI", "subject/list.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final Subject subject) {

		ModelAndView res;

		res = this.createEditModelAndView(subject, null);

		return res;

	}

	protected ModelAndView createEditModelAndView(final Subject subject, final String messageCode) {

		ModelAndView res;

		res = new ModelAndView("subject/edit");
		res.addObject("subject", subject);

		res.addObject("message", messageCode);
		res.addObject("subjectLevel", this.configurationParametersService.findSubjectLevels());
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;

	}

}
