
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

	@Query("select l from Lesson l where l.ticker = ?1")
	Collection<Lesson> getLessonWithTicker(String ticker);

	@Query("select l from Lesson l where l.teacher.userAccount.id=?1")
	Collection<Lesson> findAllLessonByTeacherId(int teacherUAId);

	@Query("select l from Lesson l where l.isDraft = false")
	Collection<Lesson> findAllFinalMode();

}
