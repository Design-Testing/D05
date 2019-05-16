
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Auditor;
import domain.CreditCard;
import forms.ActorForm;

@Service
@Transactional
public class AuditorService {

	@Autowired
	private AuditorRepository	auditorRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	public Auditor create() {

		final UserAccount logged = LoginService.getPrincipal();

		final Authority authAdmin = new Authority();
		authAdmin.setAuthority(Authority.ADMIN);
		Assert.isTrue(logged.getAuthorities().contains(authAdmin), "to create a auditor you must be a admin");

		final Auditor auditor = new Auditor();
		this.actorService.setAuthorityUserAccount(Authority.AUDITOR, auditor);

		return auditor;
	}

	public Auditor findOne(final int auditorId) {
		Assert.isTrue(auditorId != 0);
		final Auditor result = this.auditorRepository.findOne(auditorId);
		Assert.notNull(result);
		return result;
	}

	public Auditor save(final Auditor auditor) {
		Assert.notNull(auditor);
		Auditor result;
		final UserAccount logged = LoginService.getPrincipal();

		final Authority authAdmin = new Authority();
		authAdmin.setAuthority(Authority.ADMIN);

		if (auditor.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.AUDITOR, auditor);
			result = this.auditorRepository.save(auditor);
			//			this.folderService.setFoldersByDefault(result);

			Assert.isTrue(logged.getAuthorities().contains(authAdmin), "to create a auditor you must be a admin");

		} else {
			this.actorService.checkForSpamWords(auditor);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == auditor.getId(), "You only can edit your info");
			result = (Auditor) this.actorService.save(auditor);
		}
		return result;
	}
	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Auditor auditor) {
		Assert.notNull(auditor);
		Assert.isTrue(auditor.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == auditor.getId(), "You only can edit your info");
		Assert.isTrue(this.auditorRepository.exists(auditor.getId()));
		this.auditorRepository.delete(auditor);
	}

	public Collection<Auditor> findAll() {
		Collection<Auditor> res = new ArrayList<>();
		res = this.auditorRepository.findAll();
		Assert.notNull(res, "Find all returns a null collection");
		return res;
	}

	public void deletePersonalData() {
		final Auditor principal = this.findByPrincipal();
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
		this.auditorRepository.save(principal);
	}

	/* ========================= OTHER METHODS =========================== */

	public Auditor findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);
		System.out.println(user.getId());
		final Auditor auditor = this.findByUserId(user.getId());
		Assert.notNull(auditor, "auditor find by principal returns null");
		final boolean bool = this.actorService.checkAuthority(auditor, Authority.AUDITOR);
		Assert.isTrue(bool);

		return auditor;
	}

	public Auditor findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Auditor auditor = this.auditorRepository.findByUserId(id);
		return auditor;
	}

	public void flush() {
		this.auditorRepository.flush();
	}

	public Auditor reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Auditor auditor;
		final CreditCard c = new CreditCard();
		c.setHolderName(actorForm.getHolderName());
		final String cardNumber = actorForm.getNumber().replace(" ", "");
		c.setNumber(cardNumber);
		c.setMake(actorForm.getMake());
		c.setExpirationMonth(actorForm.getExpirationMonth());
		c.setExpirationYear(actorForm.getExpirationYear());
		c.setCvv(actorForm.getCvv());

		if (actorForm.getId() == 0) {
			auditor = this.create();
			auditor.setName(actorForm.getName());
			auditor.setSurname(actorForm.getSurname());
			auditor.setPhoto(actorForm.getPhoto());
			auditor.setPhone(actorForm.getPhone());
			auditor.setEmail(actorForm.getEmail());
			auditor.setAddress(actorForm.getAddress());
			auditor.setVat(actorForm.getVat());
			auditor.setVersion(actorForm.getVersion());
			//			auditor.setScore(0.0);
			//			auditor.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.AUDITOR);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			auditor.setUserAccount(account);
		} else {
			auditor = this.auditorRepository.findOne(actorForm.getId());
			auditor.setName(actorForm.getName());
			auditor.setSurname(actorForm.getSurname());
			auditor.setPhoto(actorForm.getPhoto());
			auditor.setPhone(actorForm.getPhone());
			auditor.setEmail(actorForm.getEmail());
			auditor.setAddress(actorForm.getAddress());
			auditor.setVat(actorForm.getVat());
			auditor.setVersion(actorForm.getVersion());
			final UserAccount account = this.userAccountService.findOne(auditor.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			auditor.setUserAccount(account);
		}

		auditor.setCreditCard(c);

		this.validator.validate(auditor, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return auditor;
	}

}
