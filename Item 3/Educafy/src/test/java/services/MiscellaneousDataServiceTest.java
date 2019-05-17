
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.Rooky;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MiscellaneousDataServiceTest extends AbstractTest {

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	private RookyService				hackerService;

	@Autowired
	private CurriculaService			curriculaService;

	@Autowired
	private PersonalDataService			personalDataService;


	@Test
	public void driverCreateSave() {
		final Collection<String> attachments = new ArrayList<String>();
		attachments.add("AttachmentTest1");
		attachments.add("AttachmentTest2");
		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky crea MiscellaneousData 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "freeText1", attachments, null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Un hacker intenta crear una MiscellaneousData con el texto vacío
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "", attachments, javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky intenta crear MiscellaneousData sin archivos adjuntos
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "freeText1", null, org.springframework.dao.DataIntegrityViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCreateSave(final String user, final String freeText, final Collection<String> attachments, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			Curriculum curricula = this.curriculaService.create();
			final PersonalRecord pd = curricula.getPersonalRecord();
			this.personalDataService.save(pd);
			curricula.setPersonalRecord(pd);
			curricula = this.curriculaService.save(curricula);

			final MiscellaneousRecord lRec = this.miscellaneousDataService.create();
			lRec.setFreeText(freeText);
			if (attachments != null)
				lRec.setAttachments(attachments);
			final MiscellaneousRecord lRecSaved = this.miscellaneousDataService.save(lRec, curricula.getId());
			Assert.isTrue(lRecSaved.getId() != 0);
			this.miscellaneousDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(oops.getMessage());
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<String> attachments = new ArrayList<String>();
		attachments.add("AttachmentTest1");
		attachments.add("AttachmentTest2");
		final Object testingData[][] = {
			{
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Positivo: Rooky edita MiscellaneousData 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "freeText1", attachments, null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Un hacker intenta editar una MiscellaneousData con el texto vacío
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"hacker1", "", attachments, javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateEdit(final String user, final String freeText, final Collection<String> attachments, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Rooky principal = this.hackerService.findByPrincipal();
			final Collection<Curriculum> curriculas = this.curriculaService.findCurriculaByRooky(principal.getId());
			final Curriculum curricula = curriculas.iterator().next();
			final MiscellaneousRecord lR = curricula.getMiscellaneous().iterator().next();
			lR.setFreeText(freeText);
			if (attachments != null)
				lR.setAttachments(attachments);
			this.miscellaneousDataService.save(lR, curricula.getId());
			this.miscellaneousDataService.flush();
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
				//			B: Test Positivo: Rooky borra MiscellaneousData 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"hacker2", null
			}, {
				//			A: Acme RookyRank Req. 17 -> Rookys can manage their history
				//			B: Test Negativo: Company intenta borrar MiscellaneousData 
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
			final MiscellaneousRecord lRec = this.curriculaService.findCurriculaByRooky(hacker.getId()).iterator().next().getMiscellaneous().iterator().next();
			this.miscellaneousDataService.delete(lRec);
			this.miscellaneousDataService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
