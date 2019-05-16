
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	@Query("select i from Item i where i.provider.id=?1")
	Collection<Item> findAllByProvider(Integer providerId);

}
