
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

	"classpath:spring/junit.xml"

})
@Transactional
public class SubjectServiceTest extends AbstractTest {

	@Autowired
	private SubjectService	subjectService;


	/* ========================= Test Create and Save =========================== */

	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar una asignatura
				//				B: Test Positivo: Creación correcta de una asignatura
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "subject1", "asignatura1", "Description1", "Descripción1", "1ESO", null
			}, {
				//				A: Educafy Crear y guardar una asignatura
				//				B: Test Negativo: Creación incorrecta de una asignatura, nameEs vacio
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "subject1", "", "Description1", "Descripción1", "1ESO", ConstraintViolationException.class
			}, {
				//				A: Educafy Crear y guardar una asignatura
				//				B: Test Negativo: Creación incorrecta de una asignatura, descriptionEn vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "subject1", "asignatura1", "", "Descripción1", "1ESO", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateCreateAndSave(final String admin, final String nameEn, final String nameEs, final String descriptionEn, final String descriptionEs, final String level, final Class<?> expected) {

		Class<?> caught;
		final Subject subject;
		caught = null;

		try {
			this.authenticate(admin);
			subject = this.subjectService.create();
			subject.setNameEn(nameEn);
			subject.setNameEs(nameEs);
			subject.setDescriptionEn(descriptionEn);
			subject.setDescriptionEs(descriptionEs);
			subject.setLevel(level);
			this.subjectService.save(subject);
			this.subjectService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
