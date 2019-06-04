
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import forms.ActorForm;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository	administratorRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private FolderService			folderService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private Validator				validator;


	public Administrator create() {
		final Administrator a = new Administrator();

		//		this.actorService.setAuthorityUserAccount(Authority.ADMIN, a);

		return a;
	}

	public Administrator save(final Administrator a) {
		// It must be an administrator who create another one, or who modify himself
		this.findByPrincipal();
		Assert.notNull(a);
		Administrator result;
		this.actorService.checkForSpamWords(a);
		if (a.getId() == 0) {
			//			this.actorService.setAuthorityUserAccount(Authority.ADMIN, a);
			final UserAccount ua = a.getUserAccount();
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(ua.getPassword(), null);
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setAuthorities(ua.getAuthorities());
			ua.setUsername(ua.getUsername());
			ua.setPassword(hash);
			final UserAccount uasaved = this.userAccountService.save(ua);
			a.setUserAccount(uasaved);
			result = this.administratorRepository.save(a);
			this.folderService.setFoldersByDefault(result);
		} else
			result = (Administrator) this.actorService.save(a);

		return result;
	}
	public Administrator findOne(final int id) {
		Assert.isTrue(id != 0);
		final Administrator result = this.administratorRepository.findOne(id);
		Assert.notNull(result);
		return result;
	}

	public Administrator findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Administrator a = this.findByUserId(user.getId());
		Assert.notNull(a);
		final boolean bool = this.actorService.checkAuthority(a, Authority.ADMIN);
		Assert.isTrue(bool);

		return a;
	}

	public Administrator findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Administrator a = this.administratorRepository.findByUserId(id);
		return a;
	}

	public Administrator findSystem() {
		return this.administratorRepository.findSystem();
	}

	public void deletePersonalData() {
		final Administrator principal = this.findByPrincipal();
		final List<String> s = new ArrayList<>();
		s.add("DELETED");
		principal.setAddress(null);
		principal.setEmail("DELETED@mail.de");
		principal.setSurname(s);
		//principal.setName("");
		principal.setPhone(null);
		principal.setPhoto(null);
		principal.setSpammer(false);
		final Authority ban = new Authority();
		ban.setAuthority(Authority.BANNED);
		principal.getUserAccount().getAuthorities().add(ban);
		this.administratorRepository.save(principal);
	}

	public Administrator reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Administrator admin;

		if (actorForm.getId() == 0) {
			admin = this.create();
			admin.setName(actorForm.getName());
			admin.setSurname(actorForm.getSurname());
			admin.setPhoto(actorForm.getPhoto());
			admin.setPhone(actorForm.getPhone());
			admin.setEmail(actorForm.getEmail());
			admin.setAddress(actorForm.getAddress());
			admin.setVersion(actorForm.getVersion());
			admin.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.ADMIN);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			admin.setUserAccount(account);
		} else {
			admin = this.administratorRepository.findOne(actorForm.getId());
			admin.setName(actorForm.getName());
			admin.setSurname(actorForm.getSurname());
			admin.setPhoto(actorForm.getPhoto());
			admin.setPhone(actorForm.getPhone());
			admin.setEmail(actorForm.getEmail());
			admin.setAddress(actorForm.getAddress());
			admin.setVersion(actorForm.getVersion());
			final UserAccount account = this.userAccountService.findOne(admin.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			admin.setUserAccount(account);
		}

		this.validator.validate(admin, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return admin;
	}

	public void flush() {
		this.administratorRepository.flush();
	}
}
