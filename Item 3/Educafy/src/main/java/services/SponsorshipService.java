
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.CreditCard;
import domain.Provider;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private PositionService			positionService;

	private Validator				validator;


	//Constructor
	public SponsorshipService() {
		super();
	}

	//Simple CRUD method
	public Sponsorship create() {
		return new Sponsorship();
	}

	public Collection<Sponsorship> findAll() {
		final Collection<Sponsorship> res = this.sponsorshipRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Assert.isTrue(sponsorshipId != 0);
		final Sponsorship res = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(res);
		return res;
	}

	public void delete(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));
		final Provider principal = this.providerService.findByPrincipal();
		Assert.isTrue(principal.getId() == sponsorship.getProvider().getId(), "you can not delte a sponsorship that is not yours");
		this.sponsorshipRepository.delete(sponsorship);
	}

	public Double[] getStatisticsOfSponsorshipPerProvider() {
		return this.sponsorshipRepository.getStatisticsOfSponsorshipPerProvider();
	}

	public Double[] getStatisticsOfSponsorshipPerPosition() {
		return this.sponsorshipRepository.getStatisticsOfSponsorshipPerPosition();
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}

	public Collection<Sponsorship> findAllByProvider() {
		final Provider principal = this.providerService.findByPrincipal();
		final Collection<Sponsorship> res = this.findAllByUserId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Sponsorship save(final Sponsorship s) {
		Assert.notNull(s);

		final Provider principal = this.providerService.findByPrincipal();
		final Collection<Sponsorship> ss = this.findAllByUserId(principal.getUserAccount().getId());
		Assert.isTrue(s.getProvider().equals(principal));

		if (s.getId() == 0) {
			s.setProvider(principal);
			Assert.isTrue(this.sponsorshipRepository.availableSponsorshipPosition(s.getPosition().getId(), principal.getUserAccount().getId()), "Cannot sponsors twice the same parade");
			Assert.notNull(s.getCreditCard(), "You must to set a credit card to create a sponsorship");
			Assert.isTrue(this.positionService.exists(s.getPosition().getId()), "You must sponsors to a parade of the system");
			Assert.isTrue(!this.expiredCreditCard(s.getCreditCard()));
		} else {
			Assert.isTrue(ss.contains(s), "You only can modify your sponsorships, you haven't access to this resource");
			Assert.isTrue(!this.expiredCreditCard(s.getCreditCard()));
		}
		final Sponsorship saved = this.sponsorshipRepository.save(s);
		return saved;
	}

	private Collection<Sponsorship> findAllByUserId(final int id) {
		Assert.isTrue(id != 0);
		return this.sponsorshipRepository.findAllByUserId(id);
	}

	public Sponsorship findRandomSponsorship(final int positionId) {
		Sponsorship res;
		final Collection<Sponsorship> sponsorships = this.sponsorshipRepository.findByPosition(positionId);
		if (sponsorships.size() > 0) {
			final int randomNumber = (int) (Math.random() * sponsorships.size());
			res = (Sponsorship) sponsorships.toArray()[randomNumber];
			this.messageService.sponsorshipDisplayedMessage(res);
		} else
			res = null;

		return res;
	}
	public Collection<Sponsorship> findByPosition(final int positionId) {
		final Collection<Sponsorship> res;
		res = this.sponsorshipRepository.findByPosition(positionId);
		Assert.notNull(res);
		return res;
	}

	public Sponsorship findByPosition(final int positionId, final int sponsorUAId) {
		final Sponsorship res;
		res = this.sponsorshipRepository.findByPosition(positionId, sponsorUAId);
		Assert.notNull(res);
		return res;
	}

	public SponsorshipForm inyect(final Sponsorship sponsorship) {
		final SponsorshipForm result = new SponsorshipForm();

		result.setId(sponsorship.getId());
		result.setVersion(sponsorship.getVersion());
		result.setBanner(sponsorship.getBanner());
		result.setTargetPage(sponsorship.getTargetPage());
		result.setPosition(sponsorship.getPosition());
		result.setNumber(sponsorship.getCreditCard().getNumber());
		result.setHolderName(sponsorship.getCreditCard().getHolderName());
		result.setMake(sponsorship.getCreditCard().getMake());
		result.setExpirationMonth(sponsorship.getCreditCard().getExpirationMonth());
		result.setExpirationYear(sponsorship.getCreditCard().getExpirationYear());
		result.setCvv(sponsorship.getCreditCard().getCvv());

		return result;
	}

	public Sponsorship reconstruct(final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		Sponsorship sponsorship;
		final CreditCard creditCard = new CreditCard();

		if (sponsorshipForm.getId() == 0) {
			sponsorship = this.create();
			sponsorship.setPosition(sponsorshipForm.getPosition());
		} else
			sponsorship = this.sponsorshipRepository.findOne(sponsorshipForm.getId());

		final String number = sponsorshipForm.getNumber().replace(" ", "");
		creditCard.setMake(sponsorshipForm.getMake());
		creditCard.setNumber(number);
		creditCard.setHolderName(sponsorshipForm.getHolderName());
		creditCard.setExpirationMonth(sponsorshipForm.getExpirationMonth());
		creditCard.setExpirationYear(sponsorshipForm.getExpirationYear());
		creditCard.setCvv(sponsorshipForm.getCvv());
		sponsorship.setBanner(sponsorshipForm.getBanner());
		sponsorship.setTargetPage(sponsorshipForm.getTargetPage());
		sponsorship.setProvider(this.providerService.findByPrincipal());
		sponsorship.setCreditCard(creditCard);

		//this.validator.validate(sponsorship, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return sponsorship;
	}

	public Sponsorship findOneSponsorship(final int sponsorshipId) {
		final Provider provider = this.providerService.findByPrincipal();
		final Sponsorship sponsorship = this.findOne(sponsorshipId);
		Assert.isTrue(sponsorship.getProvider().getId() == provider.getId());
		return sponsorship;
	}

	// ancilliary
	boolean expiredCreditCard(final CreditCard c) {
		boolean res = false;
		final Date now = new Date();
		final boolean mesCaducado = c.getExpirationMonth() < (now.getMonth() + 1);
		final boolean mismoAnyo = (c.getExpirationYear()) == (now.getYear() % 100);
		final boolean anyoCaducado = (c.getExpirationYear()) < (now.getYear() % 100);
		if (anyoCaducado || (mismoAnyo && mesCaducado))
			res = true;

		return res;
	}

}
