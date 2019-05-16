
package controllers.administrator;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.CompanyService;
import services.ConfigurationParametersService;
import services.CurriculaService;
import services.FinderService;
import services.MessageService;
import services.PositionService;
import services.ProviderService;
import services.RookyService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Company;
import domain.Provider;

@Controller
@RequestMapping(value = "/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	private PositionService					positionService;

	@Autowired
	private MessageService					messageService;

	@Autowired
	private RookyService					rookyService;

	@Autowired
	private CompanyService					companyService;

	@Autowired
	private FinderService					finderService;

	@Autowired
	private CurriculaService				curriculaService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private SponsorshipService				sponsorshipService;

	@Autowired
	private ProviderService					providerService;

	@Autowired
	private AdministratorService			administratorService;


	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ModelAndView statistics() {
		this.administratorService.findByPrincipal();
		final ModelAndView result;
		result = new ModelAndView("dashboard/statistics"); // -> list.jsp
		result.addObject("requestURI", "dashboard/admnistrator/statistics.do");

		// Finder
		final Double[] finderStatistics = this.finderService.getStatisticsOfPositionsPerFinder();
		final Double ratioFinders = this.finderService.findRatioFinders();
		result.addObject("averageResults", finderStatistics[0]);
		result.addObject("minResults", finderStatistics[1]);
		result.addObject("maxResults", finderStatistics[2]);
		result.addObject("desviationResults", finderStatistics[3]);
		result.addObject("ratioFinders", ratioFinders);

		// Curriculum
		final Double[] curriculaStatistics = this.curriculaService.getStatisticsOfCurriculaPerRooky();
		result.addObject("averageCurricula", curriculaStatistics[0]);
		result.addObject("minCurricula", curriculaStatistics[1]);
		result.addObject("maxCurricula", curriculaStatistics[2]);
		result.addObject("desviationCurricula", curriculaStatistics[3]);

		// Positions per company
		final Double[] positionsPerCompany = this.companyService.getStatisticsOfPositionsPerCompany();
		result.addObject("averagePositions", positionsPerCompany[0]);
		result.addObject("minPositions", positionsPerCompany[1]);
		result.addObject("maxPositions", positionsPerCompany[2]);
		result.addObject("desviationPositions", positionsPerCompany[3]);
		result.addObject("companyMorePositions", this.companyService.getCompaniesMorePositions().toArray()[0]);
		result.addObject("companiesMorePositions", this.companyService.getCompaniesMorePositions());
		result.addObject("best", this.positionService.getBestPosition()[0]);
		result.addObject("worst", this.positionService.getWorstPosition()[0]);

		// Applications per rooky
		final Double[] applicationsPerRooky = this.rookyService.getStatisticsOfApplicationsPerRooky();
		result.addObject("averageRooky", applicationsPerRooky[0]);
		result.addObject("minRooky", applicationsPerRooky[1]);
		result.addObject("maxRooky", applicationsPerRooky[2]);
		result.addObject("desviationRooky", applicationsPerRooky[3]);
		result.addObject("rookyMoreApplications", this.rookyService.getRookysMoreApplications().toArray()[0]);
		result.addObject("rookysMoreApplications", this.rookyService.getRookysMoreApplications());

		// Position audit score
		final Double[] positionScore = this.positionService.getStatisticsOfAuditScoreOfPositions();
		result.addObject("averagePositionScore", positionScore[0]);
		result.addObject("minPositionScore", positionScore[1]);
		result.addObject("maxPositionScore", positionScore[2]);
		result.addObject("desviationPositionScore", positionScore[3]);
		final Double avgSalary = this.positionService.getAvgSalaryOfPositionsHighestAvgAuditScore();
		result.addObject("avgSalary", avgSalary);

		// Company score
		final Double[] companyScore = this.companyService.getStatisticsOfPositionsPerCompany();
		result.addObject("averageCompanyScore", companyScore[0]);
		result.addObject("minCompanyScore", companyScore[1]);
		result.addObject("maxCompanyScore", companyScore[2]);
		result.addObject("desviationCompanyScore", companyScore[3]);
		final Collection<Company> companiesHighestScore = this.companyService.getCompaniesHighestAuditScore();
		result.addObject("companiesHighestScore", companiesHighestScore);

		// Sponsorship
		final Double[] sponsorshipPerPosition = this.sponsorshipService.getStatisticsOfSponsorshipPerPosition();
		result.addObject("averageSponsorshipPerPosition", sponsorshipPerPosition[0]);
		result.addObject("minSponsorshipPerPosition", sponsorshipPerPosition[1]);
		result.addObject("maxSponsorshipPerPosition", sponsorshipPerPosition[2]);
		result.addObject("desviationSponsorshipPerPosition", sponsorshipPerPosition[3]);
		final Double[] sponsorshipPerProvider = this.sponsorshipService.getStatisticsOfSponsorshipPerProvider();
		result.addObject("averageSponsorshipPerProvider", sponsorshipPerProvider[0]);
		result.addObject("minSponsorshipPerProvider", sponsorshipPerProvider[1]);
		result.addObject("maxSponsorshipPerProvider", sponsorshipPerProvider[2]);
		result.addObject("desviationSponsorshipPerProvider", sponsorshipPerProvider[3]);
		final Collection<Provider> providersTenPercent = this.providerService.findTenPerCentMoreAppsThanAverage();
		result.addObject("providersTenPercent", providersTenPercent);

		return result;

	}
	@RequestMapping(value = "/dataBreach", method = RequestMethod.GET)
	public ModelAndView launchDeactivate() {
		ModelAndView result;

		this.messageService.dataBreachMessage();

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

		result.addObject("alert", "data.breach.notified");

		return result;

	}

}
