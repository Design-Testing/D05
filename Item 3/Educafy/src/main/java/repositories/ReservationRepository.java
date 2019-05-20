
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;
import domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Company, Integer> {

	@Query("select r from Reservation r join r.lesson l where l.id=?1")
	public Collection<Reservation> findAllReservationByLesson(int lessonId);

}
