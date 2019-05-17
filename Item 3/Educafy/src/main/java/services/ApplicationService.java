
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import security.Authority;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Curriculum;
import domain.Position;
import domain.Problem;
import domain.Rooky;
import forms.ApplicationForm;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository	applicationRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private RookyService			hackerService;

	@Autowired
	private ProblemService			problemService;

	@Autowired
	private Validator				validator;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private CurriculaService		curriculaService;


	public Application create(final int positionId) {
		final Application application = new Application();
		final Rooky hacker = this.hackerService.findByPrincipal();
		final Position position = this.positionService.findOne(positionId);

		//Se asigna un problema aleatorio del conjunto de problemas que posee esa position.
		Assert.isTrue(!this.problemsFree(positionId, hacker).isEmpty(), "Ya tiene solicitudes a todos los problemas posibles.");
		final Problem assigned = this.problemAssign(positionId, hacker);
		application.setProblem(assigned);
		application.setStatus("PENDING");
		final Date moment = new Date(System.currentTimeMillis() - 1);
		application.setMoment(moment);
		application.setRooky(hacker);
		application.setPosition(position);

		return application;
	}

	public Collection<Application> findAll() {
		Collection<Application> res = new ArrayList<>();
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isRooky = this.actorService.checkAuthority(principal, Authority.ROOKY);
		final Boolean isCompany = this.actorService.checkAuthority(principal, Authority.COMPANY);

		if (isRooky)
			res = this.applicationRepository.findAllByRookyId(principal.getUserAccount().getId());
		else if (isCompany)
			res = this.applicationRepository.findAllByCompanyId(principal.getUserAccount().getId());
		//Si salta puede ser un Admin
		Assert.notNull(res);
		return res;
	}

	public Application findOne(final int applicationId) {
		Assert.isTrue(applicationId != 0);
		final Application res = this.applicationRepository.findOne(applicationId);
		Assert.notNull(res);
		return res;
	}

	public Application save(final Application application, final int positionId) {
		Assert.notNull(application);
		Assert.isTrue(positionId != 0);
		final Actor principal = this.actorService.findByPrincipal();
		final Application result;
		final Boolean isRooky = this.actorService.checkAuthority(principal, Authority.ROOKY);

		if (isRooky) {
			if (application.getId() != 0) {
				Assert.isTrue(application.getStatus().equals("PENDING"), "No puede actualizar una solicitud que no est� en estado PENDING.");
				Assert.isTrue(application.getRooky() == principal, "No puede actualizar una solicitud que no le pertenece.");
				// Assert.isTrue(application.getExplanation() != "", "Debe adjuntar una explicaci�n de su soluci�n.");
				// Assert.isTrue(application.getLink() != "", "Debe adjuntar un link de su soluci�n.");
				application.setStatus("SUBMITTED");
				final Date submitMoment = new Date(System.currentTimeMillis() - 1);
				application.setSubmitMoment(submitMoment);
			}
		} else { //COMPANY
			Assert.isTrue(application.getPosition().getCompany() == this.companyService.findByPrincipal(), "No puede actualizar una solicitud que no le pertenece.");
			Assert.isTrue(application.getStatus() == "SUBMITTED");
		}
		result = this.applicationRepository.save(application);
		return result;
	}
	public void delete(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));
		this.applicationRepository.delete(application);
	}

	/* ========================= OTHER METHODS =========================== */

	public Application acceptApplication(final int applicationId) {
		final Application application = this.findOne(applicationId);
		final Application result;
		final Company company = this.companyService.findByPrincipal();

		Assert.isTrue(application.getPosition().getCompany() == company, "No puede actualizar una solicitud que no le pertenece.");
		Assert.isTrue(application.getStatus().equals("SUBMITTED"), "No puede aceptar una solicitud que su estado sea distinto a Submitted");
		application.setStatus("ACCEPTED");
		result = this.applicationRepository.save(application);
		return result;
	}

	public Application rejectApplication(final int applicationId) {
		final Application application = this.findOne(applicationId);
		final Application result;
		final Company company = this.companyService.findByPrincipal();

		Assert.isTrue(application.getPosition().getCompany() == company, "No puede actualizar una solicitud que no le pertenece.");
		Assert.isTrue(application.getStatus().equals("SUBMITTED"), "No puede aceptar una solicitud que su estado sea distinto a Submitted");
		application.setStatus("REJECTED");
		result = this.applicationRepository.save(application);
		return result;
	}

	public Application apply(final int positionId, final int curriculaId) {
		Assert.isTrue(positionId != 0);
		Assert.isTrue(this.actorService.checkAuthority(this.actorService.findByPrincipal(), Authority.ROOKY));
		final Application application = this.create(positionId);
		Assert.notNull(application);
		final Curriculum curricula = this.curriculaService.findOne(curriculaId);
		application.setCurricula(curricula);
		final Application retreived = this.applicationRepository.save(application);
		return retreived;
	}

	public Collection<Application> findAllSubmittedByCompany() {
		final Company principal = this.companyService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllSubmittedByCompany(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findAllAcceptedByCompany() {
		final Company principal = this.companyService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllAcceptedByCompany(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findAllRejectedByCompany() {
		final Company principal = this.companyService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllRejectedByCompany(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findAllPendingByRooky() {
		final Rooky principal = this.hackerService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllPendingByRooky(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findAllSubmittedByRooky() {
		final Rooky principal = this.hackerService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllSubmittedByRooky(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findAllAcceptedByRooky() {
		final Rooky principal = this.hackerService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllAcceptedByRooky(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findAllRejectedByRooky() {
		final Rooky principal = this.hackerService.findByPrincipal();
		final Collection<Application> res = this.applicationRepository.findAllRejectedByRooky(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findApplicationByPosition(final Integer positionId) {
		Collection<Application> res;
		res = this.applicationRepository.findApplicationsByPosition(positionId);
		return res;
	}

	public Collection<Application> findAllByProblem(final int problemId) {
		final Collection<Application> res = this.applicationRepository.findAllByProblem(problemId);
		Assert.notNull(res);
		return res;
	}

	public void deleteInBatch(final Collection<Application> applications) {
		this.applicationRepository.deleteInBatch(applications);
	}

	public Application reconstruct(final ApplicationForm applicationForm, final BindingResult binding) {
		Application result;
		Assert.isTrue(applicationForm.getId() != 0);
		result = this.findOne(applicationForm.getId());

		result.setId(applicationForm.getId());
		result.setVersion(applicationForm.getVersion());
		result.setExplanation(applicationForm.getExplanation());
		result.setLink(applicationForm.getLink());

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	public Problem problemAssign(final int positionId, final Rooky hacker) {

		final List<Problem> free = (List<Problem>) this.problemsFree(positionId, hacker);
		final Integer numRandom = (int) (Math.random() * (free.size() - 1));
		final Problem assigned = free.get(numRandom);

		return assigned;

	}

	public Collection<Problem> problemsFree(final int positionId, final Rooky hacker) {
		final List<Problem> allProblems = (List<Problem>) this.problemService.findProblemsByPosition(positionId);
		final List<Problem> problems = (List<Problem>) this.problemService.findProblemsByPositionAndRooky(positionId, hacker.getUserAccount().getId());

		allProblems.removeAll(problems);

		return allProblems;
	}

	public void flush() {
		this.applicationRepository.flush();
	}

}
