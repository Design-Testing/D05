
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
import domain.Provider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProviderServiceTest extends AbstractTest {

	// Services
	@Autowired
	private ProviderService	providerService;


	/* ========================= Test Login Provider =========================== */
	@Test
	public void driverLoginProvider() {

		final Object testingData[][] = {
			{
				//				A: Acme ProviderRank Login Use Case
				//				B: Test Positivo: Un provider puede loguearse correctamente
				//				C: % Recorre 22 de la 22 lineas posibles
				//				D: cobertura de datos = 2/2
				"provider1", null
			}, {
				//				A: Acme ProviderRank Login usuario no registrado
				//				B: Test Negativo: Un usuario no registrado no puede logearse
				//				C: % Recorre 8 de la 22 lineas posibles
				//				D: cobertura de datos = 2/2
				"ProviderNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String providerUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(providerUsername);
			this.unauthenticate();
			this.providerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Provider =========================== */

	@Test
	public void driverCreateAndSaveProvider() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Lanzas");
		final Object testingData[][] = {
			{
				//				A: Acme ProviderRank Register to de system as a company
				//				B: Test Positivo: Creación correcta de un provider
				//				C: % Recorre 181 de la 181 lineas posibles
				//				D: cobertura de datos = 12/29
				"provider1", "provider1", "Provider1", surnames, "garcia@gmail.es", "+34647307406", null
			}, {
				//				A: Acme ProviderRank Register to de system as a company
				//				B: Test Negativo: Creación incorrecta de un provider con telefono inválido
				//				C: % Recorre 114 de la 181 lineas posibles
				//				D: cobertura de datos = 12/29
				"provider2", "provider2", "Provider1", surnames2, "lanzas@gmail.com", "mi telefono", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateCreateAndSave(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		Provider provider;
		final UserAccount userAccount;

		caught = null;

		try {
			provider = this.providerService.create();
			provider.setName(name);
			provider.setSurname(surname);
			provider.setEmail(email);
			provider.setPhone(phone);
			userAccount = provider.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			provider.setUserAccount(userAccount);
			provider = this.providerService.save(provider);
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Provider =========================== */

	@Test
	public void driverEditProvider() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames2 = new ArrayList<>();

		final Object testingData[][] = {
			{
				// 				A: Acme ProviderRank Edit his o her personal data
				//				B: Test Positivo: Edición correcta de los datos de un provider
				//				C: % Recorre 181 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"provider1", "provider1", "Provider1", surnames, "garcia@gmail.es", "+34647307406", null
			}, {
				// 				A: Acme ProviderRank Edit his o her personal data
				//				B: Test Positivo: Edición correcta de los datos de un provider con phone vacio
				//				C: % Recorre 181 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"provider1", "provider1", "Provider1", surnames, "garcia@gmail.es", "", null
			}, {
				// 				A: Acme ProviderRank Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un provider con mail inválido
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"provider1", "provider1", "Provider1", surnames, "no tengo email", "+34647307406", ConstraintViolationException.class
			}, {
				// 				A: Acme ProviderRank Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un provider con name vacio
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"provider1", "provider1", "", surnames, "garcia@gmail.es", "+34647307406", ConstraintViolationException.class
			}, {
				// 				A: Acme ProviderRank Edit his o her personal data
				//				B: Test Negativo: Edición incorrecta de los datos de un provider con apellidos vacio
				//				C: % Recorre 114 de las 181 lineas posibles
				//				D: cobertura de datos = 19/32
				"provider1", "provider1", "Provider1", surnames2, "garcia@gmail.es", "+34647307406", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditProvider((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEditProvider(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {
		Class<?> caught;
		Provider provider;
		provider = this.providerService.findOne(this.getEntityId(username));

		caught = null;
		try {
			super.authenticate(username);
			provider.setName(name);
			provider.setSurname(surname);
			provider.setEmail(email);
			provider.setPhone(phone);
			this.providerService.save(provider);
			this.unauthenticate();
			this.providerService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

}
