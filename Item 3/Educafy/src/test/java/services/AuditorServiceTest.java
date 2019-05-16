
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Auditor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AuditorServiceTest extends AbstractTest {

	// Services
	@Autowired
	private AuditorService	auditorService;


	/* ========================= Test Login Auditor =========================== */
	@Test
	public void driverLoginAuditor() {

		final Object testingData[][] = {
			{
				//				A: Acme Rookies Login Use Case
				//				B: Test Positivo: Un auditor puede loguearse correctamente
				//				C: % Recorre 22 de la 22 lineas posibles
				//				D: cobertura de datos = 2/2
				"auditor1", null
			}, {
				//				A: Acme Rookies Login usuario no registrado
				//				B: Test Negativo: Un usuario no registrado no puede logearse
				//				C: % Recorre 8 de la 22 lineas posibles
				//				D: cobertura de datos = 2/2
				"AuditorNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String auditorUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(auditorUsername);
			this.unauthenticate();
			this.auditorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Auditor =========================== */

	@Test
	public void driverCreateAndSaveAuditor() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Lanzas");
		final Object testingData[][] = {
			/*{
				//				A: Acme Rookies Register to de system as a company
				//				B: Test Positivo: Creación correcta de un auditor
				//				C: % Recorre 181 de la 181 lineas posibles
				//				D: cobertura de datos = 12/29
				"auditor1", "auditor1", "Auditor1", surnames, "garcia@gmail.es", "+34647307406", null
			},*/ {
				//				A: Acme Rookies Register to de system as a company
				//				B: Test Negativo: Creación incorrecta de un auditor con telefono inválido
				//				C: % Recorre 114 de la 181 lineas posibles
				//				D: cobertura de datos = 12/29
				"auditor2", "auditor2", "Auditor2", surnames2, "lanzas@gmail.com", "mi telefono", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		Auditor auditor;
		final UserAccount userAccount;

		caught = null;

		try {
			this.authenticate("system");
			auditor = this.auditorService.create();
			auditor.setName(name);
			auditor.setSurname(surname);
			auditor.setEmail(email);
			auditor.setPhone(phone);
			userAccount = auditor.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			auditor.setUserAccount(userAccount);
			this.auditorService.flush();
			auditor = this.auditorService.save(auditor);
			Assert.notNull(auditor);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Auditor =========================== */

	@Test
	public void driverEditAuditor() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames2 = new ArrayList<>();

		final Object testingData[][] = {
			{
				// 				A: Acme Rookies Edit his o her personal data
				//				B: Test Positivo: Edición correcta de los datos de un auditor
				//				C: % Recorre 181 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"auditor1", "auditor1", "Auditor1", surnames, "garcia@gmail.es", "+34647307406", null
			}, {
				// 				A: Acme Rookies Edit his o her personal data
				//				B: Test Positivo: Edición correcta de los datos de un auditor con phone vacio
				//				C: % Recorre 181 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"auditor1", "auditor1", "Auditor1", surnames, "garcia@gmail.es", "", null
			}, {
				// 				A: Acme Rookies Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un auditor con mail inválido
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"auditor1", "auditor1", "Auditor1", surnames, "no tengo email", "+34647307406", ConstraintViolationException.class
			}, {
				// 				A: Acme Rookies Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un auditor con name vacio
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"auditor1", "auditor1", "", surnames, "garcia@gmail.es", "+34647307406", ConstraintViolationException.class
			}, {
				// 				A: Acme Rookies Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un auditor con apellidos vacio
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"auditor1", "auditor1", "Auditor1", surnames2, "garcia@gmail.es", "+34647307406", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAuditor((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEditAuditor(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {
		Class<?> caught;
		Auditor auditor;
		auditor = this.auditorService.findOne(this.getEntityId(username));

		caught = null;
		try {
			super.authenticate(username);
			auditor.setName(name);
			auditor.setSurname(surname);
			auditor.setEmail(email);
			auditor.setPhone(phone);
			this.auditorService.save(auditor);
			this.unauthenticate();
			this.auditorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

}
