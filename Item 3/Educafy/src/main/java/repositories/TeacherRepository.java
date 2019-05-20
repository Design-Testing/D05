
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

	@Query("select a from Teacher a where a.userAccount.id=?1")
	Teacher findByUserId(Integer id);

	@Query("select c.teacher from Curricula c where c.id=?1")
	Teacher findTeacherByCurricula(int curriculaId);

	@Query("select c.teacher from Curricula c where c.personalRecord.id=?1")
	Teacher findTeacherByPersonalRecord(int personalRecorId);

	@Query("select c.teacher from Curricula c join c.miscellaneous m where m.id=?1")
	Teacher findTeacherByMiscellaneous(int miscellaneousRecordId);

	@Query("select c.teacher from Curricula c join c.educations m where m.id=?1")
	Teacher findTeacherByEducationRecords(int educationRecordId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c  where c.personalRecord.id=?2 and c.teacher.id=?1")
	Boolean hasPersonalRecord(int teacherId, int personalRecordId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.educations e where e.id=?2 and c.teacher.id=?1")
	Boolean hasEducationRecord(int teacherId, int educationRecordId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c join c.miscellaneous e where e.id=?2 and c.teacher.id=?1")
	Boolean hasMiscellaneousRecord(int teacherId, int miscellaneousRecordId);

	@Query("select case when (count(c) > 0) then true else false end from Curricula c where c.id=?2 and c.teacher.id=?1")
	Boolean hasCurricula(int teacherId, int curriculaId);

	//	@Query("select a.teacher from Application a where a.curricula.id=?1")
	//	Teacher findTeacherByCopyCurricula(int curriculaId);

}
