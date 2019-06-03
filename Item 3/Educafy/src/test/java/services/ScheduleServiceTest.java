
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Schedule;
import domain.Teacher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

	"classpath:spring/junit.xml"

})
@Transactional
public class ScheduleServiceTest extends AbstractTest {

	@Autowired
	private ScheduleService	scheduleService;

	@Autowired
	private TeacherService	teacherService;


	/* ========================= Test Create and Save =========================== */

	@Test
	public void driverCreateAndSave() {
		final Collection<Boolean> monday = new ArrayList<>();
		final Collection<Boolean> tuesday = new ArrayList<>();
		final Collection<Boolean> wednesday = new ArrayList<>();
		final Collection<Boolean> thursday = new ArrayList<>();
		final Collection<Boolean> friday = new ArrayList<>();

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar un horario
				//				B: Test Positivo: Creación correcta del horario
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", monday, tuesday, wednesday, thursday, friday, null
			}, {
				//				A: Educafy Crear y guardar un horario
				//				B: Test Negativo: Creación incorrecta del horario, registrado como admin
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", monday, tuesday, wednesday, thursday, friday, IllegalArgumentException.class
			}, {
				//				A: Educafy Crear y guardar un horario
				//				B: Test Negativo: Creación incorrecta del horario, registrado como estudiante
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"studen1", monday, tuesday, wednesday, thursday, friday, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (Collection<Boolean>) testingData[i][1], (Collection<Boolean>) testingData[i][2], (Collection<Boolean>) testingData[i][3], (Collection<Boolean>) testingData[i][4],
				(Collection<Boolean>) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateCreateAndSave(final String teacher, final Collection<Boolean> monday, final Collection<Boolean> tuesday, final Collection<Boolean> wednesday, final Collection<Boolean> thursday, final Collection<Boolean> friday,
		final Class<?> expected) {

		Class<?> caught;
		Schedule schedule;
		caught = null;

		try {
			this.authenticate(teacher);
			final Teacher principal = this.teacherService.findByPrincipal();

			schedule = this.scheduleService.create();
			schedule.setMonday(monday);
			schedule.setTuesday(tuesday);
			schedule.setWednesday(wednesday);
			schedule.setThursday(thursday);
			schedule.setFriday(friday);
			schedule.setTeacher(principal);
			this.scheduleService.save(schedule);
			this.scheduleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
