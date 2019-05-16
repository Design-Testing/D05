
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

import repositories.CurriculaRepository;
import repositories.RookyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.CreditCard;
import domain.Curricula;
import domain.Finder;
import domain.Rooky;
import forms.ActorForm;

@Service
@Transactional
public class RookyService {

	@Autowired
	private RookyRepository		rookyRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private CurriculaRepository	curriculaRepository;

	@Autowired
	private Validator			validator;


	public Rooky create() {
		final Rooky rooky = new Rooky();
		this.actorService.setAuthorityUserAccount(Authority.ROOKY, rooky);

		return rooky;
	}

	public Rooky findOne(final int rookyId) {
		Assert.isTrue(rookyId != 0);
		final Rooky result = this.rookyRepository.findOne(rookyId);
		Assert.notNull(result);
		return result;
	}

	public Rooky save(final Rooky rooky) {
		Assert.notNull(rooky);
		Rooky result;

		if (rooky.getId() == 0) {
			final Finder finder = this.finderService.createForNewRooky();
			rooky.setFinder(finder);
			this.actorService.setAuthorityUserAccount(Authority.ROOKY, rooky);
			result = this.rookyRepository.save(rooky);
			//			this.folderService.setFoldersByDefault(result);

			final Curricula curricula = this.curriculaService.createForNewRooky();
			curricula.setRooky(result);
			final Curricula res = this.curriculaRepository.save(curricula);
			Assert.notNull(res);

		} else {
			this.actorService.checkForSpamWords(rooky);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == rooky.getId(), "You only can edit your info");
			result = (Rooky) this.actorService.save(rooky);
		}
		return result;
	}

	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Rooky rooky) {
		Assert.notNull(rooky);
		Assert.isTrue(this.findByPrincipal().equals(rooky));
		Assert.isTrue(rooky.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == rooky.getId(), "You only can edit your info");
		Assert.isTrue(this.rookyRepository.exists(rooky.getId()));
		this.rookyRepository.delete(rooky);
	}

	public void deletePersonalData() {
		final Rooky principal = this.findByPrincipal();
		this.finderService.clear(this.finderService.findRookyFinder());
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
		this.rookyRepository.save(principal);
	}

	/* ========================= OTHER METHODS =========================== */

	public Rooky findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Rooky rooky = this.findByUserId(user.getId());
		Assert.notNull(rooky);
		final boolean bool = this.actorService.checkAuthority(rooky, Authority.ROOKY);
		Assert.isTrue(bool);

		return rooky;
	}

	public Rooky findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Rooky rooky = this.rookyRepository.findByUserId(id);
		return rooky;
	}

	/**
	 * The average, minimum, maximum and standard deviation of the number of applications per rooky
	 * 
	 * @author a8081
	 */
	public Double[] getStatisticsOfApplicationsPerRooky() {
		final Double[] res = this.rookyRepository.getStatisticsOfApplicationsPerRooky();
		Assert.notNull(res);
		return res;
	}

	/**
	 * Rookys who have made more applications
	 * 
	 * @author a8081
	 */
	public Collection<Rooky> getRookysMoreApplications() {
		final Collection<Rooky> res = this.rookyRepository.getRookysMoreApplications();
		Assert.notNull(res);
		return res;
	}

	public void flush() {
		this.rookyRepository.flush();
	}

	public Rooky reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Rooky rooky;
		final CreditCard c = new CreditCard();
		c.setHolderName(actorForm.getHolderName());
		final String cardNumber = actorForm.getNumber().replace(" ", "");
		c.setNumber(cardNumber);
		c.setMake(actorForm.getMake());
		c.setExpirationMonth(actorForm.getExpirationMonth());
		c.setExpirationYear(actorForm.getExpirationYear());
		c.setCvv(actorForm.getCvv());

		if (actorForm.getId() == 0) {
			rooky = this.create();
			rooky.setName(actorForm.getName());
			rooky.setSurname(actorForm.getSurname());
			rooky.setPhoto(actorForm.getPhoto());
			rooky.setPhone(actorForm.getPhone());
			rooky.setEmail(actorForm.getEmail());
			rooky.setAddress(actorForm.getAddress());
			rooky.setVat(actorForm.getVat());
			rooky.setVersion(actorForm.getVersion());
			rooky.setFinder(this.finderService.create());
			//			rooky.setScore(0.0);
			//			rooky.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.ROOKY);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			rooky.setUserAccount(account);
		} else {
			rooky = this.rookyRepository.findOne(actorForm.getId());
			rooky.setName(actorForm.getName());
			rooky.setSurname(actorForm.getSurname());
			rooky.setPhoto(actorForm.getPhoto());
			rooky.setPhone(actorForm.getPhone());
			rooky.setEmail(actorForm.getEmail());
			rooky.setAddress(actorForm.getAddress());
			rooky.setVat(actorForm.getVat());
			rooky.setVersion(actorForm.getVersion());
			rooky.setFinder(this.finderService.findRookyFinder());
			final UserAccount account = this.userAccountService.findOne(rooky.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			rooky.setUserAccount(account);
		}

		rooky.setCreditCard(c);

		this.validator.validate(rooky, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return rooky;
	}
	public Rooky findRookyByCurricula(final int id) {
		final Rooky result = this.rookyRepository.findRookyByCurricula(id);
		return result;
	}

	public Rooky findRookyByPersonalData(final int id) {
		final Rooky result = this.rookyRepository.findRookyByPersonalData(id);
		return result;
	}

	public Rooky findRookyByMiscellaneous(final int id) {
		final Rooky result = this.rookyRepository.findRookyByMiscellaneous(id);
		return result;
	}

	public Rooky findRookyByEducationDatas(final int id) {
		final Rooky result = this.rookyRepository.findRookyByEducationDatas(id);
		return result;
	}

	public Rooky findRookyByPositionDatas(final int id) {
		final Rooky result = this.rookyRepository.findRookyByPositionDatas(id);
		return result;
	}

	public Boolean hasPersonalData(final int rookyId, final int dataId) {
		final Boolean result = this.rookyRepository.hasPersonalData(rookyId, dataId);
		Assert.notNull(result, "hasPersonalData returns null");
		return result;
	}

	public Boolean hasEducationData(final int rookyId, final int dataId) {
		final Boolean result = this.rookyRepository.hasEducationData(rookyId, dataId);
		Assert.notNull(result, "hasEducationData returns null");
		return result;
	}

	public Boolean hasPositionData(final int rookyId, final int dataId) {
		final Boolean result = this.rookyRepository.hasPositionData(rookyId, dataId);
		Assert.notNull(result, "hasPositionData returns null");
		return result;
	}

	public Boolean hasMiscellaneousData(final int rookyId, final int dataId) {
		final Boolean result = this.rookyRepository.hasMiscellaneousData(rookyId, dataId);
		Assert.notNull(result, "hasMiscellanousData returns null");
		return result;
	}

	public Boolean hasCurricula(final int rookyId, final int dataId) {
		final Boolean result = this.rookyRepository.hasCurricula(rookyId, dataId);
		Assert.notNull(result, "hasCurricula returns null");
		return result;
	}

	public Rooky findRookyByCopyCurricula(final int id) {
		final Rooky result = this.rookyRepository.findRookyByCopyCurricula(id);
		Assert.notNull(result, "rooky found by copy of curricula is null");
		return result;
	}

}
