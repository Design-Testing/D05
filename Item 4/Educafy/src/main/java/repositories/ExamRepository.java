
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

	@Query("select e from Exam e join e.reservation r where (e.status='INPROGRESS' OR e.status='SUBMITTED' OR e.status='EVALUATED') AND r.student.userAccount.id=?1")
	Collection<Exam> findAllExamsByStudent(int studentUAId);

	@Query("select e from Exam e join e.reservation r where r.id=?1")
	Collection<Exam> findAllExamsByReservation(int reservationId);

}
