
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
import domain.Teacher;

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
	public void driver() {
		final Object testingRecord[][] = {
			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher crea curriculum
			//				C: 100% Recorre 80 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"teacher1", false, null
			}
			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Negativo: un estudiante intenta crear una curriculum
			//				C: 10% Recorre 8 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			/*
			 * , {
			 * "student1", false, IllegalArgumentException.class
			 * },
			 */
			//				A: Estudify Req. 17 -> Teachers can manage their curriculum
			//				B: Test Positivo: Teacher borra su curriculum
			//				C: 100% Recorre 64 de las 64 lineas posibles
			//				D: cobertura de datos=1/3
			/*{
				"teacher2", true, null
			},*/
		//				A: Estudify Req. 17 -> Teachers can manage their curriculum
		//				B: Test Negativo: Un estudiante intenta borrar una curriculum
		//				C: 12,5% Recorre 8 de las 64 lineas posibles
		//				D: cobertura de datos=1/3
		/*
		 * {
		 * "student1", true, IllegalArgumentException.class
		 * },
		 */
		};
		for (int i = 0; i < testingRecord.length; i++)
			this.templateCreateDeleteSave((String) testingRecord[i][0], (Boolean) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}
	protected void templateCreateDeleteSave(final String user, final Boolean delete, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(user);
			if (delete) {
				final Teacher teacher = this.teacherService.findByPrincipal();
				final Curriculum curriculum = this.curriculumService.findCurriculumByTeacher(teacher.getId());
				this.curriculumService.delete(curriculum);
			} else {
				final Curriculum curriculum = this.curriculumService.create();
				PersonalRecord pd = curriculum.getPersonalRecord();
				pd = this.personalRecordService.save(pd);
				curriculum.setPersonalRecord(pd);
				final Curriculum saved = this.curriculumService.save(curriculum);
				Assert.isTrue(saved.getId() != 0);
			}
			this.curriculumService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
