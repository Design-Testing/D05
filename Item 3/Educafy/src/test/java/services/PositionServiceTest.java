
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PositionServiceTest extends AbstractTest {

	@Autowired
	private PositionService	positionService;


	@Test
	public void createAndSaveDriver() {

		final Object testingData[][] = {
			{
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Positivo: --
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, null
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Hacker intenta crear position
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"hacker1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, IllegalArgumentException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Usuario null intenta crear position
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				null, "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, IllegalArgumentException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con title campo vacío
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con title null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", null, "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con description campo vacío
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their problems.
				//B: Test Negativo: Error con description null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", null, "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their problems.
				//B: Test Negativo: Error con profile campo vacio
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "", new ArrayList<String>(), new ArrayList<String>(), 15000.0, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their problems.
				//B: Test Negativo: Error con salary negativo
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), -1.0, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their problems.
				//B: Test Negativo: Error con salary null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), null, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<String>) testingData[i][5], (Collection<String>) testingData[i][6],
				(Double) testingData[i][7], (Class<?>) testingData[i][8]);

	}

	protected void templateCreateSave(final String user, final String title, final String description, final String deadline, final String profile, final Collection<String> skills, final Collection<String> technologies, final Double salary,
		final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final Position pos = this.positionService.create();
			skills.add("skill");
			technologies.add("Tech");
			pos.setTitle(title);
			pos.setDescription(description);
			final Date dead = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(deadline);
			pos.setDeadline(dead);
			pos.setProfile(profile);
			pos.setSkills(skills);
			pos.setTechnologies(technologies);
			pos.setSalary(salary);
			final Position saved = this.positionService.save(pos);
			Assert.isTrue(saved.getId() != 0);
			this.positionService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void edit() {

		final Object testingData[][] = {
			{
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Positivo: --
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, null
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Company intenta editar position que no le pertenece
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, true, IllegalArgumentException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Hacker intenta editar position 
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"hacker1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, IllegalArgumentException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Usuario null intenta editar position 
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				null, "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, IllegalArgumentException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con title campo vacío
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con title null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", null, "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con description campo vacío
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con description null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", null, "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con deadline null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", null, "Profile", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, NullPointerException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con profile campo vacío
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "", new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con profile null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", null, new ArrayList<String>(), new ArrayList<String>(), 15000.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con salary negativo
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), -1.0, false, ConstraintViolationException.class
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Error con salary null
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", "Title", "Description", "2019-12-12 20:00", "Profile", new ArrayList<String>(), new ArrayList<String>(), null, false, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<String>) testingData[i][5], (Collection<String>) testingData[i][6],
				(Double) testingData[i][7], (Boolean) testingData[i][8], (Class<?>) testingData[i][9]);

	}

	protected void templateEdit(final String user, final String title, final String description, final String deadline, final String profile, final Collection<String> skills, final Collection<String> technologies, final Double salary, final Boolean prop,
		final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Position pos;
			if (!prop) {
				final Integer id = this.getEntityId("position6");
				pos = this.positionService.findOne(id);
			} else {
				final Integer id = this.getEntityId("position2");
				pos = this.positionService.findOne(id);
			}
			pos.setTitle(title);
			pos.setDescription(description);
			final Date dead = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(deadline);
			pos.setDeadline(dead);
			pos.setProfile(profile);
			pos.setSkills(skills);
			pos.setTechnologies(technologies);
			pos.setSalary(salary);
			this.positionService.save(pos);
			this.positionService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void edit2() {

		final Object testingData[][] = {
			{

				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Positivo: Position a FINAL mode
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", 1, null
			}, {

				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Positivo: Position a CANCELLED mode
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", 2, null
			}, {

				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Se intenta pasar position de CANCELLED a DRAFT mode
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company2", 3, IllegalArgumentException.class
			}, {

				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: No se puede pasar position a FINAL mode porque tiene menos de 2 problemas
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", 4, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit2((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateEdit2(final String user, final Integer option, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			if (option == 1) {
				final Integer id = this.getEntityId("position6");
				this.positionService.toFinalMode(id);
			} else if (option == 2) {
				final Integer id = this.getEntityId("position1");
				this.positionService.toCancelMode(id);
			} else if (option == 3) {
				final Integer id = this.getEntityId("position2");
				this.positionService.toCancelMode(id);
			} else if (option == 4) {
				final Integer id = this.getEntityId("position5");
				this.positionService.toFinalMode(id);
			}
			this.positionService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverDelete() {

		final Object testingData[][] = {
			{
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Positivo: --
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"company1", null
			}, {
				//A: Acme HackerRank Req. 9.1 -> Companys can manage their positions.
				//B: Test Negativo: Hacker intenta borrar position
				//C: 10,25% Recorre 8 de las 78 lineas posibles
				//D: cobertura de datos=1/3
				"hacker1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	private void templateDelete(final String actor, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final Integer id = this.getEntityId("position6");
			final Position pos = this.positionService.findOne(id);
			this.positionService.delete(pos);
			this.positionService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
