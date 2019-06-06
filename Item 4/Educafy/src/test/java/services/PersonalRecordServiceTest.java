
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
public class PersonalRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private PersonalRecordService	personalRecordService;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private CurriculumService		curriculumService;


	@Test
	public void driverCreateSave() {
		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Teachers can manage their curriculum
				//			B: Test Positivo: Teacher creaPersonalRecord
				//			C: % Recorre 17 de las 17 (9+8) lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", "teacher 1 fullname", "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", null
			}, {
				//			A: Educafy Req. 20.8 -> Teachers can manage their curriculum
				//			B: Test Positivo: Teacher creaPersonalRecord
				//			C: % Recorre 17 de las 17 (9+8) lineas posibles
				//			D: cobertura de datos=4/54
				"teacher2", "teacher 2 fullname", "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", null
			}, {
				//			A: Educafy Req. 20.8 -> Teachers can manage their curriculum
				//			B: Test Negativo: no introduce una url como github link
				//			C: % Recorre 16 de las 17 (9+8) lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", "teacher 1 fullname", "statement test", "github profile", "http://testPhoto.com", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Teachers can manage their curriculum
				//			B: Test Negativo: no introduce foto en formato url
				//			C: % Recorre 8 de las 17 (9+8) lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", "teacher 1 fullname", "statement test", "http://www.github.com/test", "photo no url", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Teachers can manage their curriculum
				//			B: Test Negativo: no introduce nombre completo
				//			C: % Recorre 16 de las 17 (9+8) lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", null, "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingRecord.length; i++)
			this.templateCreateSave((String) testingRecord[i][0], (String) testingRecord[i][1], (String) testingRecord[i][2], (String) testingRecord[i][3], (String) testingRecord[i][4], (String) testingRecord[i][5], (Class<?>) testingRecord[i][6]);
	}
	protected void templateCreateSave(final String user, final String fullName, final String statement, final String github, final String photo, final String linkedin, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final PersonalRecord incRec = this.personalRecordService.create();
			incRec.setFullName(fullName);
			incRec.setStatement(statement);
			incRec.setLinkedin(linkedin);
			incRec.setGithub(github);
			incRec.setPhoto(photo);
			final PersonalRecord incRecSaved = this.personalRecordService.save(incRec);
			Assert.isTrue(incRecSaved.getId() != 0);
			this.personalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Positivo: Teacher edita PersonalRecord correctamente
				//			C: % Recorre 9 de las 9 lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", "teacher1", "teacher 1 fullname", "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Positivo: Teacher edita PersonalRecord correctamente
				//			C: % Recorre 9 de las 9 lineas posibles
				//			D: cobertura de datos=4/54
				"teacher3", "teacher3", "teacher 3 fullname", "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: no introduce una url como github link
				//			C: % Recorre 8 de las 9 lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", "teacher1", "teacher 1 fullname", "statement test", "github profile", "http://testPhoto.com", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: un teacher intenta editar personal record de otro
				//			C: % Recorre 6 de las 9 lineas posibles
				//			D: cobertura de datos=4/54
				"teacher2", "teacher1", "teacher 1 fullname", "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", java.lang.IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: no introduce nombre completo
				//			C: % Recorre 8 de las 9 lineas posibles
				//			D: cobertura de datos=4/54
				"teacher1", "teacher1", null, "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: teacher intenta editar personal record en final mode
				//			C: % Recorre 5 de las 9 lineas posibles
				//			D: cobertura de datos=4/54
				"teacher2", "teacher2", "teacher 2 fullname", "statement test", "http://www.github.com/test", "http://testPhoto.com", "http://www.linkedin.com/test", java.lang.IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingRecord.length; i++)
			this.templateEdit((String) testingRecord[i][0], (String) testingRecord[i][1], (String) testingRecord[i][2], (String) testingRecord[i][3], (String) testingRecord[i][4], (String) testingRecord[i][5], (String) testingRecord[i][6],
				(Class<?>) testingRecord[i][7]);
	}

	private void templateEdit(final String user, final String userProp, final String fullName, final String statement, final String github, final String photo, final String linkedin, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Teacher owner = this.teacherService.findOne(this.getEntityId(userProp));
			final PersonalRecord iR;
			final Curriculum curriculum = this.curriculumService.findCurriculumByTeacher(owner.getId());
			iR = curriculum.getPersonalRecord();
			iR.setFullName(fullName);
			iR.setStatement(statement);
			iR.setLinkedin(linkedin);
			iR.setGithub(github);
			iR.setPhoto(photo);
			curriculum.setPersonalRecord(iR);
			this.personalRecordService.save(iR);
			this.personalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverToFinal() {

		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Positivo: Teacher pasa a final PersonalRecord en draft mode
				//			C: % Recorre 8 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "personalRecord1", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Positivo: Teacher pasa a final PersonalRecord en draft mode
				//			C: % Recorre 8 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher3", "personalRecord3", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: Teacher intenar pasar a final PersonalRecord que ya etsá en final mode
				//			C: % Recorre 5 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher2", "personalRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: Teacher intenar pasar a final PersonalRecord que ya está en final mode
				//			C: % Recorre 5 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher13", "personalRecord5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateToFinal((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateToFinal(final String actor, final String personalRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final PersonalRecord lRec = this.personalRecordService.findOne(this.getEntityId(personalRecord));
			this.personalRecordService.toFinal(lRec);
			this.personalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverCertify() {

		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Positivo: Un certificador certifica un registro en modo final
				//			C: % Recorre 7 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "personalRecord2", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: Un certificador certifica un registro en modo final
				//			C: % Recorre 7 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "personalRecord5", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: Un certificador intenta certificar un registro en modo draft
				//			C: % Recorre 4 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "personalRecord1", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su currículum...
				//			B: Test Negativo: Teacher no puede certificar un registro
				//			C: % Recorre 1 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "personalRecord2", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateCertify((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}
	private void templateCertify(final String actor, final String personalRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final PersonalRecord lRec = this.personalRecordService.findOne(this.getEntityId(personalRecord));
			this.personalRecordService.certify(lRec);
			this.personalRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
