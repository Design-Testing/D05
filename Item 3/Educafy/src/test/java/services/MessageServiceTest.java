
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
	//	@Test
	//	public void testFindAll() {
	//
	//		super.authenticate("brotherhood1");
	//
	//		final Collection<Message> messages = this.messageService.findAll();
	//
	//		Assert.notNull(messages);
	//
	//	}
	//
	//	@Test
	//	public void testFindOne() {
	//
	//		super.authenticate("brotherhood1");
	//
	//		final Message message = (Message) this.messageService.findAll().toArray()[0];
	//
	//		final Message retrieved = this.messageService.findOne(message.getId());
	//
	//		Assert.isTrue(message.equals(retrieved));
	//
	//	}
	//
	//	@Test
	//	public void testFindMessagesByFolderIdAndUserId() {
	//
	//		super.authenticate("member1");
	//
	//		final Actor principal = this.actorService.findByPrincipal();
	//
	//		final Folder outbox = this.folderService.findOutboxByUserId(principal.getUserAccount().getId());
	//
	//		final int id = outbox.getId();
	//
	//		final Collection<Message> messages = this.messageService.findAllByFolderIdAndUserId(id, principal.getUserAccount().getId());
	//
	//		Assert.isTrue(outbox.getMessages().containsAll(messages));
	//
	//	}
	//
	//	@Test
	//	public void testSendMessage() {
	//
	//		super.authenticate("member1");
	//
	//		final Actor sender = this.actorService.findByPrincipal();
	//
	//		final UserAccount user = sender.getUserAccount();
	//
	//		final Message m = this.messageService.create();
	//
	//		final Collection<Actor> recipients = new ArrayList<>();
	//
	//		final Actor recipient = sender;
	//
	//		recipients.add(recipient);
	//
	//		m.setRecipients(recipients);
	//
	//		m.setSubject("test");
	//
	//		m.setBody("test");
	//
	//		m.setPriority("HIGH");
	//
	//		final Message sent = this.messageService.send(m);
	//
	//		final Folder outbox = this.folderService.findOutboxByUserId(user.getId());
	//
	//		final Folder inbox = this.folderService.findInboxByUserId(recipient.getUserAccount().getId());
	//
	//		Assert.isTrue(outbox.getMessages().contains(sent) && inbox.getMessages().contains(sent));
	//
	//	}
	//
	//	@Test
	//	public void testSendMessageWithSpamWord() {
	//
	//		super.authenticate("member1");
	//
	//		final Actor sender = this.actorService.findByPrincipal();
	//
	//		final UserAccount user = sender.getUserAccount();
	//
	//		final Message m = this.messageService.create();
	//
	//		final Collection<Actor> recipients = new ArrayList<>();
	//
	//		final Actor recipient = sender;
	//
	//		recipients.add(recipient);
	//
	//		m.setRecipients(recipients);
	//
	//		m.setSubject("test");
	//
	//		m.setBody("viagra");
	//
	//		m.setPriority("HIGH");
	//
	//		final Message sent = this.messageService.send(m);
	//
	//		final Folder outbox = this.folderService.findOutboxByUserId(user.getId());
	//
	//		final Folder spambox = this.folderService.findSpamboxByUserId(recipient.getUserAccount().getId());
	//
	//		Assert.isTrue(outbox.getMessages().contains(sent) && spambox.getMessages().contains(sent));
	//
	//		Assert.isTrue(sender.getSpammer());
	//
	//	}
	//
	//	@Test
	//	public void testDeleteMessage() {
	//
	//		super.authenticate("member1");
	//
	//		final Actor principal = this.actorService.findByPrincipal();
	//
	//		final Folder outbox = this.folderService.findOutboxByUserId(principal.getUserAccount().getId());
	//
	//		final Message message = (Message) outbox.getMessages().toArray()[0];
	//
	//		this.messageService.deleteFromFolder(message, outbox);
	//
	//		Assert.isTrue(!outbox.getMessages().contains(message));
	//
	//	}
	//
	//	@Test
	//	public void testDeleteMessageFromTrash() {
	//
	//		super.authenticate("member1");
	//
	//		final Actor principal = this.actorService.findByPrincipal();
	//
	//		final Folder trash = this.folderService.findTrashboxByUserId(principal.getUserAccount().getId());
	//
	//		final Message message = (Message) trash.getMessages().toArray()[0];
	//
	//		this.messageService.deleteFromFolder(message, trash);
	//
	//		Assert.isTrue(!trash.getMessages().contains(message));
	//
	//		for (final Folder f : this.folderService.findAll())
	//
	//			Assert.isTrue(!f.getMessages().contains(message));
	//
	//	}
	//
	//	@Test
	//	public void testDeleteLastMessage() {
	//
	//		super.authenticate("member1");
	//
	//		final Actor principal = this.actorService.findByPrincipal();
	//
	//		final Folder trash = this.folderService.findTrashboxByUserId(principal.getUserAccount().getId());
	//
	//		final Message message = (Message) trash.getMessages().toArray()[0];
	//
	//		this.messageService.deleteFromFolder(message, trash);
	//
	//		final Collection<Message> updated = this.messageService.findAll();
	//
	//		Assert.isTrue(!updated.contains(message));
	//
	//		Assert.isTrue(!trash.getMessages().contains(message));
	//
	//	}

}
