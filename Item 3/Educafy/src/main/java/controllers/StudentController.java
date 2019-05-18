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
import services.ConfigurationParametersService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Student;
import forms.ActorForm;

@Controller
@RequestMapping("/student")
public class StudentController extends AbstractController {

	@Autowired
	private StudentService					studentService;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// Constructors -----------------------------------------------------------

	public StudentController() {
		super();
	}

	// CREATE ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ActorForm student = new ActorForm();
		result = this.createEditModelAndView(student);
		return result;
	}

	// DISPLAY TABLA -----------------------------------------------------------

	@RequestMapping(value = "/displayTabla", method = RequestMethod.GET)
	public ModelAndView displayTabla(@RequestParam final int studentId) {
		final ModelAndView result;
		final Student student = this.studentService.findOne(studentId);
		if (student != null) {
			result = new ModelAndView("student/display");
			result.addObject("student", student);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// DISPLAY PRINCIPAL -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Student student = this.studentService.findByPrincipal();
		if (student != null) {
			result = new ModelAndView("student/display");
			result.addObject("student", student);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// EDIT -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("student/edit");
		final Student student = this.studentService.findByPrincipal();
		final ActorForm actor = this.registerService.inyect(student);
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
		result = new ModelAndView("student/edit");
		Student student;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			actorForm.setTermsAndCondicions(false);
			result.addObject("actorForm", actorForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(actorForm, Authority.ROOKY);
				student = this.studentService.reconstruct(actorForm, binding);
				student.setUserAccount(ua);
				this.registerService.saveStudent(student, binding);
				result.addObject("alert", "student.edit.correct");
				result.addObject("actorForm", actorForm);
			} catch (final ValidationException oops) {
				result.addObject("errors", binding.getAllErrors());
				actorForm.setTermsAndCondicions(false);
				result.addObject("actorForm", actorForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "student.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				actorForm.setTermsAndCondicions(false);
				result.addObject("actorForm", actorForm);
			}
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// GDPR -----------------------------------------------------------
	@RequestMapping(value = "/deletePersonalData")
	public ModelAndView deletePersonalData() {
		this.studentService.deletePersonalData();

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

		result = new ModelAndView("student/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		result.addObject("message", messageCode);

		return result;
	}

}
