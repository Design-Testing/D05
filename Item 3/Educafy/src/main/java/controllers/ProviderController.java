
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
import services.ConfigurationParametersService;
import services.ProviderService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Provider;
import forms.ProviderForm;

@Controller
@RequestMapping("/provider")
public class ProviderController extends AbstractController {

	@Autowired
	private ProviderService					providerService;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// Constructors -----------------------------------------------------------

	public ProviderController() {
		super();
	}

	// LIST ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Collection<Provider> providers = this.providerService.findAll();

		res = new ModelAndView("provider/list");
		res.addObject("providers", providers);

		return res;
	}

	// CREATE ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ProviderForm provider = new ProviderForm();
		result = this.createEditModelAndView(provider);
		return result;
	}

	// DISPLAY -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int providerId) {
		final ModelAndView result;
		final Provider provider = this.providerService.findOne(providerId);
		if (provider != null) {
			result = new ModelAndView("provider/display");
			result.addObject("provider", provider);
			result.addObject("displayButtons", false);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}
	@RequestMapping(value = "/display2", method = RequestMethod.GET)
	public ModelAndView display2() {
		final ModelAndView result;
		final Provider provider = this.providerService.findByPrincipal();
		if (provider != null) {
			result = new ModelAndView("provider/display2");
			result.addObject("provider", provider);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;

	}

	// EDIT -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Provider provider = this.providerService.findByPrincipal();
		final ProviderForm providerForm = this.registerService.inyect(provider);
		providerForm.setTermsAndCondicions(true);
		result = this.createEditModelAndView(providerForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// SAVE -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ProviderForm providerForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("provider/edit");
		Provider provider;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			providerForm.setTermsAndCondicions(false);
			result.addObject("providerForm", providerForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(providerForm, Authority.PROVIDER);
				provider = this.providerService.reconstruct(providerForm, binding);
				provider.setUserAccount(ua);
				this.registerService.saveProvider(provider, binding);
				result.addObject("alert", "provider.edit.correct");
				result.addObject("providerForm", providerForm);
			} catch (final ValidationException oops) {
				result.addObject("errors", binding.getAllErrors());
				providerForm.setTermsAndCondicions(false);
				result.addObject("providerForm", providerForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "provider.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				providerForm.setTermsAndCondicions(false);
				result.addObject("providerForm", providerForm);
			}
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		return result;
	}

	// GDPR -----------------------------------------------------------
	@RequestMapping(value = "/deletePersonalData")
	public ModelAndView deletePersonalData() {
		this.providerService.deletePersonalData();

		final ModelAndView result = new ModelAndView("redirect:../j_spring_security_logout");
		return result;
	}

	// ANCILLARY METHODS  ---------------------------------------------------------------		

	protected ModelAndView createEditModelAndView(final ProviderForm providerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(providerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ProviderForm providerForm, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("provider/edit");
		result.addObject("providerForm", providerForm);
		result.addObject("cardmakes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("countryPhoneCode", this.configurationParametersService.find().getCountryPhoneCode());
		result.addObject("message", messageCode);

		return result;
	}

}
