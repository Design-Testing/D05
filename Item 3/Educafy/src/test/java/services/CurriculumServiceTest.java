
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curriculum;
import domain.PersonalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CurriculumServiceTest extends AbstractTest {

	@Autowired
	private CurriculumService		curriculumService;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private PersonalRecordService	personalRecordService;


	@Test
	public void driverCreate() {
		final Object testingRecord[][] = {
			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher crea curriculum
			//				C: 100% Recorre 80 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"teacher11", null
			},
			//			A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher crea curriculum
			//				C: 100% Recorre 80 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"teacher12", null
			}
			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Negativo: un estudiante intenta crear una curriculum
			//				C: 10% Recorre 8 de las 80 lineas posibles
			//				D: cobertura de datos=1/3

			, {
				"student1", IllegalArgumentException.class
			},

			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher intenta crear un curriculum teneindo ya uno
			//				C: 100% Recorre 64 de las 64 lineas posibles
			//				D: cobertura de datos=1/3

			{
				"teacher2", DataIntegrityViolationException.class
			}

		};
		for (int i = 0; i < testingRecord.length; i++)
			this.templateCreateSave((String) testingRecord[i][0], (Class<?>) testingRecord[i][1]);
	}
	protected void templateCreateSave(final String user, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(user);

			final Curriculum curriculum = this.curriculumService.create();
			PersonalRecord pd = curriculum.getPersonalRecord();
			pd = this.personalRecordService.save(pd);
			curriculum.setPersonalRecord(pd);
			final Curriculum saved = this.curriculumService.save(curriculum);
			Assert.isTrue(saved.getId() != 0);

			this.curriculumService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDelete() {
		final Object testingRecord[][] = {

			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher borra su curriculum
			//				C: 100% Recorre 64 de las 64 lineas posibles
			//				D: cobertura de datos=1/3

			{
				"teacher2", "curriculum2", null
			},
			//			A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher borra su curriculum
			//				C: 100% Recorre 64 de las 64 lineas posibles
			//				D: cobertura de datos=1/3

			{
				"teacher1", "curriculum1", null
			},

			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Negativo: Un profesor intenta borrar un curriculum que no es suyo
			//				C: 12,5% Recorre 8 de las 64 lineas posibles
			//				D: cobertura de datos=1/3

			{
				"teacher1", "curriculum2", IllegalArgumentException.class
			},
			//			A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Negativo: Un estudiante intenta borrar un curriculum
			//				C: 12,5% Recorre 8 de las 64 lineas posibles
			//				D: cobertura de datos=1/3

			{
				"student1", "curriculum1", IllegalArgumentException.class
			},

		};
		for (int i = 0; i < testingRecord.length; i++)
			this.templateDeleteSave((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	protected void templateDeleteSave(final String user, final String curriculum, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Curriculum curr = this.curriculumService.findOne(this.getEntityId(curriculum));
			this.curriculumService.delete(curr);
			this.curriculumService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
