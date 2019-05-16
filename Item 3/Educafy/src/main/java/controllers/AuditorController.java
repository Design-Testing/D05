
package controllers;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.AuditorService;
import services.ConfigurationParametersService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Auditor;
import forms.AuditorForm;

@Controller
@RequestMapping("/auditor")
public class AuditorController extends AbstractController {

	@Autowired
	private AuditorService					auditorService;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// Constructors -----------------------------------------------------------

	public AuditorController() {
		super();
	}

	// LIST ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Collection<Auditor> auditors = this.auditorService.findAll();

		res = new ModelAndView("auditor/list");
		res.addObject("auditors", auditors);

		return res;
	}

	// CREATE ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final AuditorForm auditor = new AuditorForm();
		result = this.createEditModelAndView(auditor);
		return result;
	}

	// DISPLAY desde admin -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int auditorId) {

		final UserAccount logged = LoginService.getPrincipal();

		final Authority authAuditor = new Authority();
		authAuditor.setAuthority(Authority.AUDITOR);
		if (logged.getAuthorities().contains(authAuditor)) {
			final Auditor auditor = this.auditorService.findByPrincipal();
			Assert.isTrue(auditor.getId() == auditorId, "yo can not see another auditor data");
		}

		final ModelAndView result;
		final Auditor auditor = this.auditorService.findOne(auditorId);
		if (auditor != null) {
			result = new ModelAndView("auditor/display");
			result.addObject("auditor", auditor);
			result.addObject("displayButtons", false);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// DISPLAY desde el mismo auditor
	@RequestMapping(value = "/display2", method = RequestMethod.GET)
	public ModelAndView display2() {

		final ModelAndView result;
		final Auditor auditor = this.auditorService.findByPrincipal();
		if (auditor != null) {
			result = new ModelAndView("auditor/display2");
			result.addObject("auditor", auditor);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// EDIT -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		ModelAndView result;

		final Auditor auditor = this.auditorService.findByPrincipal();
		final AuditorForm auditorForm = this.registerService.inyect(auditor);
		auditorForm.setTermsAndCondicions(true);
		result = this.createEditModelAndView(auditorForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// SAVE -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final AuditorForm auditorForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("auditor/edit");
		Auditor auditor;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			auditorForm.setTermsAndCondicions(false);
			result.addObject("auditorForm", auditorForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(auditorForm, Authority.AUDITOR);
				auditor = this.auditorService.reconstruct(auditorForm, binding);
				auditor.setUserAccount(ua);
				this.registerService.saveAuditor(auditor, binding);
				result.addObject("alert", "auditor.edit.correct");
				result.addObject("auditorForm", auditorForm);
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(auditorForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "auditor.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				auditorForm.setTermsAndCondicions(false);
				result.addObject("auditorForm", auditorForm);
			}
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// GDPR -----------------------------------------------------------
	@RequestMapping(value = "/deletePersonalData")
	public ModelAndView deletePersonalData() {
		this.auditorService.deletePersonalData();

		final ModelAndView result = new ModelAndView("redirect:../j_spring_security_logout");
		return result;
	}

	// ANCILLARY METHODS  ---------------------------------------------------------------		

	protected ModelAndView createEditModelAndView(final AuditorForm auditorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(auditorForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final AuditorForm auditorForm, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("auditor/edit");
		result.addObject("auditorForm", auditorForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		result.addObject("message", messageCode);

		return result;
	}

}
