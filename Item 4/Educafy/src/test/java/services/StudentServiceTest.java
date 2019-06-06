
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

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Student;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class StudentServiceTest extends AbstractTest {

	// Services
	@Autowired
	private StudentService		studentService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FinderService		finderService;


	/* ========================= Test Login Student =========================== */
	@Test
	public void driverLoginStudent() {

		final Object testingData[][] = {
			{
				//				A: Educafy Login Use Case
				//				B: Test Positivo: Un estudiante puede loguearse correctamente
				//				C: % Recorre 23 de la 23 lineas posibles
				//				D: % cobertura de datos= 2/2
				"student1", null
			}, {
				//				A: Educafy Login User Non registered
				//				B: Test Positivo: Un usuario no registrado no puede logearse
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: % cobertura de datos= 2/2
				"EstudianteNoRegistrado", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateLogin(final String studentUsername, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(studentUsername);
			this.unauthenticate();
			this.studentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Create and Save Student =========================== */

	@Test
	public void driverCreateAndSaveStudent() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Martínez");
		final Collection<String> surnames1 = new ArrayList<>();
		surnames1.add("Martínez");
		surnames1.add("");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Martínez");

		final Object testingData[][] = {
			{
				//				A: Educafy Req. 11.1. Create user accounts for new student s
				//				B: Test Positivo: Creación correcta de un student
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student43", "student43", "Student1", surnames, "garcia@us.es", "647307406", null
			}, {
				//				A: Educafy Req. 11.1. Create user accounts for new student s
				//				B: Test Negativo: Creación incorrecta de un student con name en blanco
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student43", "student43", "", surnames, "lanzas@gmail.com", "+34647307406", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		Student student;
		final UserAccount userAccount;

		caught = null;

		try {
			student = this.studentService.create();
			userAccount = new UserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.STUDENT);
			authorities.add(auth);
			userAccount.setAuthorities(authorities);
			student.setUserAccount(userAccount);
			student.setName(name);
			student.setSurname(surname);
			student.setEmail(email);
			student.setPhone(phone);
			student.setFinder(this.finderService.createForNewStudent());
			student = this.studentService.save(student);
			super.unauthenticate();
			this.studentService.flush();
			this.userAccountService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}
	/* ========================= Test Edit and Save Student =========================== */

	@Test
	public void driverEditAndSaveStudent() {
		final Collection<String> surnames = new ArrayList<>();
		surnames.add("Garcia");
		final Collection<String> surnames1 = new ArrayList<>();
		surnames1.add("Garcia");
		surnames1.add("");
		final Collection<String> surnames2 = new ArrayList<>();
		surnames2.add("Lanzas");
		final Object testingData[][] = {
			{
				//				A: Acme HackerRank Req. 11.1. Update student  profile
				//				B: Test Positivo: Creación correcta de un student
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "Student1", surnames, "garcia@us.es", "647307406", null
			}, {
				//				A: Acme HackerRank Req. 11.1. Update student  profile
				//				B: Test Negativo: Creación incorrecta de un student con name en blanco
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "", surnames, "lanzas@gmail.com", "+34647307406", ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAndSave((String) testingData[i][0], (String) testingData[i][1], (Collection<String>) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	private void templateEditAndSave(final String principal, final String name, final Collection<String> surname, final String email, final String phone, final Class<?> expected) {

		Class<?> caught;
		Student student;

		caught = null;

		try {
			super.authenticate(principal);
			student = this.studentService.findByPrincipal();
			final UserAccount userAccount = new UserAccount();
			student.setName(name);
			student.setSurname(surname);
			student.setEmail(email);
			student.setPhone(phone);
			student = this.studentService.save(student);
			this.studentService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
	}

}
