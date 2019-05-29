
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Finder;
import domain.Lesson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService	finderService;


	@Test
	public void driverCreateAndSaveFinder() {
		final Object testingData[][] = {
			{
				//				A: Acme RookyRank R.17.2. Manage finder
				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda vacios)
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "", null, null, null, null, new ArrayList<Lesson>(), null
			}, {
				//				A: Acme RookyRank R.17.2. Manage finder
				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda vacios menos keyword)
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "lesson", null, null, null, null, new ArrayList<Lesson>(), null
			}, {
				//				A: Acme RookyRank R.17.2. Manage finder
				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda vacios menos min salary)
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "", "teacher1", null, null, null, new ArrayList<Lesson>(), null
			}, {
				//				A: Acme RookyRank R.17.2. Manage finder
				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda vacios menos max salary)
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "", null, "subject1", null, null, new ArrayList<Lesson>(), null
			}, {
				//				A: Acme RookyRank R.17.2. Manage finder
				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda vacios menos min deadline)
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "", null, null, "1ESO", null, new ArrayList<Lesson>(), null
			}, {
				//				A: Acme RookyRank R.17.2. Manage finder
				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda vacios menos max deadline)
				//				C: % Recorre 31 de las 31 lineas posibles
				//				D: % cobertura de datos= 8/32
				"student1", "", null, null, null, "2019-05-05 20:00", new ArrayList<Lesson>(), null
			//			}, {
			//				//				A: Acme RookyRank R.17.2. Manage finder
			//				//				B: Test Positivo: Un hacker puede actualizar su finder (todos los parametros de busqueda)
			//				//				C: % Recorre 31 de las 31 lineas posibles
			//				//				D: % cobertura de datos= 8/32
			//				"hacker1", "jotaunit", "200.0", "1000.0", "2019-02-02 10:00", "2019-02-02 20:00", new ArrayList<Position>(), null
			//			}, {
			//				//				A: Acme RookyRank R.17.2. Manage finder
			//				//				B: Test Negativo: Un hacker puede actualizar su finder. Ningun actor ajeno a el puede hacerlo
			//				//				C: % Recorre 6 de las 31 lineas posibles
			//				//				D: % cobertura de datos= 8/32
			//				"company1", "jotaunit", "200.0", "1000.0", "2019-02-02 10:00", "2019-02-02 20:00", new ArrayList<Position>(), IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Collection<Lesson>) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateCreateAndSave(final String principal, final String keyword, final String teacherName, final String subjectName, final String subjectLevel, final String creationDate, final Collection<Lesson> lessons, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(principal);

			Date creationD;
			if (creationDate != null)
				creationD = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(creationDate);
			else
				creationD = null;

			final Finder finder = this.finderService.findStudentFinder();
			finder.setKeyword(keyword);
			finder.setLessons(lessons);
			finder.setSubjectLevel(subjectLevel);
			finder.setSubjectName(subjectName);
			finder.setCreationDate(creationD);
			this.finderService.save(finder);
			this.finderService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverFindFinder() {
		final Object testingData[][] = {
			{
				//				A: Acme RookyRank R.17.2. Listing finder's contents
				//				B: Test Positivo: Un hacker puede listar el contenido de su finder
				//				C: % Recorre 34 de las 34 lineas posibles
				//				D: % cobertura de datos= 2/2
				"hacker1", null
			}, {
				//				A: Acme RookyRank R.17.2. Listing finder's contents
				//				B: Test Positivo: Un HACKER puede listar el contenido de su finder
				//				C: % Recorre 14 de las 31 lineas posibles
				//				D: % cobertura de datos= 2/2
				"company1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFind((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	private void templateFind(final String principal, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(principal);
			final Finder finder = this.finderService.findStudentFinder();
			this.finderService.find(finder);
			this.finderService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverClearFinder() {
		final Object testingData[][] = {
			{
				//				A: Acme RookyRank R.17.2. Clear finder
				//				B: Test Positivo: Un hacker puede limpiar su finder
				//				C: % Recorre 35 de las 35 lineas posibles
				//				D: % cobertura de datos= 2/2
				"hacker1", null
			}, {
				//				A: Acme RookyRank R.17.2. Clear finder
				//				B: Test Negativo: Un HACKER puede limpiar su finder
				//				C: % Recorre 14 de las 35 lineas posibles
				//				D: % cobertura de datos= 2/2
				"company1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateClear((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	private void templateClear(final String principal, final Class<?> expected) {

		Class<?> caught = null;
		try {
			this.authenticate(principal);
			final Finder finder = this.finderService.findStudentFinder();
			this.finderService.clear(finder);
			this.finderService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		super.checkExceptions(expected, caught);
	}

}
