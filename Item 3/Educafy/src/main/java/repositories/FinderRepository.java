
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	// TODO: check queries when Populate includes Finder 

	/** The average, minimum, maximum and standard deviation of the number of positions per company */
	@Query("select avg(fi.positions.size), min(fi.positions.size), max(fi.positions.size), stddev(fi.positions.size) from Finder fi")
	Double[] getStatisticsOfPositionsPerFinder();

	/** The ratio of empty versus non empty finders */
	@Query("select sum(case when f.positions.size=0 then 1.0 else 0.0 end) / sum(case when f.positions.size>0 then 1.0 else 0.0 end) from Finder f")
	Double findRatioFinders();

	@Query("select m.finder from Rooky m where m.id=?1")
	Finder findRookyFinder(int id);
}
