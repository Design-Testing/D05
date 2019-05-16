
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

import repositories.ProviderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.Provider;
import forms.ProviderForm;

@Service
@Transactional
public class ProviderService {

	@Autowired
	private ProviderRepository	providerRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	public Provider create() {
		final Provider provider = new Provider();
		this.actorService.setAuthorityUserAccount(Authority.PROVIDER, provider);

		return provider;
	}

	public Provider findOne(final int providerId) {
		Assert.isTrue(providerId != 0);
		final Provider result = this.providerRepository.findOne(providerId);
		Assert.notNull(result);
		return result;
	}

	public Provider save(final Provider provider) {
		Assert.notNull(provider);
		Provider result;

		if (provider.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.PROVIDER, provider);
			result = this.providerRepository.save(provider);
			//			this.folderService.setFoldersByDefault(result);
		} else {
			this.actorService.checkForSpamWords(provider);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == provider.getId(), "You only can edit your info");
			result = (Provider) this.actorService.save(provider);
		}
		return result;
	}

	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Provider provider) {
		Assert.notNull(provider);
		Assert.isTrue(this.findByPrincipal().equals(provider));
		Assert.isTrue(provider.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == provider.getId(), "You only can edit your info");
		Assert.isTrue(this.providerRepository.exists(provider.getId()));
		this.providerRepository.delete(provider);
	}

	public void deletePersonalData() {
		final Provider principal = this.findByPrincipal();
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
		this.providerRepository.save(principal);
	}

	/* ========================= OTHER METHODS =========================== */

	public Provider findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Provider provider = this.findByUserId(user.getId());
		Assert.notNull(provider);
		final boolean bool = this.actorService.checkAuthority(provider, Authority.PROVIDER);
		Assert.isTrue(bool);

		return provider;
	}

	public Provider findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Provider provider = this.providerRepository.findByUserId(id);
		return provider;
	}

	public void flush() {
		this.providerRepository.flush();
	}

	public Provider reconstruct(final ProviderForm providerForm, final BindingResult binding) {
		Provider provider;
		final CreditCard c = new CreditCard();
		c.setHolderName(providerForm.getHolderName());
		final String cardNumber = providerForm.getNumber().replace(" ", "");
		c.setNumber(cardNumber);
		c.setMake(providerForm.getMake());
		c.setExpirationMonth(providerForm.getExpirationMonth());
		c.setExpirationYear(providerForm.getExpirationYear());
		c.setCvv(providerForm.getCvv());

		if (providerForm.getId() == 0) {
			provider = this.create();
			provider.setName(providerForm.getName());
			provider.setSurname(providerForm.getSurname());
			provider.setPhoto(providerForm.getPhoto());
			provider.setPhone(providerForm.getPhone());
			provider.setEmail(providerForm.getEmail());
			provider.setAddress(providerForm.getAddress());
			provider.setVat(providerForm.getVat());
			provider.setVersion(providerForm.getVersion());
			provider.setMake(providerForm.getProviderMake());
			//			provider.setScore(0.0);
			//			provider.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.PROVIDER);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(providerForm.getUserAccountuser());
			account.setPassword(providerForm.getUserAccountpassword());
			provider.setUserAccount(account);
		} else {
			provider = this.providerRepository.findOne(providerForm.getId());
			provider.setMake(providerForm.getProviderMake());
			provider.setName(providerForm.getName());
			provider.setSurname(providerForm.getSurname());
			provider.setPhoto(providerForm.getPhoto());
			provider.setPhone(providerForm.getPhone());
			provider.setEmail(providerForm.getEmail());
			provider.setAddress(providerForm.getAddress());
			provider.setVat(providerForm.getVat());
			provider.setVersion(providerForm.getVersion());
			final UserAccount account = this.userAccountService.findOne(provider.getUserAccount().getId());
			account.setUsername(providerForm.getUserAccountuser());
			account.setPassword(providerForm.getUserAccountpassword());
			provider.setUserAccount(account);
		}

		provider.setCreditCard(c);

		this.validator.validate(provider, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return provider;
	}

	public Collection<Provider> findAll() {
		final Collection<Provider> providers = this.providerRepository.findAll();
		Assert.notNull(providers);
		return providers;

	}

	public Provider findByItem(final int itemId) {
		final Provider result;
		result = this.providerRepository.findByItem(itemId);
		Assert.notNull(result, "findByItem returns null");
		return result;
	}

	public Collection<Provider> findTenPerCentMoreAppsThanAverage() {
		return this.providerRepository.findTenPerCentMoreAppsThanAverage();
	}

}
