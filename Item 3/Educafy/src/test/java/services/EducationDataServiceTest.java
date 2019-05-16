
package services;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curricula;
import domain.EducationData;
import domain.PersonalData;
import domain.Rooky;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EducationDataServiceTest extends AbstractTest {

	// Services
	@Autowired
	private EducationDataService	educationDataService;

	@Autowired
	private RookyService			hackerService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private PersonalDataService		personalDataService;


	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea EducationData 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "degree1", "institution1", 5, "2014-09-15", "2018-09-20", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Un member intenta crear una EducationData sin grado
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", null, "institution1", 5, "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea EducationData con institución en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "degree1", "", 3, "2014-09-15", "2018-09-20", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea EducationData con fecha de finalización anterior a fecha de inicio
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "degree1", "institution2", 3, "2014-09-15", "2013-09-20", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateCreateSave(final String user, final String degree, final String institution, final Integer mark, final String startDate, final String endDate, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			Curricula curricula = this.curriculaService.create();
			PersonalData pd = curricula.getPersonalRecord();
			pd = this.personalDataService.save(pd);
			curricula.setPersonalRecord(pd);
			curricula = this.curriculaService.save(curricula);
			final EducationData lRec = this.educationDataService.create();
			lRec.setDegree(degree);
			lRec.setInstitution(institution);
			lRec.setMark(mark);
			Date start = null;
			Date end = null;
			if (endDate != null)
				end = (new SimpleDateFormat("yyyy-MM-dd")).parse(endDate);
			start = (new SimpleDateFormat("yyyy-MM-dd")).parse(startDate);
			lRec.setStartDate(start);
			lRec.setEndDate(end);
			final EducationData lRecSaved = this.educationDataService.save(lRec, curricula.getId());
			Assert.isTrue(lRecSaved.getId() != 0);
			this.educationDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(oops.getMessage());
		}

		super.checkExceptions(expected, caught);
	}
	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea EducationData 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "degree1", "institution1", 5, "2014-09-15", "2018-09-20", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Un member intenta crear una EducationData sin grado
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", null, "institution1", 5, "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea EducationData con institución en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "degree1", "", 3, "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea EducationData con fecha de finalización anterior a fecha de inicio
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "degree1", "institution2", 3, "2014-09-15", "2013-09-20", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEdit(final String user, final String degree, final String institution, final Integer mark, final String startDate, final String endDate, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Rooky principal = this.hackerService.findByPrincipal();
			final Collection<Curricula> curriculas = this.curriculaService.findCurriculaByRooky(principal.getId());
			final Curricula curricula = curriculas.iterator().next();
			final EducationData lR = curricula.getEducations().iterator().next();
			lR.setInstitution(institution);
			lR.setDegree(degree);
			lR.setMark(mark);
			Date start = null;
			Date end = null;
			if (endDate != null)
				end = (new SimpleDateFormat("yyyy-MM-dd")).parse(endDate);
			start = (new SimpleDateFormat("yyyy-MM-dd")).parse(startDate);
			lR.setStartDate(start);
			lR.setEndDate(end);
			this.educationDataService.save(lR, curricula.getId());
			this.educationDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverDelete() {

		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky borra EducationData 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"hacker2", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Compañía intenta borrar EducationData 
				//			C: 10,25% Recorre 8 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"company1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateDelete(final String actor, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final Rooky hacker = this.hackerService.findByPrincipal();
			final EducationData lRec = this.curriculaService.findCurriculaByRooky(hacker.getId()).iterator().next().getEducations().iterator().next();
			this.educationDataService.delete(lRec);
			this.educationDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
