
package services;

import java.text.SimpleDateFormat;
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
import domain.EducationRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EducationRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private EducationRecordService	educationRecordService;

	@Autowired
	private CurriculumService		curriculumService;


	@Test
	public void driverCreateSave() {
		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "Derecho", "Universidad de Málaga", 5, "2014-09-15", "2018-09-20", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "Física", "Universidad de Sevilla", 5, "2014-09-15", "2018-09-20", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Negativo: Un member intenta crear una EducationRecord sin grado
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher2", null, "institution1", 5, "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord con institución en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "degree1", "", 3, "2014-09-15", "2018-09-20", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord con fecha de finalización anterior a fecha de inicio
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "degree1", "institution2", 3, "2014-09-15", "2013-09-20", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateCreateSave((String) testingRecord[i][0], (String) testingRecord[i][1], (String) testingRecord[i][2], (Integer) testingRecord[i][3], (String) testingRecord[i][4], (String) testingRecord[i][5], (Class<?>) testingRecord[i][6]);
	}

	protected void templateCreateSave(final String user, final String degree, final String institution, final Integer mark, final String startDate, final String endDate, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final Curriculum curriculum = this.curriculumService.findOne(this.getEntityId("curriculum1"));
			final EducationRecord lRec = this.educationRecordService.create();
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
			final EducationRecord lRecSaved = this.educationRecordService.save(lRec, curriculum.getId());
			Assert.isTrue(lRecSaved.getId() != 0);
			this.educationRecordService.flush();
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
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "Ingeniería de la Slud", "Universidad de Sevilla", 5, "2014-09-15", "2018-09-20", "educationRecord1", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher3", "Ingeniería Química", "Pablo Olavide", 5, "2014-09-15", "2018-09-20", "educationRecord4", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Negativo: Un member intenta crear una EducationRecord sin grado
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher2", null, "institution1", 5, "2014-09-15", "2018-09-20", "educationRecord1", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord con institución en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "degree1", "", 3, "2014-09-15", "2018-09-20", "educationRecord1", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher crea EducationRecord con fecha de finalización anterior a fecha de inicio
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher1", "degree1", "institution2", 3, "2014-09-15", "2013-09-20", "educationRecord1", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Negativo: Teacher intena editar un EducationRecord que no está en draft mode
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"teacher2", "degree1", "institution1", 5, "2014-09-15", "2018-09-20", "educationRecord3", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateEdit((String) testingRecord[i][0], (String) testingRecord[i][1], (String) testingRecord[i][2], (Integer) testingRecord[i][3], (String) testingRecord[i][4], (String) testingRecord[i][5], (String) testingRecord[i][6],
				(Class<?>) testingRecord[i][7]);
	}

	private void templateEdit(final String user, final String degree, final String institution, final Integer mark, final String startDate, final String endDate, final String educationRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final EducationRecord lR = this.educationRecordService.findOne(this.getEntityId(educationRecord));
			final Curriculum curriculum = this.curriculumService.findCurriculumByEducationRecord(lR.getId());
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
			this.educationRecordService.save(lR, curriculum.getId());
			this.educationRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverDelete() {

		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher borra EducationRecord 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "educationRecord1", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher borra EducationRecord 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher3", "educationRecord4", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher intenar borrar EducationRecord que no esta en drft mode
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "educationRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Negativo: Teacher intena borrar education Record que no es suyo, aun estando en modo draft
				//			C: 10,25% Recorre 8 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher2", "educationRecord1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateDelete((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateDelete(final String actor, final String educationRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final EducationRecord lRec = this.educationRecordService.findOne(this.getEntityId(educationRecord));
			this.educationRecordService.delete(lRec);
			this.educationRecordService.flush();
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
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher pasa a final EducationRecord en draft mode
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "educationRecord1", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher pasa a final EducationRecord en draft mode
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher3", "educationRecord4", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher intenar pasar a final EducationRecord que ya etsá en final mode
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "educationRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher intenar pasar a final EducationRecord que ya etsá en final mode
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher13", "educationRecord5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateToFinal((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateToFinal(final String actor, final String educationRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final EducationRecord lRec = this.educationRecordService.findOne(this.getEntityId(educationRecord));
			this.educationRecordService.toFinal(lRec);
			this.educationRecordService.flush();
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
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Teacher no puede certificar un registro
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "educationRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Un certificador certifica un registro en modo final
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "educationRecord2", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Un certificador certifica un registro en modo final
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "educationRecord5", null
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Un certificador intenta certificar un registro en modo draft
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "educationRecord1", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 17 -> Teachers can manage their history
				//			B: Test Positivo: Un certificador intenta certificar un registro que ya está certificado
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "educationRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateCertify((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateCertify(final String actor, final String educationRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final EducationRecord lRec = this.educationRecordService.findOne(this.getEntityId(educationRecord));
			this.educationRecordService.certify(lRec);
			this.educationRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
