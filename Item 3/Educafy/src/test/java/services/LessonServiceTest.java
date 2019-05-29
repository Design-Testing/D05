
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Lesson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LessonServiceTest extends AbstractTest {

	// Services

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private ReservationService	reservationService;


	/* ========================= Test Create and Save Comment =========================== */

	@Test
	public void driverCreateAndSaveComment() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar una Lesson
				//				B: Test Positivo: Creación correcta de una Lesson
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "subject1", "Lesson test", "Description test", null
			}, {
				//				A: Educafy Crear y guardar una Lesson
				//				B: Test Negativo: Creación incorrecta de una Lesson, title vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "subject1", "", "Description test", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String teacher, final String subject, final String title, final String description, final Class<?> expected) {

		Class<?> caught;
		Lesson lesson;
		caught = null;

		try {
			this.authenticate(teacher);
			lesson = this.lessonService.create();
			lesson.setTitle(title);
			lesson.setDescription(description);
			this.lessonService.save(lesson, super.getEntityId(subject));
			this.lessonService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
