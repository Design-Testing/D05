
package controllers.administrator;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationParametersService;
import controllers.AbstractController;
import domain.ConfigurationParameters;

@Controller
@RequestMapping(value = "/configurationParameters/administrator")
public class ConfigurationParametersAdministratorController extends AbstractController {

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private ActorService					actorService;


	//To open the view to edit-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		ConfigurationParameters configurationParameters;
		configurationParameters = this.configurationParametersService.find();
		Assert.notNull(configurationParameters);
		result = this.createEditModelAndView(configurationParameters);

		return result;
	}

	//To save what has been edited-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final domain.ConfigurationParameters configurationParameters, final BindingResult binding) {

		ModelAndView result = new ModelAndView("configurationParameters/edit");

		if (binding.hasErrors())
			//result = this.createEditModelAndView(configurationParameters);
			result.addObject("errors", binding.getFieldErrors());
		else
			try {
				this.configurationParametersService.save(configurationParameters);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(configurationParameters, "configurationSystem.commit.error");
			}

		return result;

	}

	//	@RequestMapping(value = "/rebrand", method = RequestMethod.GET)
	//	public ModelAndView rebrand() {
	//		final ModelAndView result;
	//		final Actor principal = this.actorService.findByPrincipal();
	//		final boolean rebrand = this.configurationParametersService.find().isRebranding();
	//		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
	//		Assert.isTrue(isAdmin);
	//
	//		if (isAdmin && !rebrand)
	//			result = new ModelAndView("configurationParameters/rebranding");
	//		else if (rebrand) {
	//			result = new ModelAndView("configurationParameters/rebranding");
	//			result.addObject("errortrace", "rebrand.only.once");
	//		} else {
	//			result = new ModelAndView("configurationParameters/rebranding");
	//			result.addObject("errortrace", "rebrand.commit.error");
	//		}
	//
	//		return result;
	//
	//	}

	//	@RequestMapping(value = "/rebranding", method = RequestMethod.GET)
	//	public ModelAndView rebranding(@RequestParam final String newSysName) {
	//		ModelAndView result;
	//		try {
	//			this.configurationParametersService.rebranding(newSysName);
	//			result = this.toWelcome("rebranding.process.finished");
	//		} catch (final Throwable e) {
	//			result = new ModelAndView("configurationParameters/rebranding");
	//			result.addObject("errortrace", "rebrand.commit.error");
	//		}
	//		return result;
	//	}

	private ModelAndView toWelcome(final String alert) {
		ModelAndView result;
		result = new ModelAndView("welcome/index");
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		String mensaje = null;
		if (lang.equals("en"))
			mensaje = this.configurationParametersService.findWelcomeMessageEn();
		else if (lang.equals("es"))
			mensaje = this.configurationParametersService.findWelcomeMessageEsp();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		final String moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("moment", moment);
		result.addObject("mensaje", mensaje);

		result.addObject("alert", alert);
		return result;
	}

	//Ancillary methods------------------

	protected ModelAndView createEditModelAndView(final ConfigurationParameters cParameters) {

		Assert.notNull(cParameters);
		ModelAndView result;
		result = this.createEditModelAndView(cParameters, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ConfigurationParameters cParameters, final String messageCode) {
		Assert.notNull(cParameters);

		ModelAndView result;

		result = new ModelAndView("configurationParameters/edit");
		result.addObject("configurationParameters", cParameters);
		result.addObject("message", messageCode);
		result.addObject("RequestURI", "configurationParameters/administrator/edit.do");

		return result;

	}
}
