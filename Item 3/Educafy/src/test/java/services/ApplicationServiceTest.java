
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;
import domain.Curriculum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	// Services
	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CurriculaService	curriculaService;


	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//			A: Acme Rooky Rank Req. 10.1 -> Manage his or her applications, which includes listing them grouped by status, showing
				//			them, creating them, and updating them.
				//			B: Test Positivo: Rooky crea una nueva solicitud a una position
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"rooky1", "position6", null
			}, {
				//			A: Acme Rooky Rank Req. 10.1 -> Manage his or her applications, which includes listing them grouped by status, showing
				//			them, creating them, and updating them.
				//			B: Test Negativo: Rooky crea una nueva solicitud a una position que esta en DRAFT MODE
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"rooky1", "position5", IllegalArgumentException.class
			}, {
				//			A: Acme Rooky Rank Req. 10.1 -> Manage his or her applications, which includes listing them grouped by status, showing
				//			them, creating them, and updating them.
				//			B: Test Negativo: Rooky crea una nueva solicitud a una position en la que ese rooky ya tiene
				//			solicitudes a todos los problemas posibles.
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"rooky1", "position1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateCreateSave(final String rooky, final String position, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(rooky);

			final Integer positionId = this.getEntityId(position);
			final Curriculum curricula = this.curriculaService.findOne(this.getEntityId("curricula1"));
			final Application saved = this.applicationService.apply(positionId, curricula.getId());

			Assert.isTrue(saved.getId() != 0);
			this.applicationService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//			A: Acme Rooky Rank Req. 10.1 -> Manage his or her applications, which includes listing them grouped by status, showing
				//			them, creating them, and UPDATING them.
				//			B: Test Positivo: Un rooky actualiza una application para añadirle una solución al problema.
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"rooky1", "application1", "Explanation application 1", "http://www.linkAnswer1.com", null
			}, {
				//			A: Acme Rooky Rank Req. 10.1 -> Manage his or her applications, which includes listing them grouped by status, showing
				//			them, creating them, and UPDATING them.
				//			B: Test Negativo: Un rooky actualiza una application (que no le pertenece) para añadirle una solución al problema.
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"rooky2", "application1", "Explanation application 1", "http://www.linkAnswer1.com", IllegalArgumentException.class
			}, {
				//			A: Acme Rooky Rank Req. 10.1 -> Manage his or her applications, which includes listing them grouped by status, showing
				//			them, creating them, and UPDATING them.
				//			B: Test Negativo: Un rooky actualiza una application para añadirle una solución al problema y esa application ya tiene solución añadida.
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"rooky1", "application3", "Explanation application 3", "http://www.linkAnswer3.com", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void templateEdit(final String rooky, final String application, final String explanation, final String link, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(rooky);

			final Integer applicationId = this.getEntityId(application);
			final Application find = this.applicationService.findOne(applicationId);
			this.applicationService.save(find, find.getPosition().getId());

			this.applicationService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}
		super.checkExceptions(expected, caught);
	}
	@Test
	public void driverAcceptApplication() {
		final Object testingData[][] = {
			{
				//			A: Acme Rooky Rank Req. 9.3 -> Manage the applications to their positions which includes listing 
				//			them grouped by status, showing them, and UPDATING them.
				//			B: Test Positivo: Company acepta una application
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"company1", "application3", null
			}, {
				//			A: Acme Rooky Rank Req. 9.3 -> Manage the applications to their positions which includes listing 
				//			them grouped by status, showing them, and UPDATING them.
				//			B: Test Negativo: Company acepta una application que no le pertenece
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"company2", "application3", IllegalArgumentException.class
			}, {
				//			A: Acme Rooky Rank Req. 9.3 -> Manage the applications to their positions which includes listing 
				//			them grouped by status, showing them, and UPDATING them.
				//			B: Test Negativo: Company acepta una application con estado PENDING
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"company1", "application1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAcceptApplication((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateAcceptApplication(final String company, final String application, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(company);
			final Integer applicationId = this.getEntityId(application);

			this.applicationService.acceptApplication(applicationId);
			this.applicationService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverRejectApplication() {
		final Object testingData[][] = {
			{
				//			A: Acme Rooky Rank Req. 9.3 -> Manage the applications to their positions which includes listing 
				//			them grouped by status, showing them, and UPDATING them.
				//			B: Test Positivo: Company rechaza una application
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"company1", "application3", null
			}, {
				//			A: Acme Rooky Rank Req. 9.3 -> Manage the applications to their positions which includes listing 
				//			them grouped by status, showing them, and UPDATING them.
				//			B: Test Negativo: Company acepta una application que no le pertenece
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"company2", "application3", IllegalArgumentException.class
			}, {
				//			A: Acme Rooky Rank Req. 9.3 -> Manage the applications to their positions which includes listing 
				//			them grouped by status, showing them, and UPDATING them.
				//			B: Test Positivo: Company acepta una application con estado PENDING
				//			C: 97,95% Recorre 48 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"company1", "application1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRejectApplication((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateRejectApplication(final String company, final String application, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(company);
			final Integer applicationId = this.getEntityId(application);

			this.applicationService.rejectApplication(applicationId);
			this.applicationService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
