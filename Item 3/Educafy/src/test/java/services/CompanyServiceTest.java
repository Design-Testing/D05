
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

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Company;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CompanyServiceTest extends AbstractTest {

	// Services
	@Autowired
	private CompanyService		companyService;

	@Autowired
	private UserAccountService	userAccountService;


	/* ========================= Test Login Company =========================== */
	@Test
	public void driverLoginCompany() {

		final Object testingData[][] = {
			{
				//				A: Acme Hacker Rank Login Use Case
				//				B: Test Positivo: Un company puede loguearse correctamente
				//				C: % Recorre 10 de la 28 lineas posibles
				//				D: % cobertura de datos= 2/2
				"company1", null
			}, {
				//Login usuario no registrado
				"CompanyNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String companyUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(companyUsername);
			this.unauthenticate();
			this.companyService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Company =========================== */

	//				A: Acme Hacker Rank Req. 7.1 Register to de system as a company
	//				B: Test Negativo: Creación incorrecta de un company, con vat incorrecto (mayor que 1)
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%
	@Test()
	public void createAndSavePositive() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final CreditCard c = super.defaultCreditCard();

		UserAccount userAccount;
		Company company;

		company = this.companyService.create();
		userAccount = new UserAccount();
		userAccount.setUsername("company1");
		userAccount.setPassword("company1");
		final Collection<Authority> authorities = new ArrayList<>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.COMPANY);
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		company.setUserAccount(userAccount);
		company.setName("Company1");
		company.setSurname(surnames);
		company.setEmail("company1@gmail.es");
		company.setPhone("+34647307480");
		company.setCreditCard(c);
		company.setVat(0.21);
		company.setCommercialName("commercialName1");
		company = this.companyService.save(company);
		this.companyService.flush();

	}

	//				A: Acme Hacker Rank Req. 7.1 Register to de system as a company
	//				B: Test Negativo: Creación incorrecta de un company, commercialName incorrecto.
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%
	@Test(expected = ConstraintViolationException.class)
	public void createAndSaveNegative() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final CreditCard c = super.defaultCreditCard();

		UserAccount userAccount;
		Company company;

		company = this.companyService.create();
		userAccount = new UserAccount();
		userAccount.setUsername("company1");
		userAccount.setPassword("company1");
		final Collection<Authority> authorities = new ArrayList<>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.COMPANY);
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		company.setUserAccount(userAccount);
		company.setName("Company1");
		company.setSurname(surnames);
		company.setEmail("company1@gmail.es");
		company.setPhone("+34647307480");
		company.setCreditCard(c);
		company.setVat(0.21);
		company.setCommercialName(" ");
		company = this.companyService.save(company);
		this.companyService.flush();

	}

	/* ========================= Test Edit Company =========================== */

	//				A: Acme Hacker Rank Req. 8.2 Edit his or her personal data.
	//				B: Test Positivo: Edición correcta de los datos de un company.
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%
	@Test()
	public void EditPositive() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final CreditCard c = super.defaultCreditCard();

		Company company;

		super.authenticate("company1");
		company = this.companyService.findByPrincipal();
		company.setName("Company1");
		company.setSurname(surnames);
		company.setEmail("company1@gmail.es");
		company.setPhone("+34647307480");
		company.setCreditCard(c);
		company.setVat(0.21);
		company.setCommercialName("commercialName1");
		company = this.companyService.save(company);
		this.companyService.flush();
		super.unauthenticate();
	}

	//				A: Acme Hacker Rank Req. 8.2 Edit his or her personal data.
	//				B: Test Negativo: Edición incorrecta de los datos de un company (commercialName incorrecto).
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%
	@Test(expected = ConstraintViolationException.class)
	public void EditNegative() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final CreditCard c = super.defaultCreditCard();

		Company company;

		super.authenticate("company1");
		company = this.companyService.findByPrincipal();
		company.setName("Company1");
		company.setSurname(surnames);
		company.setEmail("company1@gmail.es");
		company.setPhone("+34647307480");
		company.setCreditCard(c);
		company.setVat(0.21);
		company.setCommercialName("");
		company = this.companyService.save(company);
		this.companyService.flush();
		super.unauthenticate();
	}

}
