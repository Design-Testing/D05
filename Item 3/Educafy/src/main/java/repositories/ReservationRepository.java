
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@Query("select r from Reservation r join r.lesson l where l.id=?1")
	Collection<Reservation> findAllReservationByLesson(int lessonId);

	@Query("select r from Reservation r where r.student.userAccount.id=?1")
	Collection<Reservation> findAllReservationByStudent(int studentId);

	@Query("select a from Reservation a where a.creditCard.id=?1")
	Collection<Reservation> findAllByCreditCard(int creditCardId);

	@Query("select sum(case when a.status='PENDING' then 1.0 else 0.0 end) / count(a) from Reservation a")
	Double findPendingReservationRatio();

	@Query("select sum(case when a.status='ACCEPTED' then 1.0 else 0.0 end) / count(a) from Reservation a")
	Double findAcceptedReservationRatio();

	@Query("select sum(case when a.status='REJECTED' then 1.0 else 0.0 end) / count(a) from Reservation a")
	Double findRejectedReservationRatio();

	@Query("select sum(case when a.status='FINAL' then 1.0 else 0.0 end) / sum(case when a.status='REJECTED' then 1.0 else 0.0 end) from Reservation a")
	Double findFinalOverRejectedReservationRatio();

	@Query("select avg(r.cost), min(r.cost), max(r.cost), stddev(r.cost) from Reservation r")
	Double[] getStatisticsOfWeeklyCost();

	@Query("select r from Reservation r join r.lesson l where l.teacher.userAccount.id=?1")
	Collection<Reservation> findAllReservationByTeacher(int teacherId);

}
