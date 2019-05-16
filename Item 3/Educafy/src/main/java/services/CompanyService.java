
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

import repositories.CompanyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Company;
import domain.CreditCard;
import forms.CompanyForm;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository		companyRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private Validator				validator;


	//METODOS CRUD

	public Company create() {
		final Company company = new Company();
		return company;
	}

	public Collection<Company> findAll() {
		final Collection<Company> companies = this.companyRepository.findAll();
		Assert.notNull(companies);
		return companies;
	}

	public Company findOne(final int companyId) {
		Assert.isTrue(companyId != 0);
		final Company company = this.companyRepository.findOne(companyId);
		Assert.notNull(company);
		return company;
	}

	public Company save(final Company company) {
		Assert.notNull(company);
		Company result;

		if (company.getId() == 0)
			this.actorService.setAuthorityUserAccount(Authority.COMPANY, company);
		else {
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == company.getId(), "You only can edit your info");
		}
		result = this.companyRepository.save(company);
		return result;
	}

	public void delete(final Company company) {
		Assert.notNull(company);
		Assert.isTrue(this.findByPrincipal().equals(company));
		Assert.isTrue(company.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == company.getId(), "You only can edit your info");
		Assert.isTrue(this.companyRepository.exists(company.getId()));
		this.companyRepository.delete(company);
	}

	public void deletePersonalData() {
		final Company principal = this.findByPrincipal();
		final List<String> s = new ArrayList<>();
		s.add("DELETED");
		principal.setCommercialName("DELETED");
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
		this.companyRepository.save(principal);
	}
	/* ========================= OTHER METHODS =========================== */

	public Company reconstruct(final CompanyForm companyForm, final BindingResult binding) {
		Company company;
		final CreditCard c = new CreditCard();
		c.setHolderName(companyForm.getHolderName());
		final String cardNumber = companyForm.getNumber().replace(" ", "");
		c.setNumber(cardNumber);
		c.setMake(companyForm.getMake());
		c.setExpirationMonth(companyForm.getExpirationMonth());
		c.setExpirationYear(companyForm.getExpirationYear());
		c.setCvv(companyForm.getCvv());

		if (companyForm.getId() == 0) {
			company = new Company();
			company.setName(companyForm.getName());
			company.setSurname(companyForm.getSurname());
			company.setPhoto(companyForm.getPhoto());
			company.setPhone(companyForm.getPhone());
			company.setEmail(companyForm.getEmail());
			company.setAddress(companyForm.getAddress());
			company.setVat(companyForm.getVat());
			company.setVersion(companyForm.getVersion());
			company.setCommercialName(companyForm.getCommercialName());
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.COMPANY);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(companyForm.getUserAccountuser());
			account.setPassword(companyForm.getUserAccountpassword());
			company.setUserAccount(account);
		} else {
			company = this.companyRepository.findOne(companyForm.getId());
			company.setName(companyForm.getName());
			company.setSurname(companyForm.getSurname());
			company.setPhoto(companyForm.getPhoto());
			company.setPhone(companyForm.getPhone());
			company.setEmail(companyForm.getEmail());
			company.setAddress(companyForm.getAddress());
			company.setVat(companyForm.getVat());
			company.setVersion(companyForm.getVersion());
			company.setCommercialName(companyForm.getCommercialName());
			final UserAccount account = this.userAccountService.findOne(company.getUserAccount().getId());
			account.setUsername(companyForm.getUserAccountuser());
			account.setPassword(companyForm.getUserAccountpassword());
			company.setUserAccount(account);
		}

		company.setCreditCard(c);

		this.validator.validate(company, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return company;

	}

	public Company findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Company company = this.findByUserId(user.getId());
		Assert.notNull(company);
		final boolean bool = this.actorService.checkAuthority(company, Authority.COMPANY);
		Assert.isTrue(bool);

		return company;
	}

	public Company findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Company company = this.companyRepository.findByUserId(id);
		return company;
	}

	public Company findCompanyByProblem(final int problemId) {
		Company res;
		res = this.companyRepository.findCompanyByProblem(problemId);
		Assert.notNull(res);
		return res;
	}

	public Company findCompanyByPosition(final int positionId) {
		Company res;
		res = this.companyRepository.findCompanyByPosition(positionId);
		Assert.notNull(res);
		return res;
	}

	public Double[] getStatisticsOfScoreOfCompanies() {
		return this.companyRepository.getStatisticsOfScoreOfCompanies();
	}

	public Double[] getStatisticsOfAuditScoreOfCompany(final int companyId) {
		return this.companyRepository.getStatisticsOfAuditScoreOfCompany(companyId);
	}

	public Collection<Company> getCompaniesHighestAuditScore() {
		final Collection<Company> res = this.companyRepository.getCompaniesHighestAuditScore();
		Assert.notNull(res);
		return res;
	}

	/**
	 * The average, minimum, maximum and standard deviation of the number of positions per company
	 * 
	 * @author a8081
	 */
	public Double[] getStatisticsOfPositionsPerCompany() {
		final Double[] res = this.companyRepository.getStatisticsOfPositionsPerCompany();
		Assert.notNull(res);
		return res;
	}

	/**
	 * Companies that have offered more positions
	 * 
	 * @author a8081
	 */
	public Collection<Company> getCompaniesMorePositions() {
		final Collection<Company> res = this.companyRepository.getCompaniesMorePositions();
		Assert.notNull(res);
		return res;
	}

	public Double computeScore(final Actor a) {
		final boolean isCompany = this.actorService.checkAuthority(a, Authority.COMPANY);
		Assert.isTrue(isCompany);

		Assert.notNull(a);

		this.administratorService.findByPrincipal();

		final Integer min = this.companyRepository.getMinScore(a.getId());
		final Integer max = this.companyRepository.getMaxScore(a.getId());

		final int range = max - min;
		final Double res = this.companyRepository.getAvgScore(a.getId());

		final Double normRes;

		if (range != 0)
			normRes = (res - min) / (max - min);
		else
			normRes = 0.;

		final Company company = this.findOne(a.getId());
		company.setScore(normRes);

		this.companyRepository.save(company);

		return normRes;
	}

	public void computeScores() {

		final Collection<Company> companies = this.findAll();
		if (!companies.isEmpty())
			for (final Company c : companies)
				this.computeScore(c);

	}

	public void flush() {
		this.companyRepository.flush();
	}

}
