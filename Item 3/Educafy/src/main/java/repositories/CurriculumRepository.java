
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curriculum;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

	@Query("select c from Curriculum c where c.teacher.id=?1")
	Curriculum findCurriculumByTeacher(int id);

	@Query("select c from Curriculum c where c.personalRecord.id=?1")
	Curriculum findCurriculumByPersonalRecord(int id);

	@Query("select c from Curriculum c join c.educationRecords e where e.id=?1")
	Curriculum findCurriculumByEducationRecord(int id);

	@Query("select c from Curriculum c join c.miscellaneousRecords e where e.id=?1")
	Curriculum findCurriculumByMiscellaneousRecord(int id);

	@Query("select c from Curriculum c where c.ticker=?1")
	Collection<Curriculum> getCurriculumWithTicker(String ticker);

	@Query("select sum(case when (ed.isCertified=1 AND c.personalRecord.isCertified=TRUE AND ms.isCertified=1) then 1.0 else 0.0 end) / count(c) from Curriculum c join c.educationRecords ed join c.miscellaneousRecords ms")
	Double findCurriculumRatio();
}
