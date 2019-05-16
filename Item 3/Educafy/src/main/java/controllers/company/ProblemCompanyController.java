
package controllers.company;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CompanyService;
import services.PositionService;
import services.ProblemService;
import controllers.AbstractController;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;

@Controller
@RequestMapping("/problem/company")
public class ProblemCompanyController extends AbstractController {

	@Autowired
	private ProblemService				problemService;

	@Autowired
	private PositionService				positionService;

	@Autowired
	private CompanyService				companyService;

	@Autowired
	private PositionCompanyController	positionCompanyController;

	@Autowired
	private ApplicationService			applicationService;


	//Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		ModelAndView result;
		Problem problem;
		final Position position = this.positionService.findOne(positionId);
		if (position.getMode().equals("DRAFT")) {
			problem = this.problemService.create();
			result = this.createEditModelAndView(problem, positionId);
			result.addObject("problem", problem);
			result.addObject("positionId", positionId);
		} else {
			result = new ModelAndView("problem/error");
			result.addObject("ok", "Cannot create a new problem in position whose mode is not DRAFT.");
		}

		return result;
	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int problemId) {

		ModelAndView res;

		final Problem problem = this.problemService.findOne(problemId);
		final Collection<Application> applications = this.applicationService.findAllByProblem(problemId);

		final Company principal = this.companyService.findByPrincipal();
		if (problem != null && (problem.getCompany().getId() == principal.getId())) {
			res = new ModelAndView("problem/display");
			res.addObject("problem", problem);
			res.addObject("applications", applications);
			res.addObject("position", problem.getPosition());
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}
	//List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Company company = this.companyService.findByPrincipal();
		final Collection<Problem> problems = this.problemService.findProblemByCompany();

		res = new ModelAndView("problem/list");
		res.addObject("company", company);
		res.addObject("problems", problems);
		res.addObject("requestURI", "/problem/company/list");

		return res;
	}

	// EDIT 
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int problemId, @RequestParam final int positionId) {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.findOne(problemId);

		final Company company = this.companyService.findByPrincipal();

		if ((problem.getMode().equals("DRAFT") && problem.getCompany().equals(company)))
			result = this.createEditModelAndView(problem, positionId);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Problem problem, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String paramPositionId;

		paramPositionId = request.getParameter("positionId");
		final Integer positionId = paramPositionId.isEmpty() ? null : Integer.parseInt(paramPositionId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(problem, positionId);
		else
			try {
				this.problemService.save(problem, positionId);
				result = this.positionCompanyController.display(positionId);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(problem, "problem.commit.error", positionId);
			}

		return result;
	}

	// TO FINAL MODE 

	@RequestMapping(value = "/finalMode", method = RequestMethod.GET)
	public ModelAndView finalMode(@RequestParam final int problemId) {
		final ModelAndView result;
		final Problem problem = this.problemService.findOne(problemId);
		final Collection<Problem> problems = this.problemService.findProblemByCompany();
		Assert.isTrue(problems.contains(problem));
		if (problem.getMode().equals("DRAFT")) {
			this.problemService.toFinalMode(problemId);
			result = this.positionCompanyController.display(problem.getPosition().getId());
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	//DELETE

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int problemId, final HttpServletRequest request) {
		ModelAndView result;
		String paramPositionId;

		paramPositionId = request.getParameter("positionId");
		final Integer positionId = paramPositionId.isEmpty() ? null : Integer.parseInt(paramPositionId);
		final Collection<Application> applications = this.applicationService.findAllByProblem(problemId);
		if (applications.isEmpty()) {
			final Problem problem = this.problemService.findOne(problemId);
			this.problemService.delete(problem);
			result = new ModelAndView("redirect:/position/company/display.do?positionId=" + positionId);
			result.addObject("trace", "problem.deleted");
			result.addObject("problemdeleted", problem.getTitle());
		} else {
			result = new ModelAndView("problem/error");
			result.addObject("ok", "not.empty.applications");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Problem problem, @RequestParam final int positionId) {
		ModelAndView result;

		result = this.createEditModelAndView(problem, null, positionId);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Problem problem, final String message, @RequestParam final int positionId) {
		ModelAndView result;

		result = new ModelAndView("problem/edit");
		result.addObject("problem", problem);
		result.addObject("message", message);
		result.addObject("positionId", positionId);

		return result;

	}

}
