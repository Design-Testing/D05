
package controllers;

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
import services.SponsorshipService;
import domain.Position;
import domain.Problem;
import domain.Sponsorship;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

	@Autowired
	private PositionService		positionService;

	@Autowired
	private ProblemService		problemService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		final ModelAndView result;
		final Position position;

		position = this.positionService.findOne(positionId);
		final Collection<Problem> problems = this.problemService.findProblemsByPosition(positionId);

		if (position != null && !position.getMode().equals("DRAFT")) {
			result = new ModelAndView("position/display");
			result.addObject("position", position);
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
			result = new ModelAndView("redirect:misc/403");

		return result;
	}
	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView myPositions() {
		final ModelAndView result;
		final Collection<Position> positions;

		positions = this.positionService.findAllFinalMode();

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);
		result.addObject("listPositions", "list");
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "position/list.do");

		return result;
	}

	// COMPANY POSITION LIST --------------------------------------------------------

	@RequestMapping(value = "/companyList", method = RequestMethod.GET)
	public ModelAndView companyPositions(@RequestParam final int companyId) {
		final ModelAndView result;
		final Collection<Position> positions;

		positions = this.positionService.findAllByCompany(companyId);

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);
		result.addObject("listPositions", "companyList");
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "position/companyList.do");

		return result;
	}
}
