
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.ticker = ?1")
	Collection<Position> getPositionWithTicker(String ticker);

	@Query("select p from Position p where p.company.userAccount.id=?1")
	Collection<Position> findAllPositionByCompanyId(int id);

	@Query("select p from Position p where p.mode = 'FINAL'")
	Collection<Position> findAllFinalMode();

	@Query("select p from Position p where p.mode='FINAL' AND p.company.id=?1")
	Collection<Position> findAllFinalModeByCompany(int id);

	@Query("select p from Position p where p.company.id=?1")
	Collection<Position> findAllByCompany(int companyId);

	/** The average, minimum, maximum and standard deviation of the salary offered */
	@Query("select avg(p.salary), min(p.salary), max(p.salary), stddev(p.salary) from Position p")
	Double[] getStatisticsOfSalary();

	/** The best position in terms of salary. */
	@Query("select p from Position p where p.salary=(select max(po.salary) from Position po)")
	Position[] getBestPosition();

	/** The worst position in terms of salary. */
	@Query("select p from Position p where p.salary=(select min(po.salary) from Position po)")
	Position[] getWorstPosition();

	@Query("select distinct p from Position p join p.skills s join p.technologies tec where ((p.mode='FINAL') OR (p.mode='CANCELLED')) AND (?1='' OR p.description LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.ticker LIKE CONCAT('%',?1,'%') OR s LIKE CONCAT('%',?1,'%') OR tec LIKE CONCAT('%',?1,'%')) AND ((p.salary>=?2) OR ?2=NULL) AND ((p.salary<=?3) OR ?3=NULL) AND ((p.deadline>=?4) OR ?4=NULL) AND ((p.deadline<=?5) OR ?5=NULL)")
	Collection<Position> findPositions(String keyword, Double minSalary, Double maxSalary, Date minDeadline, Date maxDeadline);

	@Query("select distinct p from Position p join p.skills s join p.technologies tec join p.company cn where ((p.mode='FINAL') OR (p.mode='CANCELLED')) AND (?1='' OR p.description LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.ticker LIKE CONCAT('%',?1,'%') OR s LIKE CONCAT('%',?1,'%') OR tec LIKE CONCAT('%',?1,'%') OR cn.commercialName LIKE CONCAT('%',?1,'%'))")
	Collection<Position> findByKeyword(String keyword);

	@Query("select distinct p from Application ap join ap.position p join ap.rooky h where h.id=?1")
	Collection<Position> findAppliedByRooky(int id);

	@Query("select distinct p from Audit a join a.position p where a.auditor.id=?1 and p.mode='FINAL'")
	Collection<Position> findAuditedPositionsByAuditor(int auditorId);

	@Query("select p from Position p where p.mode='FINAL'")
	Collection<Position> findAllFinal();

	/* ==========Acme Rookies========== */

	/** The average, minimum, maximum and standard deviation of the audit score of the positions stored in the system */
	@Query("select avg(au.score), min(au.score), max(au.score), stddev(au.score) from Audit au where au.isDraft=false")
	Double[] getStatisticsOfAuditScoreOfPositions();

	@Query("select avg(1.0+(select au.score from Audit au where au.position.id=?1 AND au.auditor.id=aut.id)-1.0), min(1.0+(select au.score from Audit au where au.position.id=?1 AND au.auditor.id=aut.id)-1.0), max(1.0+(select au.score from Audit au where au.position.id=?1 AND au.auditor.id=aut.id)-1.0), stddev(1.0+(select au.score from Audit au where au.position.id=?1 AND au.auditor.id=aut.id)-1.0) from Auditor aut")
	Double[] getStatisticsOfAuditScoreOfPosition(int positionId);

	/** The average salary offered by the positions that have the highest average audit score */
	@Query("select avg(p.salary) from Audit au join au.position p where (au.isDraft=false) AND au.score=(select avg(au.score) from Audit au where au.isDraft=false)")
	Double getAvgSalaryOfPositionsHighestAvgAuditScore();

	@Query("select sp.position from Sponsorship sp where sp.provider.userAccount.id=?1")
	Collection<Position> findAllParadeByProvider(int providerUAId);

}
