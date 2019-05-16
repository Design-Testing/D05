
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Audit;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AuditServiceTest extends AbstractTest {

	@Autowired
	private AuditService	auditService;

	@Autowired
	private PositionService	positionService;


	@Test
	public void driverAudit() {
		final Object testingData[][] = {
			{		// Enrole correcto
				"auditor1", "position8", "text test", 6, null
			}, {	// Enrole con dropOut seteado (no activo) 
				"auditor1", "position9", "", 5, javax.validation.ConstraintViolationException.class
			}, {	// No existe enrole
				"auditor2", "position10", "test text", null, javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateAudit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateAudit(final String auditor, final String pos, final String text, final Integer score, final Class<?> expected) {
		Class<?> caught;
		int positionId;
		caught = null;
		try {

			this.authenticate(auditor);
			positionId = super.getEntityId(pos);
			final Position position = this.positionService.findOne(positionId);

			final Audit audit = this.auditService.create(positionId);
			audit.setText(text);
			audit.setScore(score);
			final Audit res = this.auditService.save(audit, positionId);
			Assert.notNull(res);
			this.auditService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverEditAudit() {
		final Object testingData[][] = {
			{		// Positivo
				"auditor2", "audit1", "text test edited", 6, null
			}, {	// Negativo: texto en blanco
				"auditor2", "audit1", "", 5, javax.validation.ConstraintViolationException.class
			}, {	// Negativo: No es su audit
				"auditor1", "audit1", "test text edited", 6, java.lang.IllegalArgumentException.class
			}, {	// Negativo: No está en darft mode
				"auditor2", "audit7", "test text edited", 6, java.lang.IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateEditAudit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateEditAudit(final String auditor, final String aud, final String text, final Integer score, final Class<?> expected) {
		Class<?> caught;
		int positionId;
		caught = null;
		try {

			this.authenticate(auditor);
			final Audit audit = this.auditService.findOne(this.getEntityId(aud));
			positionId = audit.getPosition().getId();

			audit.setText(text);
			audit.setScore(score);
			final Audit res = this.auditService.save(audit, positionId);
			Assert.notNull(res);
			this.auditService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteAudit() {
		final Object testingData[][] = {
			{		// Positivo
				"auditor2", "audit1", null
			}, {	// Negativo: no esta en fradt mode
				"auditor2", "audit7", java.lang.IllegalArgumentException.class
			}, {	// Negativo: No es su audit
				"auditor1", "audit1", java.lang.IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDeleteAudit((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateDeleteAudit(final String auditor, final String aud, final Class<?> expected) {
		Class<?> caught;
		int positionId;
		caught = null;
		try {

			this.authenticate(auditor);
			final Audit audit = this.auditService.findOne(this.getEntityId(aud));
			positionId = audit.getPosition().getId();

			this.auditService.delete(audit);
			Assert.isTrue(!this.auditService.findAll().contains(audit));
			this.auditService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
