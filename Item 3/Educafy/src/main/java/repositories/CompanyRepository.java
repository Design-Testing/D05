
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	@Query("select c from Company c where c.userAccount.id=?1")
	Company findByUserId(Integer companyId);

	@Query("select p.company from Problem p where p.id=?1")
	Company findCompanyByProblem(int problemId);

	@Query("select p.company from Position p where p.id=?1")
	Company findCompanyByPosition(int positionId);

	/** The average, minimum, maximum and standard deviation of the number of positions per company */
	@Query("select avg(1.0+ (select count(p) from Position p where p.company.id=c.id) -1.0), min(1.0+ (select count(p) from Position p where p.company.id=c.id) -1.0), max(1.0+ (select count(p) from Position p where p.company.id=c.id) -1.0), stddev(1.0+ (select count(p) from Position p where p.company.id=c.id) -1.0) from Company c")
	Double[] getStatisticsOfPositionsPerCompany();

	/** Companies that have offered more positions **/
	@Query("select g from Company g where (1.0 + (select count(e) from Position e where e.company.id=g.id) - 1.0)=(select max(1.0 + (select count(en) from Position en where en.company.id=b.id) - 1.0) from Company b)")
	Collection<Company> getCompaniesMorePositions();

	/* ==========Acme Rookies========== */

	/** The average, minimum, maximum and standard deviation of the audit score of the companies that are registered in the system */
	@Query("select avg(c.score), min(c.score), max(c.score), stddev(c.score) from Company c")
	Double[] getStatisticsOfScoreOfCompanies();

	@Query("select avg(1.0+(select au.score from Audit au where au.position.company.id=?1 AND au.auditor.id=aut.id)-1.0), min(1.0+(select au.score from Audit au where au.position.company.id=?1 AND au.auditor.id=aut.id)-1.0), max(1.0+(select au.score from Audit au where au.position.company.id=?1 AND au.auditor.id=aut.id)-1.0), stddev(1.0+(select au.score from Audit au where au.position.company.id=?1 AND au.auditor.id=aut.id)-1.0) from Auditor aut")
	Double[] getStatisticsOfAuditScoreOfCompany(int companyId);

	/** The companies with the highest audit score */
	@Query("select g from Company g where g.score=(select max(b.score) from Company b)")
	Collection<Company> getCompaniesHighestAuditScore();

	@Query("select avg(1.0 + (select au.score from Audit au where au.id=auu.id) -1.0) from Audit auu where auu.position.company.id=?1 and auu.score IS NOT NULL")
	Double getAvgScore(int companyId);

	@Query("select min(1.0 + (select au.score from Audit au where au.id=auu.id) -1.0) from Audit auu where auu.position.company.id=?1 and auu.score IS NOT NULL")
	Integer getMinScore(int companyId);

	@Query("select max(1.0 + (select au.score from Audit au where au.id=auu.id) -1.0) from Audit auu where auu.position.company.id=?1 and auu.score IS NOT NULL")
	Integer getMaxScore(int companyId);

}
