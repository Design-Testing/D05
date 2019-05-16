
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

	@Query("select h from Provider h where h.userAccount.id=?1")
	Provider findByUserId(Integer providerId);

	@Query("select i.provider from Item i where i.id=?1")
	Provider findByItem(int itemId);

	/** The providers who have a number of sponsorships that is at least 10% above the average number of sponsorships per provider. */
	@Query("select distinct p from Provider p where (1.0*(select count(ship) from Sponsorship ship join ship.provider prov where prov.id=p.id))>=(1.1*(select avg(1.0+(select count(sp) from Sponsorship sp where sp.provider.id=pro.id)-1.0) from Provider pro))")
	Collection<Provider> findTenPerCentMoreAppsThanAverage();

}
