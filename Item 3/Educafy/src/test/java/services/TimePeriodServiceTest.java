
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.TimePeriod;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TimePeriodServiceTest extends AbstractTest {

	// Services

	@Autowired
	private TimePeriodService	timePeriodService;


	/* ========================= Test Create and Save =========================== */

	@Test
	public void driverEdit() {

		final Object testingData[][] = {
			{
				//				A: Educafy editar un TimePeriod
				//				B: Test Positivo: Edicion correcta de un TimePeriod
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "timePeriod1", 14, 15, 3, null
			}, {
				//				A: Educafy editar un TimePeriod
				//				B: Test Negativo: Editar introduciendo un tramo de mas de una hora
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "timePeriod1", 14, 16, 3, IllegalArgumentException.class
			}, {
				//				A: Educafy editar un TimePeriod
				//				B: Test Negativo: Editar introduciendo un tramo de mas de una hora
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "timePeriod1", 15, 14, 2, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	private void templateEdit(final String teacher, final String period, final Integer start, final Integer end, final Integer day, final Class<?> expected) {

		Class<?> caught;
		final TimePeriod res = this.timePeriodService.findOne(super.getEntityId(period));
		caught = null;

		try {
			this.authenticate(teacher);
			res.setStartHour(start);
			res.setEndHour(end);
			res.setDayNumber(day);
			this.timePeriodService.save(res);
			this.timePeriodService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
