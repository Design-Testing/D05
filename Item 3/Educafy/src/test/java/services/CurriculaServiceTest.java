
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curricula;
import domain.PersonalData;
import domain.Rooky;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CurriculaServiceTest extends AbstractTest {

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private RookyService		hackerService;

	@Autowired
	private PersonalDataService	personalDataService;


	@Test
	public void driver() {
		final Object testingData[][] = {
			//				A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
			//				B: Test Positivo: Rooky crea curricula
			//				C: 100% Recorre 80 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"hacker1", false, null
			}
			//				A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
			//				B: Test Negativo: una compañía intenta crear una curricula
			//				C: 10% Recorre 8 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			, {
				"company1", false, IllegalArgumentException.class
			},
			//				A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
			//				B: Test Positivo: Rooky borra su curricula
			//				C: 100% Recorre 64 de las 64 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"hacker2", true, null
			},
			//				A: Acme RookyRank Req. 17 -> Rookys can manage their curricula
			//				B: Test Negativo: Una comapñía intenta borrar una curricula
			//				C: 12,5% Recorre 8 de las 64 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"company1", true, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateDeleteSave((String) testingData[i][0], (Boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCreateDeleteSave(final String user, final Boolean delete, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(user);
			if (delete) {
				final Rooky hacker = this.hackerService.findByPrincipal();
				final Curricula curricula = this.curriculaService.findCurriculaByRooky(hacker.getId()).iterator().next();
				this.curriculaService.delete(curricula);
			} else {
				final Curricula curricula = this.curriculaService.create();
				PersonalData pd = curricula.getPersonalRecord();
				pd = this.personalDataService.save(pd);
				curricula.setPersonalRecord(pd);
				final Curricula saved = this.curriculaService.save(curricula);
				Assert.isTrue(saved.getId() != 0);
			}
			this.curriculaService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
