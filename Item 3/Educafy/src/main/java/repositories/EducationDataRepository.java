package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.EducationRecord;

@Repository
public interface EducationDataRepository extends JpaRepository<EducationRecord, Integer>{

}
