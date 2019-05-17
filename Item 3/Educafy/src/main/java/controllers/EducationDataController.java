
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
import services.EducationDataService;
import services.RookyService;
import domain.Company;
import domain.Curricula;
import domain.EducationRecord;
import domain.Rooky;

@Controller
@RequestMapping("/educationData")
public class EducationDataController {

	@Autowired
	private EducationDataService	educationDataService;

	@Autowired
	private RookyService			rookyService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private CompanyService			companyService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {

		ModelAndView result;

		final EducationRecord educationData = this.educationDataService.create();

		result = new ModelAndView("educationData/edit");
		result.addObject("educationData", educationData);
		result.addObject("curriculaId", curriculaId);
		result.addObject("message", null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int educationDataId, @RequestParam final int curriculaId) {
		ModelAndView result;
		try {
			final EducationRecord educationData;
			final Rooky rooky = this.rookyService.findByPrincipal();
			educationData = this.educationDataService.findOne(educationDataId);

			Assert.isTrue(this.rookyService.hasEducationData(rooky.getId(), educationDataId), "This personal data is not of your property");

			result = new ModelAndView("educationData/edit");
			result.addObject("educationData", educationData);
			result.addObject("curriculaId", curriculaId);
			result.addObject("message", null);

		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int curriculaId, @Valid final EducationRecord educationData, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors()) {
			result = this.createEditModelAndView(educationData);
			result.addObject("curriculaId", curriculaId);
		} else
			try {
				this.educationDataService.save(educationData, curriculaId);

				final Curricula curricula = this.curriculaService.findOne(curriculaId);
				result = new ModelAndView("curricula/display");
				result.addObject("curricula", curricula);
				result.addObject("curriculaId", curricula.getId());
				result.addObject("message", null);
				result.addObject("buttons", true);

			} catch (final Throwable e) {
				System.out.println(e.getMessage());
				if (e.getMessage().equals("End date must be after start date")) {
					result = new ModelAndView("educationData/edit");
					result.addObject("educationData", educationData);
					result.addObject("curriculaId", curriculaId);
					result.addObject("message", "alert.dates");
				} else
					result = new ModelAndView("redirect:misc/403");

			}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int educationDataId) {

		ModelAndView res;

		final EducationRecord educationData = this.educationDataService.findOne(educationDataId);

		if (educationData != null) {

			res = new ModelAndView("educationData/display");
			res.addObject("educationData", educationData);
			res.addObject("curriculaId", this.curriculaService.findCurriculaByEducationData(educationData.getId()).getId());
			res.addObject("buttons", false);

			final Curricula curricula = this.curriculaService.findOne(this.curriculaService.findCurriculaByEducationData(educationData.getId()).getId());

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

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int educationDataId) {
		ModelAndView result;
		final EducationRecord educationData = this.educationDataService.findOne(educationDataId);
		final Curricula curricula = this.curriculaService.findCurriculaByEducationData(educationDataId);
		this.educationDataService.delete(educationData);

		result = new ModelAndView("curricula/display");
		result.addObject("curricula", curricula);
		result.addObject("message", null);
		result.addObject("buttons", true);

		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationRecord educationData) {
		ModelAndView result;

		result = this.createEditModelAndView(educationData, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final EducationRecord educationData, final String message) {
		ModelAndView result;

		result = new ModelAndView("educationData/edit");
		result.addObject("educationData", educationData);
		result.addObject("message", message);
		if (educationData.getId() != 0)
			result.addObject("curriculaId", this.curriculaService.findCurriculaByEducationData(educationData.getId()).getId());

		return result;

	}
}
