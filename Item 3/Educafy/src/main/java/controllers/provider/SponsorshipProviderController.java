
package controllers.provider;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import services.PositionService;
import services.ProviderService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Controller
@RequestMapping("/sponsorship/provider")
public class SponsorshipProviderController extends AbstractController {

	@Autowired
	private SponsorshipService				sponsorshipService;

	@Autowired
	private PositionService					positionService;

	@Autowired
	private ProviderService					providerService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// =================LIST=================

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Collection<Sponsorship> sponsorships;
		sponsorships = this.sponsorshipService.findAllByProvider();
		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);
		result.addObject("lang", this.lang);
		result.addObject("requestURI", "/sponsorship/provider/list.do");

		return result;
	}

	// =================CREATE=================

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		final ModelAndView result;

		Sponsorship sponsorship;
		Position position;
		position = this.positionService.findOne(positionId);
		sponsorship = this.sponsorshipService.create();
		result = this.createEditModelAndView(sponsorship);
		result.addObject("position", position);

		return result;
	}

	// =================EDIT=================

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;

		final Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		if (sponsorship != null) {
			final Collection<Sponsorship> ss = this.sponsorshipService.findAllByProvider();
			if (ss.contains(sponsorship))
				result = this.createEditModelAndView(sponsorship);
			else
				result = new ModelAndView("redirect:/misc/403.jsp");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute("sponsorshipForm") final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		ModelAndView result;

		Sponsorship sponsorship;
		try {
			sponsorship = this.sponsorshipService.reconstruct(sponsorshipForm, binding);
			this.sponsorshipService.save(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(sponsorshipForm);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorshipForm, "sponsorship.commit.error");
		}
		return result;
	}

	// =================DISPLAY=================

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		final ModelAndView result;
		Sponsorship sponsorship;
		final Provider provider = this.providerService.findByPrincipal();
		sponsorship = this.sponsorshipService.findByPosition(positionId, provider.getUserAccount().getId());
		if (sponsorship != null) {
			result = new ModelAndView("sponsorship/display");
			result.addObject("sponsorship", sponsorship);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	@RequestMapping(value = "/displaySponsorship", method = RequestMethod.GET)
	public ModelAndView displaySponsorship(@RequestParam final int sponsorshipId) {
		final ModelAndView result;
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.findOneSponsorship(sponsorshipId);

		if (sponsorship != null) {
			result = new ModelAndView("sponsorship/display");
			result.addObject("sponsorship", sponsorship);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	// =================DELETE=================

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		try {
			final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			this.sponsorshipService.delete(sponsorship);
			result = this.list();
		} catch (final Throwable e) {
			result = new ModelAndView("redirect:misc/403");
		}
		return result;
	}

	//ANCILLIARY METHODS

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorship) {
		ModelAndView result;
		result = this.createEditModelAndView(sponsorship, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm, final String messageCode) {
		final ModelAndView result;
		final Collection<Position> positions = this.positionService.positionsAvailableProvider();
		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("positions", positions);
		result.addObject("message", messageCode);
		result.addObject("makes", this.configurationParametersService.find().getCreditCardMake());
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;
		result = this.createEditModelAndView(sponsorship, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		final ModelAndView result;
		SponsorshipForm sponsorshipForm;
		if (sponsorship.getId() == 0)
			sponsorshipForm = new SponsorshipForm();
		else
			sponsorshipForm = this.sponsorshipService.inyect(sponsorship);
		final Collection<Position> positions = this.positionService.positionsAvailableProvider();
		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("positions", positions);
		result.addObject("makes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("message", messageCode);
		return result;
	}
}
