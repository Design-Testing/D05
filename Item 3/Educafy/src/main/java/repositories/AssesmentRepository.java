
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Assesment;

@Repository
public interface AssesmentRepository extends JpaRepository<Assesment, Integer> {

	@Query("select a from Assesment a where a.student.userAccount.id=?1")
	Collection<Assesment> findAllAssesmentByStudentId(int studentUAId);

}
