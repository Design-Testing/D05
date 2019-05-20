
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reservation;
import domain.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

	@Query("select r from Reservation r join r.lesson l where l.id=?1")
	public Collection<Reservation> findAllReservationByLesson(int lessonId);

}
