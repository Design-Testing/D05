
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

import utilities.AbstractTest;
import domain.ConfigurationParameters;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationParameterServiceTest extends AbstractTest {

	// Services
	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private AdministratorService			administratorService;


	//Test Save

	@Test
	public void driverEditAndSave() {
		final Collection<String> posWords = new ArrayList<>();
		posWords.add("good");
		final Collection<String> negWords = new ArrayList<>();
		negWords.add("not");
		final Collection<String> spamWords = new ArrayList<>();
		spamWords.add("viagra");
		final Collection<String> words = new ArrayList<>();
		words.add("noExisteEstaPalabra");
		final Object testingData[][] = {
			{
				//				A: Acme HackerRank Número máximo de resultados para el finder con valor mínimo
				//				B: Test Positivo: 1 está dentro del rango permitido, es el valor minimo
				//				C: % Recorre 21 de las 21 lineas posibles
				//				D: % cobertura de datos= 92%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 1, 10, posWords, negWords, spamWords, null
			}, {
				//				A: Acme HackerRank Número máximo de resultados para el finder con una unidad por debajo del mínimo
				//				B: Test Negativo: El rango de máximo numero de resultados por parte del finder no puede ser menor a uno: ConstraintViolationException.
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura de datos= 92%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 10, 9, null, negWords, spamWords, ConstraintViolationException.class
			}, {
				//				A: Acme HackerRank Número máximo de resultados para el finder con una unidad por encima del mínimo
				//				B: Test Positivo: El minimo mas uno sigue estando dentro del rango admitido para dicho valor
				//				C: % Recorre 21 de las 21 lineas posibles
				//				D: % cobertura de datos= 92%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 10, 11, posWords, null, spamWords, null
			}, {
				//				A: Acme HackerRank Número máximo de resultados para el finder con una unidad por encima del maximo
				//				B: Test Negativo: El valor esta fuera del rango permitido: ConstraintViolationException. 
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura d92%atos= 92%
				"admin2", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 10, 101, posWords, negWords, words, ConstraintViolationException.class
			}, {
				//				A: Acme HackerRank Número máximo de resultados para el finder con el maximo valor del rango permitido
				//				B: Test Positivo: El valor esta dentro del rango permitido
				//				C: % Recorre 21 de las 21 lineas posibles
				//				D: % cobertura de datos= 92%
				//MaxFinderResults con valor max.
				"admin2", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 10, 100, posWords, negWords, spamWords, null
			}, {
				//				A: Acme HackerRank Número máximo de resultados para el finder con una unidad por debajo del maximo
				//				B: Test Positivo: El valor esta dentro del rango permitido
				//				C: % Recorre 21 de las 21 lineas posibles
				//				D: % cobertura de datos= 92%
				//MaxFinderResults con valor max - 1.
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 10, 99, posWords, negWords, spamWords, null
			}, {
				//				A: Acme HackerRank Tiempo como maximo que el finder estara guardado y disponible para su dueño, con su valor minimo
				//				B: Test Positivo: Valor minimo permitido de dicho tiempo entra dentro del rango de valores permitidos logicamente
				//				C: % Recorre 21 de las 21 lineas posibles
				//				D: % cobertura de datos= 84%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 1, 99, posWords, negWords, spamWords, null
			}, {
				//				A: Acme HackerRank Tiempo como maximo que el finder estara guardado y disponible para su dueño, con su valor minimo menos una unidad
				//				B: Test Negativo: Valor minimo permitido de dicho tiempo menos uno no entra dentro del rango de valores permitidos: ConstraintViolationException.
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura de datos= 84%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 0, 99, posWords, negWords, spamWords, ConstraintViolationException.class
			}, {
				//				A: Acme HackerRank Tiempo como maximo que el finder estara guardado y disponible para su dueño, con su valor maximo
				//				B: Test Positivo: Valor maximo permitido de dicho tiempo entra dentro del rango de valores permitidos logicamente
				//				C: % Recorre 21 de las 21 lineas posibles
				//				D: % cobertura de datos= 84%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 24, 99, posWords, negWords, spamWords, null
			}, {
				//				A: Acme HackerRank Tiempo como maximo que el finder estara guardado y disponible para su dueño, con su valor maximo mas una unidad
				//				B: Test Negativo: Valor maximo permitido de dicho tiempo mas uno no entra dentro del rango de valores permitidos: ConstraintViolationException.
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura de datos= 84%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+34", 25, 99, posWords, negWords, spamWords, ConstraintViolationException.class
			}, {
				//				A: Acme HackerRank Mensaje de bienvenida en español vacio
				//				B: Test Negativo: El mensaje de bienvenida en español no puede ser nulo ni estar en blanco: ConstraintViolationException.
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura de datos= 58%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "", "welcomeMessageEn", "+34", 10, 99, posWords, negWords, spamWords, ConstraintViolationException.class
			}, {
				//				A: Acme HackerRank Mensaje de bienvenida en inglés vacio
				//				B: Test Negativo: El mensaje de bienvenida en inglés no puede ser nulo ni estar en blanco: ConstraintViolationException.
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura de datos= 58%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "", "+34", 10, 99, posWords, negWords, spamWords, ConstraintViolationException.class
			}, {
				//				A: Acme HackerRank Prefijo correspondiente a la zona geografica del telefono con valor diferente a su patron
				//				B: Test Negativo: CountryPhoneCode con valor diferente al Pattern, ConstraintViolationException.
				//				C: % Recorre 19 de las 21 lineas posibles
				//				D: % cobertura de datos= 43%
				"admin1", "Acme Hacker Rank", "https://i.imgur.com/7b8lu4b.png", "welcomeMessageEsp", "welcomeMessageEn", "+345", 10, 99, posWords, negWords, spamWords, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], (Collection<String>) testingData[i][8], (Collection<String>) testingData[i][9], (Collection<String>) testingData[i][10], (Class<?>) testingData[i][11]);
	}
	private void templateEditAndSave(final String adminUsername, final String sysName, final String banner, final String welcomeMessageEsp, final String welcomeMessageEn, final String countryPhoneCode, final Integer finderTime,
		final Integer maxFinderResults, final Collection<String> posWords, final Collection<String> negWords, final Collection<String> spamWords, final Class<?> expected) {
		Class<?> caught;
		ConfigurationParameters cParameters;

		caught = null;

		try {
			super.authenticate(adminUsername);
			final int configparamsId = super.getEntityId("configurationParameters");
			cParameters = this.configurationParametersService.findOne(configparamsId);
			//			cParameters = this.configurationParametersService.create();
			cParameters.setSysName(sysName);
			cParameters.setBanner(banner);
			cParameters.setWelcomeMessageEn(welcomeMessageEn);
			cParameters.setWelcomeMessageEsp(welcomeMessageEsp);
			cParameters.setCountryPhoneCode(countryPhoneCode);
			cParameters.setPositiveWords(posWords);
			cParameters.setNegativeWords(negWords);
			cParameters.setMaxFinderResults(maxFinderResults);
			cParameters.setFinderTime(finderTime);
			cParameters.setSpamWords(spamWords);
			this.configurationParametersService.save(cParameters);
			this.configurationParametersService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverAddWord() {
		final Object testingData[][] = {
			{
				//				A: Acme HackerRank Login User Non registered
				//				B: Test Positivo: Un usuario no registrado no puede logearse
				//				C: % Recorre 19 de la 19 lineas posibles
				//				D: % cobertura de datos= 2/2
				"admin1", "hola", null
			}, {
				//				A: Acme HackerRank Login User Non registered
				//				B: Test Positivo: Un usuario no registrado no puede logearse
				//				C: % Recorre 7 de la 19 lineas posibles
				//				D: % cobertura de datos= 2/2
				"admin1", null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddWord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	private void templateAddWord(final String adminUsername, final String word, final Class<?> expected) {
		Class<?> caught;
		final ConfigurationParameters cParameters;

		caught = null;

		try {
			super.authenticate(adminUsername);
			//			cParameters = this.configurationParametersService.findOne(44);
			cParameters = this.configurationParametersService.create();
			this.configurationParametersService.addPositiveWord(word);
			this.configurationParametersService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteWord() {
		final Object testingData[][] = {
			{
				//				A: Acme HackerRank Borrar palabra de la lista de palabras positivas
				//				B: Test Positivo: La palabra "good" existe y puede borrarse correctamente
				//				C: % Recorre 28 de la 28 lineas posibles
				//				D: % cobertura de datos= 93%
				"admin1", "good", null
			}, {
				//				A: Acme HackerRank Borrar palabra de la lista de palabras positivas
				//				B: Test Negativo: No puede borrarse un null
				//				C: % Recorre 26 de la 28 lineas posibles
				//				D: % cobertura de datos= 93%
				"admin1", null, IllegalArgumentException.class
			}, {
				//				A: Acme HackerRank Borrar palabra de la lista de palabras positivas
				//				B: Test Negativo: Borrar una palabra que no existe en la lista de las palabras negativas
				//				C: % Recorre 27 de la 28 lineas posibles
				//				D: % cobertura de datos= 93%
				"admin1", "hola", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteWord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	private void templateDeleteWord(final String adminUsername, final String word, final Class<?> expected) {
		Class<?> caught;
		final ConfigurationParameters cParameters;

		caught = null;

		try {
			super.authenticate(adminUsername);
			cParameters = this.configurationParametersService.findOne(44);
			//			cParameters = this.configurationParametersService.create();
			this.configurationParametersService.deletePositiveWord(word);
			this.configurationParametersService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
