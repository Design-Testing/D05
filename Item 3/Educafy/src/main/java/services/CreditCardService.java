
package services;

import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Reservation;
import domain.Student;

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
		final Student principal = this.studentService.findByPrincipal();
		res = this.creditCardRepository.findAllByUserId(principal.getUserAccount().getId());
		Assert.notNull(res);

		return res;
	}

	// findOne
	public CreditCard findOne(final int id) {
		final Student s = this.studentService.findByPrincipal();
		final CreditCard creditCard = this.creditCardRepository.findOne(id);
		Assert.isTrue(creditCard.getActor().getId() == s.getId());
		return creditCard;
	}

	public CreditCard findByReservationId(final int id) {
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
		CreditCard retrieved;
		final Student principal = this.studentService.findByPrincipal();
		if (c.getId() == 0)
			c.setActor(principal);
		else {
			retrieved = this.findOne(c.getId()); // ya en el findOne compruebo que sea su creditCard
			c.setActor(retrieved.getActor());
		}
		Assert.isTrue(!this.tarjetaCaducada(c));
		final String s = c.getNumber().replace(" ", "");
		c.setNumber(s);
		return this.creditCardRepository.save(c);
	}
	// delete
	public void delete(final int creditCardId) {
		Assert.isTrue(creditCardId != 0);
		final CreditCard c = this.findOne(creditCardId);
		Assert.isTrue(this.findAll().contains(c));

		final Collection<Reservation> reservations = this.reservationService.findAllByCreditCard(c.getId());
		Assert.isTrue(reservations.isEmpty());

		this.creditCardRepository.delete(c);
	}

	// auxiliar
	public boolean tarjetaCaducada(final CreditCard c) {
		boolean res = false;
		final boolean mesCaducado = c.getExpirationMonth() < LocalDate.now().getMonthOfYear();
		final boolean mismoAnyo = (2000 + c.getExpirationYear()) == LocalDate.now().getYear();
		final boolean anyoCaducado = (2000 + c.getExpirationYear()) < LocalDate.now().getYear();
		if (anyoCaducado || (mismoAnyo && mesCaducado))
			res = true;

		return res;
	}
}
