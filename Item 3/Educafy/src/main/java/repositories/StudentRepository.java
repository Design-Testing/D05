
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("select h from Student h where h.userAccount.id=?1")
	Student findByUserId(Integer studentId);

}
