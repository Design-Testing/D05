
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import services.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

	@Query("select distinct p from Lesson p join p.skills s join p.technologies tec where ((p.mode='FINAL') OR (p.mode='CANCELLED')) AND (?1='' OR p.description LIKE CONCAT('%',?1,'%') OR p.title LIKE CONCAT('%',?1,'%') OR p.ticker LIKE CONCAT('%',?1,'%') OR s LIKE CONCAT('%',?1,'%') OR tec LIKE CONCAT('%',?1,'%')) AND ((p.salary>=?2) OR ?2=NULL) AND ((p.salary<=?3) OR ?3=NULL) AND ((p.deadline>=?4) OR ?4=NULL) AND ((p.deadline<=?5) OR ?5=NULL)")
	Collection<Lesson> findLessons(String keyword, Double minSalary, Double maxSalary, Date minDeadline, Date maxDeadline);

}
