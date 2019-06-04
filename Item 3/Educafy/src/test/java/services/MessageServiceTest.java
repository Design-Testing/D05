
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

import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

	"classpath:spring/junit.xml"

})
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private FolderService	folderService;


	/* ========================= Test Message Send =========================== */

	@Test
	public void driverSendMessage() {

		final Object testingData[][] = {
			{
				//				A: Un actor envía un mensaje
				//				B: Test Positivo: Envía el mensaje correctamente
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "2019/05/12 20:45", "subject1", "Buenas tardes", "HIGH", new ArrayList<>(), null
			}, {
				//				A: Un actor envía un mensaje
				//				B: Test Negativo: Envía el mensaje mal, sin subject
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "2019/05/12 20:45", "", "Buenas tardes", "HIGH", new ArrayList<>(), ConstraintViolationException.class
			}, {
				//				A: Un actor envía un mensaje
				//				B: Test Negativo: Envía el mensaje mal, sin prioridad
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "2019/05/12 20:45", "subject1", "Buenas tardes", "", new ArrayList<>(), ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSend((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<Actor>) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateSend(final String admin, final String moment, final String subject, final String body, final String priority, final Collection<Actor> recipients, final Class<?> expected) {

		Class<?> caught;
		final Message message;

		Date m;

		caught = null;

		try {
			this.authenticate(admin);
			final Actor principal = this.actorService.findByPrincipal();
			recipients.add(principal);

			message = this.messageService.create();
			m = (new SimpleDateFormat("yyyy/MM/dd HH:mm")).parse(moment);
			message.setMoment(m);
			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);
			message.setSender(principal);
			message.setRecipients(recipients);

			this.messageService.send(message);
			this.messageService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
