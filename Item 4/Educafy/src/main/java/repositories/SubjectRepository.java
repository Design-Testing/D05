
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	@Query("select s.level from Subject s")
	Collection<String> subjectLevels();

	@Query("select s.nameEs from Subject s")
	Collection<String> subjectNamesEs();

	@Query("select s.nameEn from Subject s")
	Collection<String> subjectNamesEn();

}
