
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("select h from Student h where h.userAccount.id=?1")
	Student findByUserId(Integer studentId);

	/** The average, minimum, maximum and standard deviation of the number of applications per student */
	@Query("select avg(1.0+ (select count(p) from Application p where p.student.id=c.id) -1.0), min(1.0+ (select count(p) from Application p where p.student.id=c.id) -1.0), max(1.0+ (select count(p) from Application p where p.student.id=c.id) -1.0), stddev(1.0+ (select count(p) from Application p where p.student.id=c.id) -1.0) from Student c")
	Double[] getStatisticsOfApplicationsPerStudent();

	/** Students who have made more applications **/
	@Query("select g from Student g where (1.0 + (select count(e) from Application e where e.student.id=g.id) - 1.0)=(select max(1.0 + (select count(en) from Application en where en.student.id=b.id) - 1.0) from Student b)")
	Collection<Student> getStudentsMoreApplications();

	@Query("select c.student from Curricula c where c.id=?1")
	Student findStudentByCurricula(int id);

	@Query("select c.student from Curricula c where c.personalRecord.id=?1")
	Student findStudentByPersonalData(int id);

	@Query("select c.student from Curricula c join c.miscellaneous m where m.id=?1")
	Student findStudentByMiscellaneous(int id);

	@Query("select c.student from Curricula c join c.positions m where m.id=?1")
	Student findStudentByPositionDatas(int id);

	@Query("select c.student from Curricula c join c.educations m where m.id=?1")
	Student findStudentByEducationDatas(int id);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.educations e where e.id=?2 and c.student.id=?1")
	Boolean hasEducationData(int studentId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.positions e where e.id=?2 and c.student.id=?1")
	Boolean hasPositionData(int studentId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.miscellaneous e where e.id=?2 and c.student.id=?1")
	Boolean hasMiscellaneousData(int studentId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c where c.id=?2 and c.student.id=?1")
	Boolean hasCurricula(int studentId, int dataId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c  where c.personalRecord.id=?2 and c.student.id=?1")
	Boolean hasPersonalData(int studentId, int dataId);

	@Query("select a.student from Application a where a.curricula.id=?1")
	Student findStudentByCopyCurricula(int id);

}
