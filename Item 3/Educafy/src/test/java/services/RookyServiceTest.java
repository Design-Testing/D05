
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

import security.UserAccount;
import utilities.AbstractTest;
import domain.Rooky;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RookyServiceTest extends AbstractTest {

	// Services
	@Autowired
	private RookyService	rookyService;


	/* ========================= Test Login Rooky =========================== */
	@Test
	public void driverLoginRooky() {

		final Object testingData[][] = {
			{
				//				A: Acme RookyRank Login Use Case
				//				B: Test Positivo: Un rooky puede loguearse correctamente
				//				C: % Recorre 22 de la 22 lineas posibles
				//				D: cobertura de datos = 2/2
				"rooky1", null
			}, {
				//				A: Acme RookyRank Login usuario no registrado
				//				B: Test Negativo: Un usuario no registrado no puede logearse
				//				C: % Recorre 8 de la 22 lineas posibles
				//				D: cobertura de datos = 2/2
				"RookyNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String rookyUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(rookyUsername);
			this.unauthenticate();
			this.rookyService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Rooky =========================== */

	@Test
	public void driverCreateAndSaveRooky() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Lanzas");
		final Object testingData[][] = {
			{
				//				A: Acme RookyRank Register to de system as a company
				//				B: Test Positivo: Creación correcta de un rooky
				//				C: % Recorre 181 de la 181 lineas posibles
				//				D: cobertura de datos = 12/29
				"rooky1", "rooky1", "Rooky1", surnames, "garcia@gmail.es", "+34647307406", null
			}, {
				//				A: Acme RookyRank Register to de system as a company
				//				B: Test Negativo: Creación incorrecta de un rooky con telefono inválido
				//				C: % Recorre 114 de la 181 lineas posibles
				//				D: cobertura de datos = 12/29
				"rooky2", "rooky2", "Rooky1", surnames2, "lanzas@gmail.com", "mi telefono", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		Rooky rooky;
		final UserAccount userAccount;

		caught = null;

		try {
			rooky = this.rookyService.create();
			rooky.setName(name);
			rooky.setSurname(surname);
			rooky.setEmail(email);
			rooky.setPhone(phone);
			userAccount = rooky.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			rooky.setUserAccount(userAccount);
			rooky = this.rookyService.save(rooky);
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Rooky =========================== */

	@Test
	public void driverEditRooky() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames2 = new ArrayList<>();

		final Object testingData[][] = {
			{
				// 				A: Acme RookyRank Edit his o her personal data
				//				B: Test Positivo: Edición correcta de los datos de un rooky
				//				C: % Recorre 181 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"rooky1", "rooky1", "Rooky1", surnames, "garcia@gmail.es", "+34647307406", null
			}, {
				// 				A: Acme RookyRank Edit his o her personal data
				//				B: Test Positivo: Edición correcta de los datos de un rooky con phone vacio
				//				C: % Recorre 181 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"rooky1", "rooky1", "Rooky1", surnames, "garcia@gmail.es", "", null
			}, {
				// 				A: Acme RookyRank Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un rooky con mail inválido
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"rooky1", "rooky1", "Rooky1", surnames, "no tengo email", "+34647307406", ConstraintViolationException.class
			}, {
				// 				A: Acme RookyRank Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un rooky con name vacio
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"rooky1", "rooky1", "", surnames, "garcia@gmail.es", "+34647307406", ConstraintViolationException.class
			}, {
				// 				A: Acme RookyRank Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un rooky con apellidos vacio
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"rooky1", "rooky1", "Rooky1", surnames2, "garcia@gmail.es", "+34647307406", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditRooky((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEditRooky(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {
		Class<?> caught;
		Rooky rooky;
		rooky = this.rookyService.findOne(this.getEntityId(username));

		caught = null;
		try {
			super.authenticate(username);
			rooky.setName(name);
			rooky.setSurname(surname);
			rooky.setEmail(email);
			rooky.setPhone(phone);
			this.rookyService.save(rooky);
			this.unauthenticate();
			this.rookyService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

}
