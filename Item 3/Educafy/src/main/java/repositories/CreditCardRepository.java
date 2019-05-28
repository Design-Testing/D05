
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

	@Query("select ap.creditCard from Reservation ap where ap.id=?1")
	CreditCard findByReservationId(Integer id);

	@Query("select c from CreditCard c where c.actor.userAccount.id=?1")
	Collection<CreditCard> findAllByActorUserId(Integer id);

	@Query("select c from CreditCard c where c.actor.userAccount.id=?1")
	Collection<CreditCard> findAllByUserId(Integer id);
}
