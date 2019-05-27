
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("select h from Student h where h.userAccount.id=?1")
	Student findByUserId(Integer studentId);

	@Query("select s from Student s order by 1.0+(select avg(ex.score) from Reservation sp join sp.exams ex where sp.student.id=s.id) desc")
	List<Student> getStudentsOrderByExamScore();

	@Query("select distinct p from Student p where (1.0*(select count(ship) from Reservation ship join ship.student prov where prov.id=p.id))>=(1.1*(select avg(1.0+(select count(sp) from Reservation sp where sp.student.id=pro.id)-1.0) from Student pro))")
	Collection<Student> findTenPerCentMoreReservationThanAverage();

}
