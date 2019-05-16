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
import services.RookyService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Rooky;
import forms.ActorForm;

@Controller
@RequestMapping("/rooky")
public class RookyController extends AbstractController {

	@Autowired
	private RookyService					rookyService;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// Constructors -----------------------------------------------------------

	public RookyController() {
		super();
	}

	// CREATE ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ActorForm rooky = new ActorForm();
		result = this.createEditModelAndView(rooky);
		return result;
	}

	// DISPLAY TABLA -----------------------------------------------------------

	@RequestMapping(value = "/displayTabla", method = RequestMethod.GET)
	public ModelAndView displayTabla(@RequestParam final int rookyId) {
		final ModelAndView result;
		final Rooky rooky = this.rookyService.findOne(rookyId);
		if (rooky != null) {
			result = new ModelAndView("rooky/display");
			result.addObject("rooky", rooky);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// DISPLAY PRINCIPAL -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Rooky rooky = this.rookyService.findByPrincipal();
		if (rooky != null) {
			result = new ModelAndView("rooky/display");
			result.addObject("rooky", rooky);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// EDIT -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("rooky/edit");
		final Rooky rooky = this.rookyService.findByPrincipal();
		final ActorForm actor = this.registerService.inyect(rooky);
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
		result = new ModelAndView("rooky/edit");
		Rooky rooky;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			actorForm.setTermsAndCondicions(false);
			result.addObject("actorForm", actorForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(actorForm, Authority.ROOKY);
				rooky = this.rookyService.reconstruct(actorForm, binding);
				rooky.setUserAccount(ua);
				this.registerService.saveRooky(rooky, binding);
				result.addObject("alert", "rooky.edit.correct");
				result.addObject("actorForm", actorForm);
			} catch (final ValidationException oops) {
				result.addObject("errors", binding.getAllErrors());
				actorForm.setTermsAndCondicions(false);
				result.addObject("actorForm", actorForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "rooky.edit.usernameIsUsed");
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
		this.rookyService.deletePersonalData();

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

		result = new ModelAndView("rooky/edit");
		result.addObject("actorForm", actorForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		result.addObject("message", messageCode);

		return result;
	}

}
