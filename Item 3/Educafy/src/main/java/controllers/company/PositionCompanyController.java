
package controllers.company;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.PositionService;
import services.ProblemService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Company;
import domain.Position;
import domain.Problem;
import domain.Sponsorship;
import forms.PositionForm;

@Controller
@RequestMapping("/position/company")
public class PositionCompanyController extends AbstractController {

	@Autowired
	private PositionService		positionService;

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ProblemService		problemService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final PositionForm position = new PositionForm();

		result = this.createEditModelAndView(position);
		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		final ModelAndView result;
		final Position position;
		final Company company;

		position = this.positionService.findOne(positionId);
		company = this.companyService.findByPrincipal();
		final Collection<Problem> problems = this.problemService.findProblemsByPosition(positionId);

		if (position != null && (position.getCompany().getId() == company.getId() || !position.getMode().equals("DRAFT"))) {
			result = new ModelAndView("position/display");
			result.addObject("position", position);
			result.addObject("company", company);
			result.addObject("ownerId", position.getCompany().getId());
			result.addObject("rol", "company");
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

	@RequestMapping(value = "/myPositions", method = RequestMethod.GET)
	public ModelAndView myPositions() {
		final ModelAndView result;
		final Collection<Position> positions;

		positions = this.positionService.findAllByCompany();

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);
		result.addObject("lang", this.lang);
		result.addObject("rol", "company");
		result.addObject("listPositions", "myPositions");
		result.addObject("requetURI", "position/company/myPositions.do");
		result.addObject("principalID", this.companyService.findByPrincipal().getId());

		return result;
	}

	// TO FINAL MODE --------------------------------------------------------

	@RequestMapping(value = "/finalMode", method = RequestMethod.GET)
	public ModelAndView finalMode(@RequestParam final int positionId) {
		ModelAndView result;
		final Position position = this.positionService.findOne(positionId);

		if (position == null) {
			result = this.myPositions();
			result.addObject("msg", "position.final.mode.error");
		} else
			try {
				this.positionService.toFinalMode(positionId);
				result = this.myPositions();
			} catch (final Throwable oops) {
				String errormsg = "position.final.mode.error";
				result = this.myPositions();
				if (this.problemService.findFinalProblemsByPosition(positionId).size() < 2)
					errormsg = "position.mode.no.problems";
				if (!position.getMode().equals("DRAFT"))
					errormsg = "position.final.no.draft";
				result.addObject("msg", errormsg);
			}

		return result;
	}
	// TO CANCELLED MODE --------------------------------------------------------

	@RequestMapping(value = "/cancelledMode", method = RequestMethod.GET)
	public ModelAndView cancelledMode(@RequestParam final int positionId) {
		ModelAndView result;
		final Position position = this.positionService.findOne(positionId);

		if (position == null) {
			result = new ModelAndView("position/error");
			result.addObject("msg", "position.cancel.mode.error");
		} else
			try {
				this.positionService.toCancelMode(positionId);
				result = this.myPositions();
			} catch (final Throwable oops) {
				String errormsg = "position.cancel.mode.error";
				if (!position.getMode().equals("FINAL"))
					errormsg = "position.cancel.no.final";
				result = this.myPositions();
				result.addObject("msg", errormsg);
			}

		return result;
	}
	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		ModelAndView result;
		Position position;

		position = this.positionService.findOne(positionId);

		final Company company = this.companyService.findByPrincipal();

		if ((position.getMode().equals("DRAFT") && position.getCompany().equals(company)))
			result = this.createEditModelAndView(this.positionService.constructPruned(position));
		else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PositionForm positionForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(positionForm);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				final Position position = this.positionService.reconstruct(positionForm, binding);
				this.positionService.save(position);
				result = this.myPositions();
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(positionForm, "commit.position.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(positionForm, "commit.position.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}
	// DELETE --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int positionId) {
		final Position position = this.positionService.findOne(positionId);
		this.positionService.delete(position);
		return this.myPositions();

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PositionForm position) {
		ModelAndView result;
		result = this.createEditModelAndView(position, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionForm position, final String messageCode) {
		Assert.notNull(position);
		final ModelAndView result;

		result = new ModelAndView("position/edit");
		result.addObject("positionForm", position); //this.constructPruned(position)
		result.addObject("message", messageCode);

		return result;
	}

}
