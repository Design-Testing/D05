
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;
import domain.Provider;
import forms.PositionForm;

@Service
@Transactional
public class PositionService {

	@Autowired
	private PositionRepository				positionRepository;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private CompanyService					companyService;

	@Autowired
	private ProblemService					problemService;

	@Autowired
	private ApplicationService				applicationService;

	@Autowired
	private RookyService					rookyService;

	@Autowired
	private ConfigurationParametersService	configParamService;

	@Autowired
	private ProviderService					providerService;

	@Autowired
	private Validator						validator;


	public Position create() {
		final Position position = new Position();
		final Company principal = this.companyService.findByPrincipal();
		position.setCompany(principal);
		final String ticker = this.generateTicker(principal.getCommercialName());
		position.setTicker(ticker);
		position.setMode("DRAFT");
		return position;
	}

	public Collection<Position> findAll() {
		final Collection<Position> result = this.positionRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAllFinalMode() {
		final Collection<Position> result = this.positionRepository.findAllFinalMode();
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAllFinalModeByCompany(final int id) {
		final Collection<Position> result = this.positionRepository.findAllFinalModeByCompany(id);
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAllByCompany() {
		final Company company = this.companyService.findByPrincipal();
		final Collection<Position> result = this.positionRepository.findAllByCompany(company.getId());
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAllByCompany(final int companyId) {
		Assert.isTrue(companyId != 0);
		final Collection<Position> result = this.positionRepository.findAllByCompany(companyId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAppliedByRooky() {
		final Collection<Position> result = this.positionRepository.findAppliedByRooky(this.rookyService.findByPrincipal().getId());
		Assert.notNull(result);
		return result;
	}

	/**
	 * The average, minimum, maximum and standard deviation of the salary offered
	 * 
	 * @author a8081
	 * */
	public Double[] getStatisticsOfSalary() {
		final Double[] res = this.positionRepository.getStatisticsOfSalary();
		Assert.notNull(res);
		return res;
	}

	/**
	 * The best position in terms of salary.
	 * 
	 * @author a8081
	 * */
	public Position[] getBestPosition() {
		final Position[] res = this.positionRepository.getBestPosition();
		Assert.notNull(res);
		return res;
	}

	/**
	 * The worst position in terms of salary.
	 * 
	 * @author a8081
	 * */
	public Position[] getWorstPosition() {
		final Position[] res = this.positionRepository.getWorstPosition();
		Assert.notNull(res);
		return res;
	}

	public Position findOne(final int positionId) {
		Assert.isTrue(positionId != 0);
		final Position res = this.positionRepository.findOne(positionId);
		Assert.notNull(res);
		return res;
	}

	public Position save(final Position position) {
		Assert.notNull(position);
		final Company principal = this.companyService.findByPrincipal();
		final Position result;
		if (position.getId() != 0) {
			Assert.isTrue(position.getCompany().equals(principal));
			Assert.isTrue(position.getMode().equals("DRAFT"), "No puede modificar una posición que ya no esta en DRAFT MODE.");
		}
		result = this.positionRepository.save(position);
		return result;
	}

	public void delete(final Position position) {
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		final Position retrieved = this.findOne(position.getId());
		final Company principal = this.companyService.findByPrincipal();
		Assert.isTrue(retrieved.getCompany().equals(principal));
		final List<Problem> problems = (List<Problem>) this.problemService.findProblemsByPosition(position.getId());
		final List<Application> applications = (List<Application>) this.applicationService.findApplicationByPosition(position.getId());

		/* Se borran todas la applications de esa position */
		if (!applications.isEmpty())
			this.applicationService.deleteInBatch(applications);

		/* Se borran todos los problemas de esa position */
		if (!problems.isEmpty())
			this.problemService.deleteInBatch(problems);

		this.positionRepository.delete(position);
	}

	public Collection<Position> findAllByPrincipal() {
		Collection<Position> res = new ArrayList<>();
		final Actor principal = this.actorService.findByPrincipal();
		res = this.positionRepository.findAllPositionByCompanyId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public List<Position> search(final String keyword) {
		List<Position> result = new ArrayList<>(this.positionRepository.findByKeyword(keyword));
		final int maxResults = this.configParamService.find().getMaxFinderResults();
		if (result.size() > maxResults) {
			Collections.shuffle(result);
			result = result.subList(0, maxResults);
		}
		return result;
	}
	private String generateTicker(final String companyName) {
		String res = "";
		final Integer n1 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n2 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n3 = (int) Math.floor(Math.random() * 9 + 1);
		final Integer n4 = (int) Math.floor(Math.random() * 9 + 1);
		final String word = companyName.substring(0, 4).toUpperCase();
		final String ticker = word + '-' + n1 + n2 + n3 + n4;
		res = ticker;

		final Collection<Position> pos = this.positionRepository.getPositionWithTicker(ticker);
		if (!pos.isEmpty())
			this.generateTicker(companyName);
		return res;
	}

	//	public Collection<Position> findPositions(final String key) {
	//		final Collection<Position> res = this.positionRepository.findPositions(key);
	//		Assert.notNull(res);
	//		return res;
	//	}

	public Position toFinalMode(final int positionId) {
		final Position position = this.findOne(positionId);
		Assert.notNull(position);
		final Company company = this.companyService.findByPrincipal();
		final Position result;
		Assert.isTrue(position.getCompany().equals(company));
		Assert.isTrue(this.problemService.findProblemsByPosition(positionId).size() >= 2, "Position must have 2 or more Problems associated.");
		Assert.isTrue(position.getMode().equals("DRAFT"), "Para poner una position en FINAL MODE debe de estar anteriormente en DRAFT MODE.");
		final Collection<Problem> problems = this.problemService.findProblemsByPosition(positionId);
		for (final Problem p : problems)
			Assert.isTrue(p.getMode().equals("FINAL"), "Los problemas de esta posicion deben estar en modo FINAL");
		position.setMode("FINAL");
		result = this.positionRepository.save(position);
		return result;
	}
	public Position toCancelMode(final int positionId) {
		final Position position = this.findOne(positionId);
		Assert.notNull(position);
		final Company company = this.companyService.findByPrincipal();
		final Position result;
		Assert.isTrue(position.getCompany().equals(company));
		Assert.isTrue(position.getMode().equals("FINAL"), "Para poner una posiciï¿½n en CANCELLED MODE debe de estar en FINAL MODE.");
		position.setMode("CANCELLED");
		result = this.positionRepository.save(position);
		return result;
	}

	public Position reconstruct(final PositionForm positionForm, final BindingResult binding) {
		Position result;

		if (positionForm.getId() == 0)
			result = this.create();
		else
			result = this.findOne(positionForm.getId());

		result.setVersion(positionForm.getVersion());
		result.setTitle(positionForm.getTitle());
		result.setDescription(positionForm.getDescription());
		result.setProfile(positionForm.getProfile());
		result.setDeadline(positionForm.getDeadline());
		result.setSkills(positionForm.getSkills());
		result.setTechnologies(positionForm.getTechnologies());
		result.setSalary(positionForm.getSalary());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public PositionForm constructPruned(final Position position) {
		final PositionForm pruned = new PositionForm();

		pruned.setId(position.getId());
		pruned.setVersion(position.getVersion());
		pruned.setTitle(position.getTitle());
		pruned.setDescription(position.getDescription());
		pruned.setProfile(position.getProfile());
		pruned.setDeadline(position.getDeadline());
		pruned.setSkills(position.getSkills());
		pruned.setTechnologies(position.getTechnologies());
		pruned.setSalary(position.getSalary());

		return pruned;
	}

	public void flush() {
		this.positionRepository.flush();
	}

	public Collection<Position> findPositions(final String keyword, final Double minSalary, final Double maxSalary, final Date minDeadline, final Date maxDeadline) {
		final Collection<Position> res = this.positionRepository.findPositions(keyword, minSalary, maxSalary, minDeadline, maxDeadline);
		Assert.notNull(res);
		return res;
	}

	public Collection<Position> findFreePositionsByAuditor(final int auditorId) {
		final Collection<Position> audited = this.positionRepository.findAuditedPositionsByAuditor(auditorId);
		System.out.println(audited);
		final Collection<Position> res = this.positionRepository.findAllFinal();
		System.out.println(res);
		res.removeAll(audited);
		System.out.println(res);
		Assert.notNull(res);
		return res;
	}

	public boolean exists(final Integer positionId) {
		Assert.isTrue(positionId != 0, "position id cannot be zero");
		return this.positionRepository.exists(positionId);
	}

	public Double[] getStatisticsOfAuditScoreOfPositions() {
		return this.positionRepository.getStatisticsOfAuditScoreOfPositions();
	}

	public Double[] getStatisticsOfAuditScoreOfPosition(final int positionId) {
		return this.positionRepository.getStatisticsOfAuditScoreOfPosition(positionId);
	}

	public Double getAvgSalaryOfPositionsHighestAvgAuditScore() {
		return this.positionRepository.getAvgSalaryOfPositionsHighestAvgAuditScore();
	}

	public Collection<Position> positionsAvailableProvider() {
		Collection<Position> myPositions;
		Collection<Position> positions;
		final Provider s = this.providerService.findByPrincipal();
		positions = this.findAllFinalMode();
		myPositions = this.findAllPositionByProvider(s);
		positions.removeAll(myPositions);
		return positions;
	}

	public Collection<Position> findAllPositionByProvider(final Provider s) {
		Collection<Position> res;
		res = this.positionRepository.findAllParadeByProvider(s.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}
}
