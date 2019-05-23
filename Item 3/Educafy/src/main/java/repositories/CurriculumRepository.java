
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curriculum;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

	@Query("select c from Curriculum c where c.teacher.id=?1")
	Collection<Curriculum> findCurriculumByTeacher(int id);

	@Query("select c from Curriculum c where c.personalRecord.id=?1")
	Curriculum findCurriculumByPersonalRecord(int id);

	@Query("select c from Curriculum c join c.educationRecords e where e.id=?1")
	Curriculum findCurriculumByEducationRecord(int id);

	@Query("select c from Curriculum c join c.miscellaneousRecords e where e.id=?1")
	Curriculum findCurriculumByMiscellaneousRecord(int id);

}
