
package controllers.company;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CompanyService;
import controllers.AbstractController;
import domain.Application;
import domain.Company;

@Controller
@RequestMapping("/application/company")
public class ApplicationCompanyController extends AbstractController {

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CompanyService		companyService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int applicationId) {
		final ModelAndView result;
		final Application application;
		final Company company;

		application = this.applicationService.findOne(applicationId);
		company = this.companyService.findByPrincipal();

		result = new ModelAndView("application/display");
		result.addObject("company", company);
		result.addObject("application", application);
		result.addObject("rol", "company");

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.findAll();

		String listApplications;
		String rol;

		listApplications = "list";
		rol = "company";

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "application/company/list.do");
		result.addObject("listApplications", listApplications);
		result.addObject("principalID", this.companyService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST SUBMITTED --------------------------------------------------------

	@RequestMapping(value = "/listSubmitted", method = RequestMethod.GET)
	public ModelAndView listSubmitted() {
		final ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.findAllSubmittedByCompany();

		String listApplications;
		String rol;

		listApplications = "listSubmitted";
		rol = "company";

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "application/company/listSubmitted.do");
		result.addObject("listApplications", listApplications);
		result.addObject("principalID", this.companyService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST REJECTED --------------------------------------------------------

	@RequestMapping(value = "/listRejected", method = RequestMethod.GET)
	public ModelAndView listRejected() {
		final ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.findAllRejectedByCompany();

		String listApplications;
		String rol;

		listApplications = "listRejected";
		rol = "company";

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "application/company/listRejected.do");
		result.addObject("listApplications", listApplications);
		result.addObject("principalID", this.companyService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST ACCEPTED --------------------------------------------------------

	@RequestMapping(value = "/listAccepted", method = RequestMethod.GET)
	public ModelAndView listAccepted() {
		final ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.findAllAcceptedByCompany();

		String listApplications;
		String rol;

		listApplications = "listAccepted";
		rol = "company";

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "application/company/listAccepted.do");
		result.addObject("listApplications", listApplications);
		result.addObject("principalID", this.companyService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// ACCEPT APPLICATION --------------------------------------------------------

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int applicationId) {
		final ModelAndView result;
		final Application application = this.applicationService.findOne(applicationId);

		if (application == null || !application.getStatus().equals("SUBMITTED")) {
			result = new ModelAndView("application/error");
			result.addObject("msg", "application.accept.error");
		} else {
			this.applicationService.acceptApplication(applicationId);
			result = this.listAccepted();
		}

		return result;
	}
	// REJECT PARADE --------------------------------------------------------

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int applicationId) {
		ModelAndView result;
		final Application application = this.applicationService.findOne(applicationId);

		if (application == null || !application.getStatus().equals("SUBMITTED")) {
			result = new ModelAndView("application/error");
			result.addObject("msg", "application.reject.error");
		} else {
			this.applicationService.rejectApplication(applicationId);
			result = this.listRejected();
		}

		return result;
	}

}
