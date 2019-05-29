
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Lesson;
import domain.Reservation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ReservationServiceTest extends AbstractTest {

	// Services

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private ReservationService	reservationService;

	@Autowired
	private CreditCardService	creditCardService;


	/* ========================= Test Create and Save Comment =========================== */

	@Test
	public void driverCreateAndSaveComment() {

		final Object testingData[][] = {
			//			{
			//				//				A: Educafy Crear y guardar una Reservation
			//				//				B: Test Positivo: Creación correcta de una Reservation
			//				//				C: % Recorre 196 de la 196 lineas posibles
			//				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
			//				"student1", "lesson1", 2, "creditCard1", null
			//			},
			{
				//				A: Educafy Crear y guardar una Reservation
				//				B: Test Negativo: Creación incorrecta de una Reservation, hoursWeek null
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "lesson1", null, "creditCard1", NullPointerException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String student, final String lesson, final int hoursWeek, final String creditCard, final Class<?> expected) {

		Class<?> caught;
		Reservation reservation;
		caught = null;
		final Lesson les = this.lessonService.findOne(super.getEntityId(lesson));
		final CreditCard card = this.creditCardService.findOne(super.getEntityId(creditCard));
		try {
			this.authenticate(student);
			reservation = this.reservationService.create();
			reservation.setHoursWeek(hoursWeek);
			reservation.setCreditCard(card);
			reservation.setLesson(les);
			this.reservationService.save(reservation);
			this.reservationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
