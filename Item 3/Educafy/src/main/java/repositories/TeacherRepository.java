
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

	@Query("select a from Teacher a where a.userAccount.id=?1")
	Teacher findByUserId(Integer id);

	@Query("select c.teacher from Curriculum c where c.id=?1")
	Teacher findTeacherByCurriculum(int id);

	@Query("select c.teacher from Curriculum c where c.personalRecord.id=?1")
	Teacher findTeacherByPersonalRecord(int id);

	@Query("select c.teacher from Curriculum c join c.educationRecords e where e.id=?1")
	Teacher findTeacherByEducationRecord(int id);

	@Query("select c.teacher from Curriculum c join c.miscellaneousRecords e where e.id=?1")
	Teacher findTeacherByMiscellaneousRecord(int id);

	@Query("select case when (count(c) > 0) then true else false end from Curriculum c where c.personalRecord.id=?2 and c.teacher.id=?1")
	boolean hasPersonalRecord(int teacherId, int recordId);

	@Query("select case when (count(c) > 0) then true else false end from Curriculum c join c.educationRecords e where e.id=?2 and c.teacher.id=?1")
	boolean hasEducationRecord(int teacherId, int recordId);

	@Query("select case when (count(c) > 0) then true else false end from Curriculum c join c.miscellaneousRecords e where e.id=?2 and c.teacher.id=?1")
	boolean hasMiscellaneousRecord(int teacherId, int recordId);

	@Query("select l.teacher from Reservation r join r.lesson l where r.id=?1")
	Teacher findTeacherByReservation(int reservationId);

}
