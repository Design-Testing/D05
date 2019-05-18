
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
import domain.Curriculum;
import domain.PersonalRecord;
import domain.PositionData;
import domain.Rooky;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PositionDataServiceTest extends AbstractTest {

	// Services
	@Autowired
	private PositionDataService	positionDataService;

	@Autowired
	private RookyService		hackerService;

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private PersonalDataService	personalDataService;


	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea PositionData 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "title1", "description1", "2014-09-15", "2018-09-20", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Un hacker intenta crear una PositionData con título nulo
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", null, "institution1", "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea PositionData con descripción en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", "title2", "", "2014-09-15", "2018-09-20", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea PositionData con fecha de finalización anterior a fecha de comienzo
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", "title3", "institution3", "2014-09-15", "2013-09-20", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final String startDate, final String endDate, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			Curriculum curricula = this.curriculaService.create();
			PersonalRecord pd = curricula.getPersonalRecord();
			pd = this.personalDataService.save(pd);
			curricula.setPersonalRecord(pd);
			curricula = this.curriculaService.save(curricula);
			final PositionData lRec = this.positionDataService.create();
			lRec.setTitle(title);
			lRec.setDescription(description);
			Date start = null;
			Date end = null;
			if (endDate != null)
				end = (new SimpleDateFormat("yyyy-MM-dd")).parse(endDate);
			start = (new SimpleDateFormat("yyyy-MM-dd")).parse(startDate);
			lRec.setStartDate(start);
			lRec.setEndDate(end);
			final PositionData lRecSaved = this.positionDataService.save(lRec, curricula.getId());
			Assert.isTrue(lRecSaved.getId() != 0);
			this.positionDataService.flush();
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
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea PositionData 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "title1", "description1", "2014-09-15", "2018-09-20", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Un hacker intenta crear una PositionData con título nulo
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", null, "institution1", "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea PositionData con descripción en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", "title2", "", "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea PositionData con fecha de finalización anterior a fecha de comienzo
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker2", "title3", "institution3", "2014-09-15", "2013-09-20", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void templateEdit(final String user, final String title, final String description, final String startDate, final String endDate, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Rooky principal = this.hackerService.findByPrincipal();
			final Collection<Curriculum> curriculas = this.curriculaService.findCurriculaByRooky(principal.getId());
			final Curriculum curricula = curriculas.iterator().next();
			final PositionData lR = curricula.getPositions().iterator().next();
			lR.setTitle(title);
			lR.setDescription(description);
			Date start = null;
			Date end = null;
			if (endDate != null)
				end = (new SimpleDateFormat("yyyy-MM-dd")).parse(endDate);
			start = (new SimpleDateFormat("yyyy-MM-dd")).parse(startDate);
			lR.setStartDate(start);
			lR.setEndDate(end);
			this.positionDataService.save(lR, curricula.getId());
			this.positionDataService.flush();
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
				//			B: Test Positivo: Rooky borra PositionData 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"hacker2", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Compañía intenta borrar PositionData 
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
			final PositionData lRec = this.curriculaService.findCurriculaByRooky(hacker.getId()).iterator().next().getPositions().iterator().next();
			this.positionDataService.delete(lRec);
			this.positionDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
