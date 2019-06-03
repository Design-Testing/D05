
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Assesment;
import domain.Lesson;
import domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AssesmentServiceTest extends AbstractTest {

	// Services
	@Autowired
	private StudentService		studentService;

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private LessonService		lessonService;


	/* ========================= Test Create and Save Assesment =========================== */

	@Test
	public void driverCreateAndSaveAssesment() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar un assesment
				//				B: Test Positivo: Creación correcta de un assesment
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "lesson1", "4", "Bien Trabajado", null
			}, {
				//				A: Educafy Crear y guardar un assesment
				//				B: Test Negativo: Creación incorrecta de un assesment, score vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "lesson1", "", "Bien Trabajado", NumberFormatException.class
			}, {
				//				A: Educafy Crear y guardar un assesment
				//				B: Test Negativo: Creación incorrecta de un assesment, comment vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "lesson1", "3", "", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String username, final String lesson, final String score, final String comment, final Class<?> expected) {

		Class<?> caught;
		Assesment assesment;
		final Student student = this.studentService.findOne(super.getEntityId(username));
		final Lesson newLesson = this.lessonService.findOne(super.getEntityId(lesson));

		caught = null;

		try {
			this.authenticate(username);

			assesment = this.assesmentService.create();
			assesment.setScore(new Integer(score));
			assesment.setComment(comment);
			assesment.setStudent(student);
			assesment.setLesson(newLesson);
			final Assesment saved = this.assesmentService.save(assesment, newLesson.getId());
			this.assesmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Assesment =========================== */

	@Test
	public void driverEditAssesment() {

		final Object testingData[][] = {
			{
				//				A: Educafy - Editar un assesment
				//				B: Test Positivo: Edición correcta de un assesment
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"assesment1", "student1", "lesson1", "4", "Bien Trabajado", null
			}, {
				//				A: Educafy - Editar un assesment
				//				B: Test Negativo: Edición incorrecta de un assesment con score en blanco
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"assesment1", "student1", "lesson1", "", "Bien Trabajado", NumberFormatException.class
			}, {
				//				A: Educafy - Editar un assesment
				//				B: Test Negativo: Edición incorrecta de un assesment con comment en blanco
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"assesment1", "student1", "lesson1", "4", "", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	private void templateEdit(final String assesment, final String username, final String lesson, final String score, final String comment, final Class<?> expected) {

		Class<?> caught;
		Assesment res;
		res = this.assesmentService.findOne(this.getEntityId(assesment));
		final Student student = this.studentService.findOne(super.getEntityId(username));
		final Lesson newLesson = this.lessonService.findOne(super.getEntityId(lesson));

		caught = null;

		try {
			this.authenticate(username);

			res.setScore(new Integer(score));
			res.setComment(comment);
			res.setStudent(student);
			res.setLesson(newLesson);
			final Assesment saved = this.assesmentService.save(res, newLesson.getId());
			this.assesmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
