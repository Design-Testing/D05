
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	// Service under test ---------------------------------
	@Autowired
	private SocialProfileService	socialProfileService;


	// Tests ----------------------------------------------

	/* ========================= Test Create and Save Social Profile =========================== */

	//				A: Educafy Crear y guardar un social profile
	//				B: Test Positivo: Creación correcta de un social profile
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test
	public void testCreateAndSavePositive() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.create();

		socialProfile.setProfileLink("http://www.facebook.com/teacher1");
		socialProfile.setSocialNetwork("Facebook");
		socialProfile.setNick("nickTeacher1");

		final SocialProfile result = this.socialProfileService.save(socialProfile);
		Assert.notNull(result);

		this.socialProfileService.flush();
		super.unauthenticate();
	}

	//				A: Educafy Crear y guardar un social profile
	//				B: Test Negativo: Creación incorrecta de un social profile, socialNetwork vacío
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test(expected = ConstraintViolationException.class)
	public void testCreateAndSaveNegative() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.create();

		socialProfile.setProfileLink("http://www.facebook.com/teacher1");
		socialProfile.setSocialNetwork("");
		socialProfile.setNick("nickTeacher1");

		final SocialProfile result = this.socialProfileService.save(socialProfile);
		Assert.notNull(result);

		this.socialProfileService.flush();
		super.unauthenticate();
	}

	//				A: Educafy Crear y guardar un social profile
	//				B: Test Negativo: Creación incorrecta de un social profile, nick vacío
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test(expected = ConstraintViolationException.class)
	public void testCreateAndSaveNegative2() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.create();

		socialProfile.setProfileLink("http://www.facebook.com/teacher1");
		socialProfile.setSocialNetwork("Facebook");
		socialProfile.setNick("");

		final SocialProfile result = this.socialProfileService.save(socialProfile);
		Assert.notNull(result);

		this.socialProfileService.flush();
		super.unauthenticate();
	}

	/* ========================= Test Edit Social Profile =========================== */

	//				A: Educafy Editar un social profile
	//				B: Test Positivo: Modificación correcta de un social profile
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test
	public void testEditPositive() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileTeacher1"));
		socialProfile.setNick("nickTeacher1Modified");
		this.socialProfileService.save(socialProfile);

		final SocialProfile socialProfileModified = this.socialProfileService.findOne(super.getEntityId("socialProfileTeacher1"));
		Assert.isTrue(socialProfileModified.getNick().equals("nickTeacher1Modified"));

		this.socialProfileService.flush();
		super.unauthenticate();

	}

	//				A: Educafy Editar un social profile
	//				B: Test Negativo: Modificación incorrecta de un social profile, nick vacío
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test(expected = ConstraintViolationException.class)
	public void testEditNegative() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileTeacher1"));
		socialProfile.setNick(" ");
		this.socialProfileService.save(socialProfile);

		this.socialProfileService.flush();
		super.unauthenticate();

	}

	//				A: Educafy Editar un social profile
	//				B: Test Negativo: Modificación incorrecta de un social profile, socilNetwork vacía
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test(expected = ConstraintViolationException.class)
	public void testEditNegative2() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileTeacher1"));
		socialProfile.setSocialNetwork("");
		this.socialProfileService.save(socialProfile);

		this.socialProfileService.flush();
		super.unauthenticate();

	}

	/* ========================= Test Delete Social Profile =========================== */

	//				A: Educafy Borrar un social profile
	//				B: Test Positivo: Borrado correcto de un social profile
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test
	public void testDeletePositive() {
		super.authenticate("teacher1");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileTeacher1"));
		this.socialProfileService.delete(socialProfile);

		this.socialProfileService.flush();
		super.unauthenticate();
	}

	//				A: Educafy Borrar un social profile
	//				B: Test Negativo: Borrado incorrecto de un social profile, teacher no correspondiente al social profile
	//				C: % Recorre 61 de la 188 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos=67.50%

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNegative() {
		super.authenticate("teacher2");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileTeacher1"));
		this.socialProfileService.delete(socialProfile);

		this.socialProfileService.flush();
		super.unauthenticate();
	}

}
