
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Rooky;

public interface RookyRepository extends JpaRepository<Rooky, Integer> {

	@Query("select h from Rooky h where h.userAccount.id=?1")
	Rooky findByUserId(Integer rookyId);

	/** The average, minimum, maximum and standard deviation of the number of applications per rooky */
	@Query("select avg(1.0+ (select count(p) from Application p where p.rooky.id=c.id) -1.0), min(1.0+ (select count(p) from Application p where p.rooky.id=c.id) -1.0), max(1.0+ (select count(p) from Application p where p.rooky.id=c.id) -1.0), stddev(1.0+ (select count(p) from Application p where p.rooky.id=c.id) -1.0) from Rooky c")
	Double[] getStatisticsOfApplicationsPerRooky();

	/** Rookys who have made more applications **/
	@Query("select g from Rooky g where (1.0 + (select count(e) from Application e where e.rooky.id=g.id) - 1.0)=(select max(1.0 + (select count(en) from Application en where en.rooky.id=b.id) - 1.0) from Rooky b)")
	Collection<Rooky> getRookysMoreApplications();

	@Query("select c.rooky from Curricula c where c.id=?1")
	Rooky findRookyByCurricula(int id);

	@Query("select c.rooky from Curricula c where c.personalRecord.id=?1")
	Rooky findRookyByPersonalData(int id);

	@Query("select c.rooky from Curricula c join c.miscellaneous m where m.id=?1")
	Rooky findRookyByMiscellaneous(int id);

	@Query("select c.rooky from Curricula c join c.positions m where m.id=?1")
	Rooky findRookyByPositionDatas(int id);

	@Query("select c.rooky from Curricula c join c.educations m where m.id=?1")
	Rooky findRookyByEducationDatas(int id);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.educations e where e.id=?2 and c.rooky.id=?1")
	Boolean hasEducationData(int rookyId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.positions e where e.id=?2 and c.rooky.id=?1")
	Boolean hasPositionData(int rookyId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.miscellaneous e where e.id=?2 and c.rooky.id=?1")
	Boolean hasMiscellaneousData(int rookyId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c where c.id=?2 and c.rooky.id=?1")
	Boolean hasCurricula(int rookyId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c  where c.personalRecord.id=?2 and c.rooky.id=?1")
	Boolean hasPersonalData(int rookyId, int dataId);

	@Query("select a.rooky from Application a where a.curricula.id=?1")
	Rooky findRookyByCopyCurricula(int id);

}
