
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.CurriculumService;
import services.TeacherService;
import domain.Curriculum;
import domain.Teacher;

@Controller
@RequestMapping("/curriculum")
public class CurriculumController extends AbstractController {

	@Autowired
	private CurriculumService	curriculumService;

	@Autowired
	private TeacherService		teacherService;


	@RequestMapping(value = "/displayById", method = RequestMethod.GET)
	public ModelAndView displayById(@RequestParam final int curriculumId) {
		final ModelAndView res;
		final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
		if (!(curriculum == null)) {

			res = new ModelAndView("curriculum/display");

			final UserAccount logged = LoginService.getPrincipal();
			res.addObject("curriculum", curriculum);

			final Authority authTeacher = new Authority();
			authTeacher.setAuthority(Authority.TEACHER);
			final Authority authStudent = new Authority();
			authStudent.setAuthority(Authority.STUDENT);
			final Authority authCertifier = new Authority();
			authCertifier.setAuthority(Authority.CERTIFIER);
			if (logged.getAuthorities().contains(authTeacher)) {
				if (curriculum.getTeacher().getId() == this.teacherService.findByPrincipal().getId())
					res.addObject("buttons", true);
				else
					res.addObject("buttonsAnonymous", true);
			} else if (logged.getAuthorities().contains(authStudent))
				res.addObject("buttonsAnonymous", true);
			else if (logged.getAuthorities().contains(authCertifier))
				res.addObject("buttonsCertifier", true);
		} else {
			res = new ModelAndView("curriculum/create");
			res.addObject("curriculum", curriculum);
		}

		return res;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView res;
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Curriculum curriculum = this.curriculumService.findCurriculumByTeacher(teacher.getId());
		if (!(curriculum == null)) {

			res = new ModelAndView("curriculum/display");

			res.addObject("curriculum", curriculum);

			final UserAccount logged = LoginService.getPrincipal();

			final Authority authTeacher = new Authority();
			authTeacher.setAuthority(Authority.TEACHER);
			final Authority authStudent = new Authority();
			authStudent.setAuthority(Authority.STUDENT);
			final Authority authCertifier = new Authority();
			authCertifier.setAuthority(Authority.CERTIFIER);
			if (logged.getAuthorities().contains(authTeacher)) {
				if (curriculum.getTeacher().getId() == this.teacherService.findByPrincipal().getId())
					res.addObject("buttons", true);
				else
					res.addObject("buttonsAnonymous", true);
			} else if (logged.getAuthorities().contains(authStudent))
				res.addObject("buttonsAnonymous", true);
			else if (logged.getAuthorities().contains(authCertifier))
				res.addObject("buttonsCertifier", true);

		} else {
			res = new ModelAndView("curriculum/create");
			res.addObject("curriculum", curriculum);
		}

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Teacher teacher = this.teacherService.findByPrincipal();
		if (this.curriculumService.findCurriculumByTeacher(teacher.getId()) == null) {
			final Curriculum curriculum = this.curriculumService.create();
			this.curriculumService.save(curriculum);
			result = this.display();
			result.addObject("curriculum", curriculum);
		}
		result = this.display();
		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Curriculum curriculum, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(curriculum);
		else
			try {
				if (curriculum.getVersion() == 0) {
					final Teacher teacher = this.teacherService.findByPrincipal();
					this.curriculumService.save(curriculum);
				}
				result = this.display();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(curriculum, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int curriculumId) {
		ModelAndView result;
		//		final Teacher teacher = this.teacherService.findByPrincipal();
		final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
		this.curriculumService.delete(curriculum);
		result = this.display();
		result.addObject("curriculum", curriculum);
		return result;
	}

	@RequestMapping(value = "/displayByTeacherId", method = RequestMethod.GET)
	public ModelAndView displayByTeacherId(@RequestParam final int teacherId) {
		final ModelAndView res;
		final Curriculum curriculum = this.curriculumService.findCurriculumByTeacher(teacherId);
		if (!(curriculum == null)) {

			res = new ModelAndView("curriculum/display");

			final UserAccount logged = LoginService.getPrincipal();
			res.addObject("curriculum", curriculum);

			final Authority authTeacher = new Authority();
			authTeacher.setAuthority(Authority.TEACHER);
			final Authority authStudent = new Authority();
			authStudent.setAuthority(Authority.STUDENT);
			final Authority authCertifier = new Authority();
			authCertifier.setAuthority(Authority.CERTIFIER);
			if (logged.getAuthorities().contains(authTeacher)) {
				if (curriculum.getTeacher().getId() == this.teacherService.findByPrincipal().getId())
					res.addObject("buttons", true);
				else
					res.addObject("buttonsAnonymous", true);
			} else if (logged.getAuthorities().contains(authStudent))
				res.addObject("buttonsAnonymous", true);
			else if (logged.getAuthorities().contains(authCertifier))
				res.addObject("buttonsCertifier", true);
		} else {
			res = new ModelAndView("curriculum/create");
			res.addObject("curriculum", curriculum);
		}

		return res;
	}

	protected ModelAndView createEditModelAndView(final Curriculum curriculum) {
		ModelAndView result;

		result = this.createEditModelAndView(curriculum, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Curriculum curriculum, final String message) {
		ModelAndView result;

		result = new ModelAndView("curriculum/display");
		result.addObject("curriculum", curriculum);
		result.addObject("message", message);

		return result;

	}
}
