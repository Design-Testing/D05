
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@Query("select r from Reservation r join r.lesson l where l.id=?1")
	public Collection<Reservation> findAllReservationByLesson(int lessonId);

	@Query("select r from Reservation r where r.student.userAccount.id=?1")
	public Collection<Reservation> findAllReservationByStudent(int studentId);

}
