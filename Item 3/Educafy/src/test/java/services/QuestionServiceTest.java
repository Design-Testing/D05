
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
import domain.Question;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class QuestionServiceTest extends AbstractTest {

	// Services

	@Autowired
	private ExamService		examService;

	@Autowired
	private QuestionService	questionService;


	/* ========================= Test Create and Save =========================== */

	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar un question
				//				B: Test Positivo: Creación correcta de un question
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "exam1", "Question 1", null
			}, {
				//				A: Educafy Crear y guardar un question
				//				B: Test Negativo: Creación incorrecta de un question, title vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "exam1", "", ConstraintViolationException.class
			}, {
				//				A: Educafy Crear y guardar un question
				//				B: Test Negativo: Creación incorrecta de un question, el examen donde se guarda no pertenece al teacher registrado
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "exam5", "Question test", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateAndSave(final String teacher, final String exam, final String title, final Class<?> expected) {

		Class<?> caught;
		Question question;
		final Exam examRes = this.examService.findOne(super.getEntityId(exam));
		caught = null;

		try {
			this.authenticate(teacher);
			question = this.questionService.create();
			question.setTitle(title);
			this.questionService.save(question, examRes.getId());
			this.questionService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
