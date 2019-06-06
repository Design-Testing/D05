
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

	"classpath:spring/junit.xml"

})
@Transactional
public class CreditCardServiceTest extends AbstractTest {

	@Autowired
	private CreditCardService	creditCardService;


	@Test
	public void driverCreateAndSaveCreditCard() {
		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar una tarjeta de crédito
				//				B: Test Positivo: Creación correcta de una tarjeta de crédito
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "student 1", "VISA", "4716477920082572", 06, 19, "163", null
			}, {

				//				A: Educafy Crear y guardar una tarjeta de crédito
				//				B: Test Negativo: Creación incorrecta de una tarjeta de crédito, parámetros expirationYear vacío
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "student 1", "VISA", "4716477920082572", 06, null, "163", NullPointerException.class
			}, {

				//				A: Educafy Crear y guardar una finder
				//				B: Test Negativo: Creación incorrecta de una tarjeta de crédito, parámetros expirationMonth vacío
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "student 1", "VISA", "4716477920082572", null, 19, "", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateCreateAndSave(final String principal, final String holderName, final String make, final String number, final Integer expirationMonth, final Integer expirationYear, final String cvv, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(principal);
			final CreditCard creditCard = this.creditCardService.create();

			creditCard.setHolderName(holderName);
			creditCard.setMake(make);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvv(cvv);
			this.creditCardService.save(creditCard);
			//			this.creditCardService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
	}

}
