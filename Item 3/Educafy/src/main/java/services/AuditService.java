
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditRepository;
import domain.Audit;
import domain.Auditor;
import domain.Position;
import forms.AuditForm;

@Service
@Transactional
public class AuditService {

	@Autowired
	private AuditRepository	auditRepository;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private Validator		validator;

	@Autowired
	private PositionService	positionService;


	public Audit create(final int positionId) {
		final Audit audit = new Audit();
		final Auditor auditor = this.auditorService.findByPrincipal();
		final Position position = this.positionService.findOne(positionId);
		Assert.isTrue(position.getMode().equals("FINAL"), "the position is not final");

		audit.setIsDraft(true);
		final Date moment = new Date(System.currentTimeMillis() - 1);
		audit.setMoment(moment);
		audit.setAuditor(auditor);
		audit.setPosition(position);

		return audit;
	}

	public Collection<Audit> findAll() {
		final Collection<Audit> res = this.auditRepository.findAll();
		Assert.notNull(res, "find all audits return null");
		return res;
	}

	public Audit findOne(final int auditId) {
		Assert.isTrue(auditId != 0);
		final Audit res = this.auditRepository.findOne(auditId);
		Assert.notNull(res);
		return res;
	}

	public Audit save(final Audit audit, final int positionId) {
		Assert.notNull(audit);
		Assert.isTrue(positionId != 0);
		final Auditor principal = this.auditorService.findByPrincipal();
		final Audit result;
		final Position position = this.positionService.findOne(positionId);
		final Date submitMoment = new Date(System.currentTimeMillis() - 1);
		audit.setMoment(submitMoment);
		Assert.isTrue(position.getMode().equals("FINAL"), "the position is not final");

		if (audit.getId() != 0) {
			Assert.isTrue(audit.getAuditor().getId() == principal.getId(), "No puede actualizar una solicitud que no le pertenece.");
			Assert.isTrue(audit.getIsDraft(), "You can not edit a audit that is not in draft mode");
			audit.setIsDraft(true); //para pasarla a modo final se hace con el método toFinalMode
			audit.setPosition(position);

		} else {
			final Collection<Position> freePositions = this.positionService.findFreePositionsByAuditor(principal.getId());
			Assert.isTrue(freePositions.contains(position), "already audited this position");
			audit.setIsDraft(true);
			audit.setAuditor(principal);
			audit.setPosition(position);
		}
		result = this.auditRepository.save(audit);
		Assert.notNull(result, "save audit returns null");
		return result;
	}

	public void delete(final Audit audit) {
		Assert.notNull(audit, "audit parameter in delete audit is null");
		Assert.isTrue(audit.getId() != 0, "trying to delete an audit with audit 0");
		Assert.isTrue(this.auditRepository.exists(audit.getId()), "trying to delte an audit that does not exist");
		Assert.isTrue(audit.getIsDraft(), "you can not delete an audit that is not in draft mode");
		final Auditor principal = this.auditorService.findByPrincipal();
		Assert.isTrue(audit.getAuditor().getId() == principal.getId(), "You can no delete an audit that is not yours");
		this.auditRepository.delete(audit);
	}

	/* ========================= OTHER METHODS =========================== */

	public Collection<Audit> findAllByAuditor(final int auditorId) {
		final Auditor logged = this.auditorService.findByPrincipal();
		Assert.isTrue(logged.getId() == auditorId, "you can not see another auditor's audits");
		final Collection<Audit> res = this.auditRepository.findAllByAuditor(auditorId);
		Assert.notNull(res, "find all audits by auditor returns null");
		return res;
	}

	public Collection<Audit> findAllDraftByAuditor(final int auditorId) {
		final Auditor logged = this.auditorService.findByPrincipal();
		Assert.isTrue(logged.getId() == auditorId, "you can not see another auditor's audits");
		final Collection<Audit> res = this.auditRepository.findAllDraftByAuditor(auditorId);
		Assert.notNull(res, "find all draft audits by auditor returns null");
		return res;
	}

	public Collection<Audit> findAllFinalByAuditor(final int auditorId) {
		final Auditor logged = this.auditorService.findByPrincipal();
		Assert.isTrue(logged.getId() == auditorId, "you can not see another auditor's audits");
		final Collection<Audit> res = this.auditRepository.findAllFinalByAuditor(auditorId);
		Assert.notNull(res, "find all final audits by auditor returns null");
		return res;
	}

	public Collection<Audit> findAllByPosition(final int positionId) {
		final Collection<Audit> res = this.auditRepository.findAllByPosition(positionId);
		Assert.notNull(res, "find all audits by position returns null");
		return res;
	}

	public Collection<Audit> findAllByCompany(final int companyId) {
		Assert.isTrue(companyId != 0);
		final Collection<Audit> res = this.auditRepository.findAllByCompany(companyId);
		Assert.notNull(res);
		return res;
	}

	public Integer countAllByCompany(final int companyId) {
		Assert.isTrue(companyId != 0);
		return this.auditRepository.countAllByCompany(companyId);
	}

	public Audit toFinalMode(final int auditId) {
		final Audit audit = this.findOne(auditId);
		final Auditor principal = this.auditorService.findByPrincipal();
		final Audit result;
		Assert.isTrue(audit.getId() != 0, "the audit has id 0");
		Assert.isTrue(audit.getAuditor().getId() == principal.getId(), "No puede actualizar una solicitud que no le pertenece.");
		Assert.isTrue(audit.getIsDraft(), "You can not edit a audit that is not in draft mode");
		audit.setIsDraft(false); //para pasarla a modo final se hace con el método toFinalMode
		audit.setAuditor(principal);
		result = this.auditRepository.save(audit);
		Assert.notNull(result, "audit method toFinalMode returns null");

		return result;
	}

	public void deleteInBatch(final Collection<Audit> audits) {
		this.auditRepository.deleteInBatch(audits);
	}

	public Audit reconstruct(final AuditForm auditForm, final BindingResult binding, final int positionId) {
		Audit result;
		if (auditForm.getId() == 0)
			result = this.create(positionId);
		else
			result = this.findOne(auditForm.getId());

		result.setId(auditForm.getId());
		result.setVersion(auditForm.getVersion());
		result.setText(auditForm.getText());
		result.setScore(auditForm.getScore());

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public AuditForm inyect(final Audit audit) {
		final AuditForm pruned = new AuditForm();

		pruned.setId(audit.getId());
		pruned.setVersion(audit.getVersion());
		pruned.setScore(audit.getScore());
		pruned.setText(audit.getText());

		return pruned;
	}

	public void flush() {
		this.auditRepository.flush();
	}

}
