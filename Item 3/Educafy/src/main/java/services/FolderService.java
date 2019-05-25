
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FolderRepository;
import domain.Actor;
import domain.Folder;
import domain.Message;
import forms.FolderForm;

@Service
@Transactional
public class FolderService {

	@Autowired
	private FolderRepository				folderRepository;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private MessageService					messageService;

	@Autowired
	private Validator						validator;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	public Folder create() {
		final Folder folder = new Folder();
		final Collection<Message> ms = new ArrayList<>();
		folder.setIsSystemFolder(false);
		folder.setMessages(ms);
		return folder;
	}

	public Collection<Folder> findAll() {
		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Folder> res = this.findAllByUserId(principal.getUserAccount().getId());
		Assert.notNull(res);

		return res;
	}

	public Folder findOne(final int id) {
		final Folder res = this.folderRepository.findOne(id);

		return res;
	}

	public Folder save(final Folder f, final Actor a) {
		Assert.notNull(f);
		Assert.notNull(a);

		Folder saved;
		final boolean bool = this.checkForSpamWords(f);
		if (bool)
			a.setSpammer(true);
		if (f.getId() == 0) {
			f.setActor(a);
			saved = this.folderRepository.save(f);
		} else {
			final Collection<Folder> fs = this.findAllByUserId(a.getUserAccount().getId());
			Assert.isTrue(fs.contains(f));
			saved = this.folderRepository.save(f);
		}
		return saved;
	}
	private boolean checkForSpamWords(final Folder f) {
		final Collection<String> words = new ArrayList<>();
		words.add(f.getName());

		return this.configurationParametersService.checkForSpamWords(words);
	}

	public void delete(final Folder f) {
		Assert.notNull(f);
		Assert.isTrue(!f.getIsSystemFolder());
		Assert.isTrue(f.getId() != 0);

		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Folder> fs = this.findAllByUserId(principal.getUserAccount().getId());
		final Collection<Message> ms = this.messageService.findAllByFolderIdAndUserId(f.getId(), principal.getUserAccount().getId());
		Assert.isTrue(fs.contains(f));
		if (!ms.isEmpty())
			this.messageService.deleteAll(ms, f);
		this.folderRepository.delete(f);
	}

	public Collection<Folder> setFoldersByDefault(final Actor actor) {
		final Collection<Folder> folders = new ArrayList<Folder>();
		final Collection<Message> messages = new ArrayList<Message>();

		final Folder inbox = this.create();
		inbox.setName("In box");
		inbox.setIsSystemFolder(true);
		inbox.setActor(actor);
		inbox.setMessages(messages);
		this.save(inbox, actor);
		folders.add(inbox);

		final Folder outbox = this.create();
		outbox.setName("Out box");
		outbox.setIsSystemFolder(true);
		outbox.setActor(actor);
		outbox.setMessages(messages);
		this.save(outbox, actor);
		folders.add(outbox);

		final Folder trash = this.create();
		trash.setName("Trash box");
		trash.setIsSystemFolder(true);
		trash.setActor(actor);
		trash.setMessages(messages);
		this.save(trash, actor);
		folders.add(trash);

		final Folder spam = this.create();
		spam.setName("Spam box");
		spam.setIsSystemFolder(true);
		spam.setActor(actor);
		spam.setMessages(messages);
		this.save(spam, actor);
		folders.add(spam);

		final Folder notification = this.create();
		notification.setName("Notification box");
		notification.setIsSystemFolder(true);
		notification.setActor(actor);
		notification.setMessages(messages);
		this.save(notification, actor);
		folders.add(notification);

		return folders;
	}

	public Collection<Folder> saveAll(final Collection<Folder> fs) {
		Assert.notEmpty(fs);
		return this.folderRepository.save(fs);
	}

	public Collection<Folder> findAllByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findAllByUserId(id);
	}

	public Folder findInboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findInboxByUserId(id);
	}

	public Folder findOutboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findOutboxByUserId(id);
	}

	public Folder findSpamboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findSpamboxByUserId(id);
	}

	public Folder findTrashboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findTrashboxByUserId(id);
	}

	public Folder findNotificationboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findNotificationboxByUserId(id);
	}

	public Collection<Folder> findAllByMessageIdAndUserId(final int mid, final int uid) {
		Assert.isTrue(mid != 0);
		Assert.isTrue(uid != 0);

		return this.folderRepository.findAllByMessageIdAndUserId(mid, uid);
	}

	public Collection<Folder> findAllSystemFolderByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findAllSystemFolderByUserId(id);
	}

	public Folder reconstruct(final FolderForm folderForm, final BindingResult binding) {
		Folder result;

		if (folderForm.getId() == 0) {
			result = this.create();
			result.setActor(this.actorService.findByPrincipal());
		} else
			result = this.findOne(folderForm.getId());

		result.setVersion(folderForm.getVersion());
		result.setName(folderForm.getName());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

}
