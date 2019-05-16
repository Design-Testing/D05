
package services.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import security.UserAccount;
import security.UserAccountRepository;
import services.AdministratorService;
import services.AuditorService;
import services.CompanyService;
import services.ProviderService;
import services.RookyService;
import services.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Auditor;
import domain.Company;
import domain.CreditCard;
import domain.Provider;
import domain.Rooky;
import forms.ActorForm;
import forms.AuditorForm;
import forms.CompanyForm;
import forms.ProviderForm;

@Service
@Transactional
public class RegisterService {

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private RookyService			rookyService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private AuditorService			auditorService;

	@Autowired
	private ProviderService			providerService;


	public Administrator saveAdmin(final Administrator admin, final BindingResult binding) {
		Administrator result;
		final UserAccount ua = admin.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (admin.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			admin.setUserAccount(ua);
			result = this.administratorService.save(admin);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Administrator old = this.administratorService.findOne(admin.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.administratorService.save(admin);

		}

		return result;
	}

	public Provider saveProvider(final Provider provider, final BindingResult binding) {
		Provider result;
		final UserAccount ua = provider.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (provider.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			provider.setUserAccount(ua);
			result = this.providerService.save(provider);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Provider old = this.providerService.findOne(provider.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.providerService.save(provider);

		}

		return result;
	}

	public Rooky saveRooky(final Rooky rooky, final BindingResult binding) {
		Rooky result;
		final UserAccount ua = rooky.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (rooky.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			rooky.setUserAccount(ua);
			result = this.rookyService.save(rooky);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Rooky old = this.rookyService.findOne(rooky.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.rookyService.save(rooky);

		}

		return result;
	}

	public Company saveCompany(final Company company, final BindingResult binding) {
		Company result;
		final UserAccount ua = company.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (company.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			company.setUserAccount(ua);
			result = this.companyService.save(company);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Company old = this.companyService.findOne(company.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.companyService.save(company);

		}

		return result;
	}

	public ActorForm inyect(final Actor actor) {
		final ActorForm result = new ActorForm();

		final CreditCard c = actor.getCreditCard();

		result.setAddress(actor.getAddress());
		result.setEmail(actor.getEmail());
		result.setId(actor.getId());
		result.setName(actor.getName());
		result.setPhone(actor.getPhone());
		result.setPhoto(actor.getPhoto());
		result.setSurname(actor.getSurname());
		result.setVat(actor.getVat());
		result.setVersion(actor.getVersion());
		// CreditCard
		result.setHolderName(c.getHolderName());
		result.setExpirationMonth(c.getExpirationMonth());
		result.setExpirationYear(c.getExpirationYear());
		result.setMake(c.getMake());
		result.setNumber(c.getNumber());
		result.setCvv(c.getCvv());

		result.setUserAccountpassword(actor.getUserAccount().getPassword());
		result.setUserAccountuser(actor.getUserAccount().getUsername());
		result.setVersion(actor.getVersion());

		return result;
	}

	public CompanyForm inyect(final Company company) {
		final CompanyForm result = new CompanyForm();

		final CreditCard c = company.getCreditCard();

		result.setAddress(company.getAddress());
		result.setEmail(company.getEmail());
		result.setId(company.getId());
		result.setName(company.getName());
		result.setPhone(company.getPhone());
		result.setPhoto(company.getPhoto());
		result.setSurname(company.getSurname());
		result.setVat(company.getVat());
		result.setVersion(company.getVersion());
		result.setCommercialName(company.getCommercialName());
		// CreditCard
		result.setHolderName(c.getHolderName());
		result.setExpirationMonth(c.getExpirationMonth());
		result.setExpirationYear(c.getExpirationYear());
		result.setMake(c.getMake());
		result.setNumber(c.getNumber());
		result.setCvv(c.getCvv());

		result.setUserAccountpassword(company.getUserAccount().getPassword());
		result.setUserAccountuser(company.getUserAccount().getUsername());
		result.setVersion(company.getVersion());

		return result;
	}

	public ProviderForm inyect(final Provider provider) {
		final ProviderForm result = new ProviderForm();

		final CreditCard c = provider.getCreditCard();

		result.setAddress(provider.getAddress());
		result.setEmail(provider.getEmail());
		result.setId(provider.getId());
		result.setName(provider.getName());
		result.setPhone(provider.getPhone());
		result.setPhoto(provider.getPhoto());
		result.setSurname(provider.getSurname());
		result.setVat(provider.getVat());
		result.setVersion(provider.getVersion());
		result.setProviderMake(provider.getMake());
		// CreditCard
		result.setHolderName(c.getHolderName());
		result.setExpirationMonth(c.getExpirationMonth());
		result.setExpirationYear(c.getExpirationYear());
		result.setMake(c.getMake());
		result.setNumber(c.getNumber());
		result.setCvv(c.getCvv());

		result.setUserAccountpassword(provider.getUserAccount().getPassword());
		result.setUserAccountuser(provider.getUserAccount().getUsername());
		result.setVersion(provider.getVersion());

		return result;
	}

	public AuditorForm inyect(final Auditor auditor) {
		final AuditorForm result = new AuditorForm();

		final CreditCard c = auditor.getCreditCard();

		result.setAddress(auditor.getAddress());
		result.setEmail(auditor.getEmail());
		result.setId(auditor.getId());
		result.setName(auditor.getName());
		result.setPhone(auditor.getPhone());
		result.setPhoto(auditor.getPhoto());
		result.setSurname(auditor.getSurname());
		result.setVat(auditor.getVat());
		result.setVersion(auditor.getVersion());

		// CreditCard
		result.setHolderName(c.getHolderName());
		result.setExpirationMonth(c.getExpirationMonth());
		result.setExpirationYear(c.getExpirationYear());
		result.setMake(c.getMake());
		result.setNumber(c.getNumber());
		result.setCvv(c.getCvv());

		result.setUserAccountpassword(auditor.getUserAccount().getPassword());
		result.setUserAccountuser(auditor.getUserAccount().getUsername());
		result.setVersion(auditor.getVersion());

		return result;
	}

	public Auditor saveAuditor(final Auditor auditor, final BindingResult binding) {
		Auditor result;
		final UserAccount ua = auditor.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (auditor.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			auditor.setUserAccount(ua);
			result = this.auditorService.save(auditor);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Auditor old = this.auditorService.findOne(auditor.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.auditorService.save(auditor);

		}

		return result;
	}

}
