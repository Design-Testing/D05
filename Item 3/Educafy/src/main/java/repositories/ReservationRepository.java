
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reservation;
import domain.Student;
import domain.Teacher;

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

	/** The providers who have a number of sponsorships that is at least 10% above the average number of sponsorships per provider. */

	@Query("select distinct p from Student p where (1.0*(select count(ship) from Reservation ship join ship.student prov where prov.id=p.id))>=(1.1*(select avg(1.0+(select count(sp) from Reservation sp where sp.student.id=pro.id)-1.0) from Student pro))")
	Collection<Student> findTenPerCentMoreReservationThanAverage();

	@Query("select distinct p from Teacher p where (1.0*(select count(ship) from Reservation ship join ship.lesson lesn join lesn.teacher prov where (ship.status='FINAL') AND (prov.id=p.id)))>=(1.1*(select avg(1.0+(select count(sp) from Reservation sp join sp.lesson lsnp where lsnp.teacher.id=pro.id)-1.0) from Teacher pro))")
	Collection<Teacher> findTenPerCentMoreFinalReservationThanAverage();

	@Query("select avg(ex.score), min(ex.score), max(ex.score), stddev(ex.score) from Reservation r join r.exams ex where r.student.id=?1")
	Double[] getStatisticsOfPassExams(int studentId);

}
