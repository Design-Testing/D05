
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
import services.CompanyService;
import services.ConfigurationParametersService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Company;
import forms.CompanyForm;

@Controller
@RequestMapping("/company")
public class CompanyController extends AbstractController {

	@Autowired
	private CompanyService					companyService;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// Constructors -----------------------------------------------------------

	public CompanyController() {
		super();
	}

	// LIST ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Collection<Company> companies = this.companyService.findAll();

		res = new ModelAndView("company/list");
		res.addObject("companies", companies);

		return res;
	}

	// COMPUTE SCORE ----------------------------------------------------------------

	@RequestMapping(value = "/computeScore", method = RequestMethod.GET)
	public ModelAndView computeScore(@RequestParam final int companyId) {
		final ModelAndView res;
		final Collection<Company> companies = this.companyService.findAll();

		this.companyService.computeScore(this.companyService.findOne(companyId));

		res = new ModelAndView("company/list");
		res.addObject("companies", companies);

		return res;
	}

	// COMPUTE SCORES ----------------------------------------------------------------

	@RequestMapping(value = "/computeScores", method = RequestMethod.GET)
	public ModelAndView computeScores() {
		final ModelAndView res;
		final Collection<Company> companies = this.companyService.findAll();

		this.companyService.computeScores();

		res = new ModelAndView("company/list");
		res.addObject("companies", companies);

		return res;
	}

	// CREATE ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final CompanyForm company = new CompanyForm();
		result = this.createEditModelAndView(company);
		return result;
	}

	// DISPLAY -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int companyId) {
		final ModelAndView result;
		final Company company = this.companyService.findOne(companyId);
		if (company != null) {
			result = new ModelAndView("company/display");
			result.addObject("company", company);
			result.addObject("displayButtons", false);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}
	@RequestMapping(value = "/display2", method = RequestMethod.GET)
	public ModelAndView display2() {
		final ModelAndView result;
		final Company company = this.companyService.findByPrincipal();
		if (company != null) {
			result = new ModelAndView("company/display2");
			result.addObject("company", company);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// EDIT -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Company company = this.companyService.findByPrincipal();
		final CompanyForm companyForm = this.registerService.inyect(company);
		companyForm.setTermsAndCondicions(true);
		result = this.createEditModelAndView(companyForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// SAVE -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final CompanyForm companyForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("company/edit");
		Company company;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			companyForm.setTermsAndCondicions(false);
			result.addObject("companyForm", companyForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(companyForm, Authority.COMPANY);
				company = this.companyService.reconstruct(companyForm, binding);
				company.setUserAccount(ua);
				this.registerService.saveCompany(company, binding);
				result.addObject("alert", "company.edit.correct");
				result.addObject("companyForm", companyForm);
			} catch (final ValidationException oops) {
				result.addObject("errors", binding.getAllErrors());
				companyForm.setTermsAndCondicions(false);
				result.addObject("companyForm", companyForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "company.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				companyForm.setTermsAndCondicions(false);
				result.addObject("companyForm", companyForm);
			}
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// GDPR -----------------------------------------------------------
	@RequestMapping(value = "/deletePersonalData")
	public ModelAndView deletePersonalData() {
		this.companyService.deletePersonalData();

		final ModelAndView result = new ModelAndView("redirect:../j_spring_security_logout");
		return result;
	}

	// ANCILLARY METHODS  ---------------------------------------------------------------		

	protected ModelAndView createEditModelAndView(final CompanyForm companyForm) {
		ModelAndView result;

		result = this.createEditModelAndView(companyForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CompanyForm companyForm, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("company/edit");
		result.addObject("companyForm", companyForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		result.addObject("message", messageCode);

		return result;
	}

}
