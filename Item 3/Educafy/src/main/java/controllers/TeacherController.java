/*
 * AdministratorController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import services.AssesmentService;
import services.ConfigurationParametersService;
import services.LessonService;
import services.TeacherService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Assesment;
import domain.Lesson;
import domain.Teacher;
import forms.ActorForm;

@Controller
@RequestMapping("/teacher")
public class TeacherController extends AbstractController {

	@Autowired
	private TeacherService					teacherService;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private LessonService					lessonService;

	@Autowired
	private AssesmentService				assesmentService;


	// Constructors -----------------------------------------------------------

	public TeacherController() {
		super();
	}

	// CREATE ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ActorForm teacher = new ActorForm();
		result = this.createEditModelAndView(teacher);
		return result;
	}

	// DISPLAY TABLA -----------------------------------------------------------

	@RequestMapping(value = "/displayTabla", method = RequestMethod.GET)
	public ModelAndView displayTabla(@RequestParam final int teacherId) {
		final ModelAndView result;
		final Teacher teacher = this.teacherService.findOne(teacherId);
		final Collection<Lesson> lessons = this.lessonService.findAllLessonsByTeacher(teacherId);
		//TODO: Sacar curriculum del teacher
		final Collection<Assesment> assesments = this.assesmentService.findAllAssesmentByTeacher(teacherId);
		//TODO: Sacar los comments del teacher
		if (teacher != null) {
			result = new ModelAndView("teacher/display");
			result.addObject("teacher", teacher);
			result.addObject("lessons", lessons);
			//			result.addObject("curriculum", curriculum);
			result.addObject("assesments", assesments);
			//			result.addObject("comments", comments);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}
	// DISPLAY PRINCIPAL -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Teacher teacher = this.teacherService.findByPrincipal();
		result = new ModelAndView("teacher/display");
		result.addObject("teacher", teacher);
		result.addObject("displayButtons", true);

		return result;

	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Teacher> teachers;

		teachers = this.teacherService.findAll();

		result = new ModelAndView("teacher/list");
		result.addObject("teachers", teachers);
		result.addObject("requetURI", "teacher/list.do");

		return result;
	}

	// EDIT -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("teacher/edit");
		final Teacher teacher = this.teacherService.findByPrincipal();
		final ActorForm actor = this.registerService.inyect(teacher);
		actor.setTermsAndCondicions(true);
		result.addObject("actorForm", actor);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// SAVE -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("teacher/edit");
		Teacher teacher;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			actorForm.setTermsAndCondicions(false);
			result.addObject("actorForm", actorForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(actorForm, Authority.TEACHER);
				teacher = this.teacherService.reconstruct(actorForm, binding);
				teacher.setUserAccount(ua);
				this.registerService.saveTeacher(teacher, binding);
				result.addObject("alert", "teacher.edit.correct");
				result.addObject("actorForm", actorForm);
			} catch (final ValidationException oops) {
				result.addObject("errors", binding.getAllErrors());
				actorForm.setTermsAndCondicions(false);
				result.addObject("actorForm", actorForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "teacher.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				actorForm.setTermsAndCondicions(false);
				result.addObject("actorForm", actorForm);
			}
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	//list all teachers
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView result;
		result = new ModelAndView("curriculum/listByTeachers");
		final Collection<Teacher> teachers = this.teacherService.findAll();
		result.addObject("teachers", teachers);
		return result;
	}

	// GDPR -----------------------------------------------------------
	@RequestMapping(value = "/deletePersonalData")
	public ModelAndView deletePersonalData() {
		this.teacherService.deletePersonalData();

		final ModelAndView result = new ModelAndView("redirect:../j_spring_security_logout");
		return result;
	}
	// ANCILLARY METHODS  ---------------------------------------------------------------		

	protected ModelAndView createEditModelAndView(final ActorForm actorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(actorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorForm actorForm, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("teacher/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		result.addObject("message", messageCode);

		return result;
	}

}
