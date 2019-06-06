
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	// TODO: check queries when Populate includes Finder 

	@Query("select m.finder from Student m where m.id=?1")
	Finder findStudentFinder(int id);
}
