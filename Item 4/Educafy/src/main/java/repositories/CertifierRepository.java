
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Certifier;

@Repository
public interface CertifierRepository extends JpaRepository<Certifier, Integer> {

	@Query("select a from Certifier a where a.userAccount.id=?1")
	Certifier findByUserId(Integer id);

}
