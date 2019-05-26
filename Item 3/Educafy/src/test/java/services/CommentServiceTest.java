
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Assesment;
import domain.Comment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	// Services
	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private CommentService		commentService;


	/* ========================= Test Create and Save Comment =========================== */

	@Test
	public void driverCreateAndSaveComment() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar un comment
				//				B: Test Positivo: Creación correcta de un comment
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "assesment1", "Text of comment 1", "2019/05/12 20:45", null
			}, {
				//				A: Educafy Crear y guardar un assesment
				//				B: Test Negativo: Creación incorrecta de un comment, text vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "assesment1", " ", "2019/05/12 20:45", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String teacher, final String assesment, final String text, final String moment, final Class<?> expected) {

		Class<?> caught;
		Comment comment;
		final Assesment ass = this.assesmentService.findOne(super.getEntityId(assesment));
		Date momentt;

		caught = null;

		try {
			this.authenticate(teacher);

			comment = this.commentService.create();
			comment.setText(text);
			momentt = (new SimpleDateFormat("yyyy/MM/dd HH:mm")).parse(moment);
			comment.setMoment(momentt);
			comment.setAssesment(ass);

			final Comment saved = this.commentService.save(comment, ass.getId());
			this.commentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
