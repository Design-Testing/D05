
package controllers;

import javax.validation.Valid;

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
import services.CompanyService;
import services.CurriculaService;
import services.PositionDataService;
import services.RookyService;
import domain.Company;
import domain.Curricula;
import domain.PositionData;
import domain.Rooky;

@Controller
@RequestMapping("/positionData")
public class PositionDataController {

	@Autowired
	private PositionDataService	positionDataService;

	@Autowired
	private RookyService		rookyService;

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private CompanyService		companyService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {

		ModelAndView result;

		final PositionData positionData = this.positionDataService.create();

		result = new ModelAndView("positionData/edit");
		result.addObject("positionData", positionData);
		result.addObject("curriculaId", curriculaId);
		result.addObject("message", null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionDataId, @RequestParam final int curriculaId) {
		ModelAndView result;
		try {
			final PositionData positionData;
			final Rooky rooky = this.rookyService.findByPrincipal();
			positionData = this.positionDataService.findOne(positionDataId);

			Assert.isTrue(this.rookyService.hasPositionData(rooky.getId(), positionDataId), "This personal data is not of your property");

			result = new ModelAndView("positionData/edit");
			result.addObject("positionData", positionData);
			result.addObject("curriculaId", curriculaId);
			result.addObject("message", null);

		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int curriculaId, @Valid final PositionData positionData, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors()) {
			result = this.createEditModelAndView(positionData);
			result.addObject("curriculaId", curriculaId);
		} else
			try {
				this.positionDataService.save(positionData, curriculaId);

				final Curricula curricula = this.curriculaService.findOne(curriculaId);
				result = new ModelAndView("curricula/display");
				result.addObject("curricula", curricula);
				result.addObject("curriculaId", curricula.getId());
				result.addObject("message", null);
				result.addObject("buttons", true);

			} catch (final Throwable e) {
				if (e.getMessage().equals("End date must be after start date"))
					result = this.createEditModelAndView(positionData, "alert.dates");
				else
					result = new ModelAndView("redirect:misc/403");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int positionDataId) {
		ModelAndView result;
		final PositionData positionData = this.positionDataService.findOne(positionDataId);
		final Curricula curricula = this.curriculaService.findCurriculaByPositionData(positionDataId);
		this.positionDataService.delete(positionData);

		result = new ModelAndView("curricula/display");
		result.addObject("curricula", curricula);
		result.addObject("message", null);
		result.addObject("buttons", true);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionDataId) {

		ModelAndView res;

		final PositionData positionData = this.positionDataService.findOne(positionDataId);

		if (positionData != null) {

			res = new ModelAndView("positionData/display");
			res.addObject("positionData", positionData);
			res.addObject("curriculaId", this.curriculaService.findCurriculaByPositionData(positionData.getId()).getId());
			res.addObject("buttons", false);

			final Curricula curricula = this.curriculaService.findOne(this.curriculaService.findCurriculaByPositionData(positionData.getId()).getId());

			final UserAccount logged = LoginService.getPrincipal();

			final Authority authRooky = new Authority();
			authRooky.setAuthority(Authority.ROOKY);
			final Authority authCompany = new Authority();
			authCompany.setAuthority(Authority.COMPANY);
			if (logged.getAuthorities().contains(authRooky)) {
				final Rooky rooky = this.rookyService.findByPrincipal();
				if (curricula.getRooky() != null)
					Assert.isTrue(curricula.getRooky().equals(rooky));
				else if (curricula.getRooky() == null)
					Assert.isTrue(this.rookyService.findRookyByCopyCurricula(curricula.getId()).equals(rooky));
			} else if (logged.getAuthorities().contains(authCompany)) {
				final Company company = this.companyService.findByPrincipal();
				Assert.isTrue(this.curriculaService.findCurriculasByCompany(company.getId()).contains(curricula));
			}

		} else
			res = new ModelAndView("redirect:misc/403");

		return res;

	}

	protected ModelAndView createEditModelAndView(final PositionData positionData) {
		ModelAndView result;

		result = this.createEditModelAndView(positionData, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PositionData positionData, final String message) {
		ModelAndView result;

		result = new ModelAndView("positionData/edit");
		result.addObject("positionData", positionData);
		result.addObject("message", message);
		if (positionData.getId() != 0)
			result.addObject("curriculaId", this.curriculaService.findCurriculaByPositionData(positionData.getId()).getId());

		return result;

	}
}
