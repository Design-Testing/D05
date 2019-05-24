
package services;

import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import security.Authority;
import domain.Actor;
import domain.CreditCard;
import domain.Reservation;

@Service
@Transactional
public class CreditCardService {

	@Autowired
	ActorService			actorService;

	@Autowired
	StudentService			studentService;

	@Autowired
	CreditCardRepository	creditCardRepository;

	@Autowired
	ReservationService		reservationService;


	// create
	public CreditCard create() {
		return new CreditCard();
	}

	// findAll
	public Collection<CreditCard> findAll() {
		final Collection<CreditCard> res;
		final Actor principal = this.actorService.findByPrincipal();
		final boolean isStudent = this.actorService.checkAuthority(principal, Authority.STUDENT);
		Assert.isTrue(isStudent);
		res = this.creditCardRepository.findAllByUserId(principal.getUserAccount().getId());
		Assert.notNull(res);

		return res;
	}

	// findOne
	public CreditCard findOne(final int id) {
		final CreditCard creditCard = this.creditCardRepository.findOne(id);

		return creditCard;
	}

	public CreditCard findByApplicationId(final int id) {
		Assert.isTrue(id != 0);
		final Actor principal = this.actorService.findByPrincipal();
		final CreditCard creditCard = this.creditCardRepository.findByReservationId(id);
		Assert.notNull(creditCard);
		Assert.isTrue(creditCard.getActor().equals(principal));

		final Collection<CreditCard> cs = this.findAll();
		Assert.isTrue(cs.contains(creditCard));

		return creditCard;
	}

	// save

	public CreditCard save(final CreditCard c) {
		Assert.notNull(c);
		final Actor principal = this.actorService.findByPrincipal();
		c.setActor(principal);
		final boolean isStudent = this.actorService.checkAuthority(principal, Authority.STUDENT);
		Assert.isTrue(isStudent);
		Assert.isTrue(!this.tarjetaCaducada(c));
		final String s = c.getNumber().replace(" ", "");
		c.setNumber(s);
		return this.creditCardRepository.save(c);
	}

	// delete
	public void delete(final CreditCard c) {
		Assert.notNull(c);
		Assert.isTrue(c.getId() != 0);
		Assert.isTrue(this.findAll().contains(c));

		final Actor principal = this.actorService.findByPrincipal();
		final boolean isStudent = this.actorService.checkAuthority(principal, Authority.STUDENT);
		Assert.isTrue(isStudent);

		if (isStudent) {
			final Collection<Reservation> reservations = this.reservationService.findAllByCreditCard(c.getId());
			Assert.isTrue(reservations.isEmpty());
		}

		this.creditCardRepository.delete(c);
	}

	// auxiliar
	boolean tarjetaCaducada(final CreditCard c) {
		boolean res = false;
		final boolean mesCaducado = c.getExpirationMonth() < LocalDate.now().getMonthOfYear();
		final boolean mismoAnyo = (2000 + c.getExpirationYear()) == LocalDate.now().getYear();
		final boolean anyoCaducado = (2000 + c.getExpirationYear()) < LocalDate.now().getYear();
		if (anyoCaducado || (mismoAnyo && mesCaducado))
			res = true;

		return res;
	}
}
