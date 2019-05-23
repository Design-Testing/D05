
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

	@Query("select a from Teacher a where a.userAccount.id=?1")
	Teacher findByUserId(Integer id);

	@Query("select c.teacher from Curriculum c where c.id=?1")
	Teacher findTeacherByCurriculum(int id);

}
