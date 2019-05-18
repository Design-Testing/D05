
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curriculum;
import domain.PersonalRecord;
import domain.Rooky;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PersonalDataServiceTest extends AbstractTest {

	// Services
	@Autowired
	private PersonalDataService	personalDataService;

	@Autowired
	private RookyService		hackerService;

	@Autowired
	private CurriculaService	curriculaService;


	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Positivo: Rooky creaPersonalData
				//			C: 100% Recorre 68 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", "hacker 1 fullname", "statement test", "http://www.github.com/test", "+34 667345123", "http://www.linkedin.com/test", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Negativo: no introduce una url como github link
				//			C: 89,70% Recorre 61 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", "hacker 1 fullname", "statement test", "github profile", "+34 667345123", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Negativo: introduce un numero de telefono incorrecto
				//			C: 11,76% Recorre 8 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", "hacker 1 fullname", "statement test", "http://www.github.com/test", "667345123", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Negativo: no introduce nombre completo
				//			C: 98,52% Recorre 67 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", null, "statement test", "http://www.github.com/test", "667345123", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	protected void templateCreateSave(final String user, final String fullName, final String statement, final String github, final String phone, final String linkedin, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final PersonalRecord incRec = this.personalDataService.create();
			incRec.setFullName(fullName);
			incRec.setStatement(statement);
			incRec.setLinkedin(linkedin);
			incRec.setGithub(github);
			incRec.setPhone(phone);
			final PersonalRecord incRecSaved = this.personalDataService.save(incRec);
			Assert.isTrue(incRecSaved.getId() != 0);
			this.personalDataService.flush();
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
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Positivo: Rooky creaPersonalData
				//			C: 100% Recorre 68 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", "hacker1", "hacker 1 fullname", "statement test", "http://www.github.com/test", "+34 667345123", "http://www.linkedin.com/test", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Negativo: no introduce una url como github link
				//			C: 89,70% Recorre 61 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", "hacker1", "hacker 1 fullname", "statement test", "github profile", "+34 667345123", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Negativo: un hacker intenta editar personal data de otro
				//			C: 11,76% Recorre 8 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker2", "hacker1", "hacker 1 fullname", "statement test", "http://www.github.com/test", "667345123", "http://www.linkedin.com/test", java.lang.IllegalArgumentException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
				//			B: Test Negativo: no introduce nombre completo
				//			C: 98,52% Recorre 67 de las 68 lineas posibles
				//			D: cobertura de datos=4/54
				"hacker1", "hacker1", null, "statement test", "http://www.github.com/test", "667345123", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	private void templateEdit(final String user, final String userProp, final String fullName, final String statement, final String github, final String phone, final String linkedin, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Rooky owner = this.hackerService.findOne(this.getEntityId(userProp));
			final PersonalRecord iR;
			final Curriculum curricula = this.curriculaService.findCurriculaByRooky(owner.getId()).iterator().next();
			iR = curricula.getPersonalRecord();
			iR.setFullName(fullName);
			iR.setStatement(statement);
			iR.setLinkedin(linkedin);
			iR.setGithub(github);
			iR.setPhone(phone);
			curricula.setPersonalRecord(iR);
			this.personalDataService.save(iR);
			this.personalDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
