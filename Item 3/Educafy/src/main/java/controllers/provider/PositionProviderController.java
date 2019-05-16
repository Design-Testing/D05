
package controllers.provider;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PositionService;
import services.ProblemService;
import services.ProviderService;
import services.SponsorshipService;
import domain.Position;
import domain.Problem;
import domain.Provider;
import domain.Sponsorship;

@Controller
@RequestMapping("/position/provider")
public class PositionProviderController {

	@Autowired
	SponsorshipService	sponsorshipService;

	@Autowired
	PositionService		positionService;

	@Autowired
	ProviderService		providerService;

	@Autowired
	ProblemService		problemService;

	final String		lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Position> positions;
		Collection<Position> myPositions;
		final Provider s = this.providerService.findByPrincipal();
		positions = this.positionService.findAllFinalMode();
		myPositions = this.positionService.findAllPositionByProvider(s);
		result = new ModelAndView("position/list");
		result.addObject("providerpositions", myPositions);
		result.addObject("positions", positions);
		result.addObject("lang", this.lang);
		result.addObject("rol", "provider");
		//result.addObject("listPositions", "list");

		result.addObject("requetURI", "position/provider/list.do");
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		final ModelAndView result;
		Position position;

		position = this.positionService.findOne(positionId);
		final Collection<Problem> problems = this.problemService.findProblemsByPosition(positionId);

		if (position != null && position.getMode().equals("FINAL")) {
			result = new ModelAndView("position/display");
			result.addObject("position", position);
			result.addObject("rol", "sponsor");
			result.addObject("lang", this.lang);
			result.addObject("problems", problems);
			final Sponsorship sp = this.sponsorshipService.findRandomSponsorship(positionId);
			if (sp != null) {
				final String imgbanner = sp.getBanner();
				result.addObject("imgbanner", imgbanner);
				final String targetpage = sp.getTargetPage();
				result.addObject("targetpage", targetpage);
			}
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

}
