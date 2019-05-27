
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

	@Query("select r.exams from Reservation r where r.student.userAccount.id=?1")
	public Collection<Exam> findAllExamsByStudent(int studentId);

}
