
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Teacher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TeacherServiceTest extends AbstractTest {

	// Services
	@Autowired
	private TeacherService	teacherService;


	/* ========================= Test Login Teacher =========================== */
	@Test
	public void driverLoginTeacher() {

		final Object testingData[][] = {
			{
				//				A: Educafy Login Use Case
				//				B: Test Positivo: Un teacher puede loguearse correctamente
				//				C: % Recorre 23 de la 23 lineas posibles
				//				D: % cobertura de datos= 2/2
				"teacher1", null
			}, {
				//				A: Educafy Login User Non registered
				//				B: Test Positivo: Un usuario no registrado no puede logearse
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: % cobertura de datos= 2/2
				"TeacherNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String teacherUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(teacherUsername);
			this.unauthenticate();
			this.teacherService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Administrator =========================== */

	@Test
	public void driverCreateAndSaveTeacher() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Fernández");
		final Collection<String> surnames1 = new ArrayList<>();
		surnames1.add("Fernández");
		surnames1.add("");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Romero");

		final Object testingData[][] = {
			{
				//				A: Educafy Req. 11.1. Create user accounts for new administrators
				//				B: Test Positivo: Creación correcta de un teacher
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "teacher1", "Teacher1", surnames, "teacher1@gmail.es", "+34647607400", "0.21", null
			}, {
				//				A: Educafy Req. 11.1. Create user accounts for new administrators
				//				B: Test Negativo: Creación incorrecta de un teacher con name en blanco
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "teacher1", "", surnames, "teacher1@gmail.es", "+34647607400", "0.21", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final String vat, final Class<?> expected) {

		// para crear un administrador tienes que tener autoridad de administrador

		Class<?> caught;
		Teacher teacher;
		final UserAccount userAccount;

		caught = null;

		try {
			teacher = this.teacherService.create();
			teacher.setName(name);
			teacher.setSurname(surname);
			teacher.setEmail(email);
			teacher.setPhone(phone);
			//			teacher.setVat(new Double(vat));
			userAccount = teacher.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			teacher.setUserAccount(userAccount);
			teacher = this.teacherService.save(teacher);
			this.teacherService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Administrator =========================== */

	@Test
	public void driverEditAndSaveAdministrator() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Fernández");
		final Collection<String> surnames1 = new ArrayList<>();
		surnames1.add("Fernández");
		surnames1.add("");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Romero");

		final Object testingData[][] = {
			{
				//				A: Acme HackerRank Req. 11.1. Update administrator profile
				//				B: Test Positivo: Creación correcta de un admin
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "Teacher1Mod", surnames, "teacher1@gmail.es", "+34647607400", "0.21", null
			}, {
				//				A: Acme HackerRank Req. 11.1. Update administrator profile
				//				B: Test Negativo: Creación incorrecta de un admin con name en blanco
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "", surnames, "teacher1@gmail.es", "+34647607400", "0.21", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAndSave((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateEditAndSave(final String principal, final String name, final Collection<String> surname, final String email, final String phone, final String vat, final Class<?> expected) {

		// para crear un administrador tienes que tener autoridad de administrador

		Class<?> caught;
		Teacher teacher;

		caught = null;

		try {
			super.authenticate(principal);
			teacher = this.teacherService.findByPrincipal();
			teacher.setName(name);
			teacher.setSurname(surname);
			teacher.setEmail(email);
			teacher.setPhone(phone);
			//			teacher.setVat(new Double(vat));
			teacher = this.teacherService.save(teacher);
			this.teacherService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

}
