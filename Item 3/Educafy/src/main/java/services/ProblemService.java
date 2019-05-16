
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ProblemRepository;
import security.Authority;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemRepository	problemRepository;

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ActorService		actorService;


	//Metodos CRUD

	public Problem create() {
		final Problem problem = new Problem();
		final Company company = new Company();
		final Collection<String> attachments = new ArrayList<>();
		problem.setCompany(company);
		problem.setAttachments(attachments);
		return problem;
	}

	public Collection<Problem> findAll() {
		final Collection<Problem> problems = this.problemRepository.findAll();
		Assert.notNull(problems);
		return problems;
	}

	public Problem findOne(final int problemId) {
		Assert.isTrue(problemId != 0);
		final Problem result = this.problemRepository.findOne(problemId);
		Assert.notNull(result);
		return result;
	}

	public Problem save(final Problem problem, final int positionId) {
		Assert.notNull(problem);
		Assert.isTrue(positionId != 0);
		final Problem res;
		final Company company = this.companyService.findByPrincipal();
		this.actorService.checkAuthority(company, Authority.COMPANY);
		if (problem.getId() == 0) {
			final Position position = this.positionService.findOne(positionId);
			final Collection<Position> positions = this.positionService.findAllByCompany();
			Assert.isTrue(positions.contains(position));
			problem.setPosition(position);
			problem.setCompany(company);
			problem.setMode("DRAFT");
		} else {
			final Position position = this.positionService.findOne(positionId);
			Assert.isTrue(problem.getMode().equals("DRAFT"), "No puedes modificar un problem que estï¿½ en FINAL MODE");
			Assert.isTrue(problem.getCompany().equals(company), "No puede modificar un problem que no le pertenezca");
			Assert.isTrue(problem.getPosition().equals(position), "Ese problema no pertenece a esa posición.");

		}
		res = this.problemRepository.save(problem);
		return res;
	}

	public void delete(final Problem problem) {
		Assert.notNull(problem);
		Assert.isTrue(problem.getId() != 0);
		final Company company = this.companyService.findByPrincipal();
		Assert.isTrue(company.equals(problem.getCompany()), "No puedes borrar un problema que no le pertenezca.");
		final Problem retrieved = this.findOne(problem.getId());
		Assert.isTrue(retrieved.equals(problem));
		Assert.isTrue(this.problemRepository.exists(problem.getId()));
		final Collection<Application> applications = this.applicationService.findAllByProblem(retrieved.getId());
		Assert.isTrue(applications.isEmpty(), "No puedes borrar este problema, ya que tiene solicitudes asociadas.");
		this.problemRepository.delete(retrieved.getId());

	}

	public Problem toFinalMode(final int problemId) {
		final Problem problem = this.findOne(problemId);
		final Problem result;
		final Company company = this.companyService.findByPrincipal();
		Assert.isTrue(problem.getCompany() == company, "Actor who want to edit parade mode to FINAL is not his owner");
		Assert.isTrue(problem.getMode().equals("DRAFT"), "To set final mode, parade must be in draft mode");
		problem.setMode("FINAL");
		result = this.problemRepository.save(problem);
		return result;
	}

	public Problem findProblemByApplication(final int applicationId) {
		Problem res;
		res = this.problemRepository.findProblemByApplication(applicationId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Problem> findProblemsByPosition(final int positionId) {
		Collection<Problem> res = new ArrayList<>();
		res = this.problemRepository.findProblemsByPosition(positionId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Problem> findFinalProblemsByPosition(final int positionId) {
		Collection<Problem> res = new ArrayList<>();
		res = this.problemRepository.findFinalProblemsByPosition(positionId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Problem> findProblemByCompany() {
		final Company company = this.companyService.findByPrincipal();
		Collection<Problem> res = new ArrayList<>();
		res = this.problemRepository.findProblemsByCompany(company.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Problem> findProblemsByPositionAndRooky(final Integer positionId, final Integer hackerUAId) {
		Collection<Problem> res = new ArrayList<>();
		res = this.problemRepository.findProblemsByPositionAndRooky(positionId, hackerUAId);
		return res;
	}

	public void deleteInBatch(final Collection<Problem> problems) {
		this.problemRepository.deleteInBatch(problems);
	}

	public void flush() {
		this.problemRepository.flush();
	}

}
