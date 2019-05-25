
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CertifierRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Certifier;
import forms.ActorForm;

@Service
@Transactional
public class CertifierService {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CertifierRepository	certifierRepository;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private Validator			validator;


	public Certifier findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Certifier certifier = this.findByUserId(user.getId());
		Assert.notNull(certifier);
		final boolean bool = this.actorService.checkAuthority(certifier, Authority.CERTIFIER);
		Assert.isTrue(bool);

		return certifier;
	}

	public Certifier findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Certifier certifier = this.certifierRepository.findByUserId(id);
		return certifier;
	}

	public Certifier create() {
		final Certifier certifier = new Certifier();
		this.actorService.setAuthorityUserAccount(Authority.CERTIFIER, certifier);

		return certifier;
	}

	public Certifier findOne(final int certifierId) {
		Assert.isTrue(certifierId != 0);
		final Certifier result = this.certifierRepository.findOne(certifierId);
		Assert.notNull(result);
		return result;
	}

	public Certifier save(final Certifier certifier) {
		Assert.notNull(certifier);
		Certifier result;

		if (certifier.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.CERTIFIER, certifier);
			result = this.certifierRepository.save(certifier);
			this.folderService.setFoldersByDefault(result);
		} else {
			this.actorService.checkForSpamWords(certifier);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == certifier.getId(), "You only can edit your info");
			result = (Certifier) this.actorService.save(certifier);
		}
		return result;
	}

	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Certifier certifier) {
		Assert.notNull(certifier);
		Assert.isTrue(this.findByPrincipal().equals(certifier));
		Assert.isTrue(certifier.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == certifier.getId(), "You only can edit your info");
		Assert.isTrue(this.certifierRepository.exists(certifier.getId()));
		this.certifierRepository.delete(certifier);
	}

	public void deletePersonalData() {
		final Certifier principal = this.findByPrincipal();
		final List<String> s = new ArrayList<>();
		s.add("DELETED");
		principal.setAddress(null);
		principal.setEmail("DELETED@mail.de");
		principal.setSurname(s);
		//principal.setName("");
		principal.setPhone(null);
		principal.setPhoto(null);
		principal.setSpammer(false);
		principal.setVat(0.0);
		final Authority ban = new Authority();
		ban.setAuthority(Authority.BANNED);
		principal.getUserAccount().getAuthorities().add(ban);
		this.certifierRepository.save(principal);
	}

	/* ========================= OTHER METHODS =========================== */

	public void flush() {
		this.certifierRepository.flush();
	}

	public Certifier reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Certifier certifier;

		if (actorForm.getId() == 0) {
			certifier = this.create();
			certifier.setName(actorForm.getName());
			certifier.setSurname(actorForm.getSurname());
			certifier.setPhoto(actorForm.getPhoto());
			certifier.setPhone(actorForm.getPhone());
			certifier.setEmail(actorForm.getEmail());
			certifier.setAddress(actorForm.getAddress());
			certifier.setVat(actorForm.getVat());
			certifier.setVersion(actorForm.getVersion());
			//			certifier.setScore(0.0);
			//			certifier.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.CERTIFIER);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			certifier.setUserAccount(account);
		} else {
			certifier = this.certifierRepository.findOne(actorForm.getId());
			certifier.setName(actorForm.getName());
			certifier.setSurname(actorForm.getSurname());
			certifier.setPhoto(actorForm.getPhoto());
			certifier.setPhone(actorForm.getPhone());
			certifier.setEmail(actorForm.getEmail());
			certifier.setAddress(actorForm.getAddress());
			certifier.setVat(actorForm.getVat());
			certifier.setVersion(actorForm.getVersion());
			final UserAccount account = this.userAccountService.findOne(certifier.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			certifier.setUserAccount(account);
		}

		this.validator.validate(certifier, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return certifier;
	}

}
