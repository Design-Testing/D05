
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private ProviderService		providerService;


	@Test
	public void driverSponsorship() {

		final CreditCard creditCard = new CreditCard();
		creditCard.setCvv("640");
		creditCard.setNumber("4916727352312824");
		creditCard.setMake("VISA");
		creditCard.setHolderName("provider1");
		creditCard.setExpirationMonth(11);
		creditCard.setExpirationYear(20);

		final Object testingData[][] = {
			{		// Enrole correcto
				"provider1", "position8", "http://test.com", "http://test.com", creditCard, null
			}, {	// Enrole con dropOut seteado (no activo) 
				"provider1", "position9", "http://test.com", "http://test.com", null, java.lang.IllegalArgumentException.class
			}, {	// No existe enrole
				"provider2", "position10", "http://test.com", "", creditCard, javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateSponsorship((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (CreditCard) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateSponsorship(final String provider, final String pos, final String banner, final String targetPage, final CreditCard creditCard, final Class<?> expected) {
		Class<?> caught;
		int positionId;
		caught = null;
		try {

			this.authenticate(provider);
			positionId = super.getEntityId(pos);
			final Position position = this.positionService.findOne(positionId);
			final Provider prov = this.providerService.findOne(this.getEntityId(provider));

			final Sponsorship sponsorship = this.sponsorshipService.create();
			sponsorship.setProvider(prov);
			sponsorship.setPosition(position);
			sponsorship.setBanner(banner);
			sponsorship.setTargetPage(targetPage);
			sponsorship.setCreditCard(creditCard);
			final Sponsorship res = this.sponsorshipService.save(sponsorship);
			Assert.notNull(res);
			this.sponsorshipService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverEditSponsorship() {

		final CreditCard creditCard = new CreditCard();
		creditCard.setCvv("640");
		creditCard.setNumber("4916727352312824");
		creditCard.setMake("VISA");
		creditCard.setHolderName("provider1");
		creditCard.setExpirationMonth(11);
		creditCard.setExpirationYear(20);

		final Object testingData[][] = {
			{		// Positivo
				"provider1", "sponsorship1", "http://test.com", "http://test.com", creditCard, null
			}, {	// Negativo: banner no cum`ple url pattern
				"provider1", "sponsorship1", "test no url", "http://test.com", creditCard, javax.validation.ConstraintViolationException.class
			}, {	// Negativo: No es su sponsorship
				"provider1", "sponsorship2", "http://test.com", "http://test.com", creditCard, java.lang.IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateEditSponsorship((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (CreditCard) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void templateEditSponsorship(final String provider, final String sship, final String banner, final String targetPage, final CreditCard creditCard, final Class<?> expected) {
		Class<?> caught;
		final int positionId;
		caught = null;
		try {

			this.authenticate(provider);
			final Provider prov = this.providerService.findOne(this.getEntityId(provider));

			final Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId(sship));
			sponsorship.setBanner(banner);
			sponsorship.setTargetPage(targetPage);
			sponsorship.setCreditCard(creditCard);
			final Sponsorship res = this.sponsorshipService.save(sponsorship);
			Assert.notNull(res);
			this.sponsorshipService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteSponsorship() {
		final Object testingData[][] = {
			{		// Positivo
				"provider2", "sponsorship2", null
			}, {	// Negativo: No es su sponsorship
				"provider1", "sponsorship2", java.lang.IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDeleteSponsorship((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void templateDeleteSponsorship(final String provider, final String aud, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {

			this.authenticate(provider);
			final Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId(aud));

			this.sponsorshipService.delete(sponsorship);
			Assert.isTrue(!this.sponsorshipService.findAll().contains(sponsorship));
			this.sponsorshipService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
