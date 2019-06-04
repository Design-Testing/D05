
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

	@Query("select l from Reservation r join r.lesson l where r.student.userAccount.id=?1")
	Collection<Lesson> findAllLessonByStudentId(int studentUAId);

	@Query("select l from Lesson l where l.subject.id =?1")
	Collection<Lesson> findAllBySubject(Integer subjectId);

	// select distinct le from Lesson le join le.subject s join le.teacher t where (le.isDraft=FALSE) AND ('Lesson'='' OR le.description LIKE CONCAT('%','Lesson','%') OR le.title LIKE CONCAT('%','Lesson','%') OR le.ticker LIKE CONCAT('%','Lesson','%') OR le.description LIKE CONCAT('%','Lesson','%')) AND ('1ESO'='' OR '1ESO'=s.level) AND ('Subject1'='' OR 'Subject1'=s.nameEs OR 'Subject1'=s.nameEn) AND ('tea'='' OR t.name LIKE CONCAT('%','tea','%'));
	@Query("select distinct le from Lesson le join le.subject s join le.teacher t where (le.isDraft=FALSE) AND (?1='' OR le.description LIKE CONCAT('%',?1,'%') OR le.title LIKE CONCAT('%',?1,'%') OR le.ticker LIKE CONCAT('%',?1,'%') OR le.description LIKE CONCAT('%',?1,'%')) AND (?2='' OR ?2=s.level) AND (?3='' OR ?3=s.nameEs OR ?3=s.nameEn) AND (?4='' OR t.name LIKE CONCAT('%',?4,'%'))")
	Collection<Lesson> findLessons(String keyword, String subjectLevel, String subjectName, String teacherName);

	/** The average, minimum, maximum and standard deviation of lessons per teacher */
	@Query("select avg(1.0+ (select count(p) from Lesson p where p.teacher.id=c.id) -1.0), min(1.0+ (select count(p) from Lesson p where p.teacher.id=c.id) -1.0), max(1.0+ (select count(p) from Lesson p where p.teacher.id=c.id) -1.0), stddev(1.0+ (select count(p) from Lesson p where p.teacher.id=c.id) -1.0) from Teacher c")
	Double[] getStatisticsOfLessonsPerTeacher();

	@Query("select avg(c.price), min(c.price), max(c.price), stddev(c.price) from Lesson c")
	Double[] getStatisticsOfLessonPrice();

	@Query("select avg(1.0+ (select count(p) from Reservation p where p.lesson.id=c.id) -1.0), min(1.0+ (select count(p) from Reservation p where p.lesson.id=c.id) -1.0), max(1.0+ (select count(p) from Reservation p where p.lesson.id=c.id) -1.0), stddev(1.0+ (select count(p) from Reservation p where p.lesson.id=c.id) -1.0) from Lesson c")
	Double[] getStatisticsOfReservationPerLesson();

}
