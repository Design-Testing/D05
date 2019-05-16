
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curricula;

@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer> {

	/** The average, minimum, maximum and standard deviation of the number of curricula per rooky */
	@Query("select avg(1.0+ (select count(p) from Curricula p where p.rooky.id=c.id) -1.0), min(1.0+ (select count(p) from Curricula p where p.rooky.id=c.id) -1.0), max(1.0+ (select count(p) from Curricula p where p.rooky.id=c.id) -1.0), stddev(1.0+ (select count(p) from Curricula p where p.rooky.id=c.id) -1.0) from Rooky c")
	Double[] getStatisticsOfCurriculaPerRooky();

	@Query("select c from Curricula c where c.rooky.id=?1")
	Collection<Curricula> findCurriculaByRooky(int id);

	@Query("select c from Curricula c where c.personalRecord.id=?1")
	Curricula findCurriculaByPersonalData(int id);

	@Query("select c from Curricula c join c.educations e where e.id=?1")
	Curricula findCurriculaByEducationData(int id);

	@Query("select c from Curricula c join c.positions e where e.id=?1")
	Curricula findCurriculaByPositionData(int id);

	@Query("select c from Curricula c join c.miscellaneous e where e.id=?1")
	Curricula findCurriculaByMiscellaneousData(int id);

	@Query("select a.curricula from Application a join a.position p where p.company.id=?1")
	Collection<Curricula> findCurriculasByCompany(int id);

}
