
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Exam;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ExamServiceTest extends AbstractTest {

	// Services

	@Autowired
	private ExamService			examService;

	@Autowired
	private ReservationService	resservationService;


	/* ========================= Test Create and Save Comment =========================== */

	@Test
	public void driverCreateAndSaveComment() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar un exam
				//				B: Test Positivo: Creación correcta de un exam
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "reservation1", "Exam test", null
			}, {
				//				A: Educafy Crear y guardar un exam
				//				B: Test Negativo: Creación incorrecta de un exam, title vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "reservation1", "", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateAndSave(final String teacher, final String reservation, final String title, final Class<?> expected) {

		Class<?> caught;
		Exam exam;
		caught = null;

		try {
			this.authenticate(teacher);
			exam = this.examService.create();
			exam.setTitle(title);
			this.examService.save(exam, super.getEntityId(reservation));
			this.examService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
