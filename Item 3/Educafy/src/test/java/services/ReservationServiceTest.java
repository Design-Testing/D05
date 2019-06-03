
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import utilities.AbstractTest;
import domain.Actor;
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
	private ActorService		actorService;

	@Autowired
	private CreditCardService	creditCardService;


	/* ========================= Test Create and Save =========================== */

	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar una Reservation
				//				B: Test Positivo: Creación correcta de una Reservation
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "lesson1", 2, "creditCard1", null
			}, {
				//				A: Educafy Crear y guardar una Reservation
				//				B: Test Negativo: Creación incorrecta de una Reservation, mas de 10 hoursWeek
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "lesson1", 12, "creditCard1", ConstraintViolationException.class
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
		try {
			this.authenticate(student);
			final CreditCard card = this.creditCardService.findOne(super.getEntityId(creditCard));
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

	@Test
	public void driverToAccepted() {

		final Object testingData[][] = {
			{
				//				A: Educafy cambiar status de una Reservation a ACCEPTED
				//				B: Test Positivo: --
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "reservation1", null
			}, {
				//				A: Educafy cambiar status de una Reservation a ACCEPTED
				//				B: Test Negativo: La reservation se en encuentra con status FINAL
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "reservation3", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateToAccepted((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateToAccepted(final String teacher, final String reservation, final Class<?> expected) {

		Class<?> caught;
		final Reservation res = this.reservationService.findOne(super.getEntityId(reservation));
		caught = null;

		try {
			this.authenticate(teacher);
			this.reservationService.toAcceptedMode(res);
			this.reservationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverToRejectedReviewing() {

		final Object testingData[][] = {
			{
				//				A: Educafy cambiar status de una Reservation a REJECTED
				//				B: Test Positivo: --
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "reservation1", "Explanation rejected", null
			}, {
				//				A: Educafy cambiar status de una Reservation a REJECTED
				//				B: Test Negativo: La explanation es una cadena vacia
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"teacher1", "reservation1", "", IllegalArgumentException.class
			}, {
				//				A: Educafy cambiar status de una Reservation a REVIEWING
				//				B: Test Positivo: --
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "reservation1", "Explanation reviewing", null
			}, {
				//				A: Educafy cambiar status de una Reservation a REVIEWING
				//				B: Test Negativo: La explanation es una cadena vacia
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "reservation1", "", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateToRejectedReviewing((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateToRejectedReviewing(final String principal, final String reservation, final String explanation, final Class<?> expected) {

		Class<?> caught;
		final Reservation res = this.reservationService.findOne(super.getEntityId(reservation));
		caught = null;

		try {
			this.authenticate(principal);
			final Actor actor = this.actorService.findByPrincipal();
			res.setExplanation(explanation);
			if (this.actorService.checkAuthority(actor, Authority.TEACHER))
				this.reservationService.toAcceptedMode(res);
			else if (this.actorService.checkAuthority(actor, Authority.STUDENT))
				this.reservationService.toReviewingMode(res);
			this.reservationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverToFinal() {

		final Object testingData[][] = {
			{
				//				A: Educafy cambiar status de una Reservation a FINAL
				//				B: Test Positivo: --
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "reservation2", null
			}, {
				//				A: Educafy cambiar status de una Reservation a FINAL
				//				B: Test Negativo: La reservation se en encuentra con status PENDING
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "reservation1", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateToFinal((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateToFinal(final String student, final String reservation, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			this.authenticate(student);
			this.reservationService.toFinalMode(super.getEntityId(reservation));
			this.reservationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverToDelete() {

		final Object testingData[][] = {
			{
				//				A: Educafy eliminar Reservation
				//				B: Test Positivo: --
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "reservation3", null
			}, {
				//				A: Educafy eliminar Reservation
				//				B: Test Negativo: La reservation se en encuentra con status PENDING
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"student1", "reservation1", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateToDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateToDelete(final String student, final String reservation, final Class<?> expected) {

		Class<?> caught;
		caught = null;
		final Reservation res = this.reservationService.findOne(super.getEntityId(reservation));
		try {
			this.authenticate(student);
			this.reservationService.delete(res);
			this.reservationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
