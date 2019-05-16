
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Item;
import domain.Provider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ItemServiceTest extends AbstractTest {

	// Services
	@Autowired
	private ItemService		itemService;

	@Autowired
	private ProviderService	providerService;


	@Test
	public void driverCreateSave() {

		final Collection<String> links = new ArrayList<String>();
		links.add("http://link1.com");
		final Object testingData[][] = {
			{
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider crea Item 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "name test 1", "description test 1", links, "http://photo.com", null
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Negativo: Un member intenta crear una Item sin grado
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", null, "description test 1", links, "http://photo.com", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider crea Item con descripción en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "name test 1", "", links, "http://photo.com", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider crea Item con photo que no se corresponde con pattern de URL
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "name test 1", "description test 1", links, "photo no url", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreateSave(final String user, final String name, final String description, final Collection<String> links, final String photo, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final Item item = this.itemService.create();
			item.setName(name);
			item.setDescription(description);
			item.setLinks(links);
			item.setPhoto(photo);
			final Item res = this.itemService.save(item);
			Assert.notNull(res);
			this.itemService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(oops.getMessage());
		}

		super.checkExceptions(expected, caught);
	}
	@Test
	public void driverEdit() {
		final Collection<String> links = new ArrayList<String>();
		links.add("http://link1.com");
		final Object testingData[][] = {
			{
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider crea Item 
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "item1", "name test 1", "description test 1", links, "http://photo.com", null
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Negativo: Un member intenta crear una Item sin grado
				//			C: 32,65% Recorre 16 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "item1", null, "description test 1", links, "http://photo.com", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider crea Item con descripción en blanco
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "item1", "name test 1", "", links, "http://photo.com", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider crea Item con photo que no se corresponde con pattern de URL
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "item1", "name test 1", "description test 1", links, "photo no url", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider intenta actualizar Item que no es suyo
				//			C: 100% Recorre 49 de las 49 lineas posibles
				//			D: cobertura de datos=6/405
				"provider1", "item3", "name test 1", "description test 1", links, "http://photo.com", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Collection<String>) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateEdit(final String user, final String item, final String name, final String description, final Collection<String> links, final String photo, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final Provider principal = this.providerService.findByPrincipal();
			final Item itemFound = this.itemService.findOne(this.getEntityId(item));
			itemFound.setName(name);
			itemFound.setDescription(description);
			itemFound.setLinks(links);
			itemFound.setPhoto(photo);
			final Item res = this.itemService.save(itemFound);
			Assert.notNull(res);
			this.itemService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverDelete() {

		final Object testingData[][] = {
			{
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Positivo: Provider borra Item 
				//			C: 100% Recorre 78 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"provider1", "item1", null
			}, {
				//			A: Acme ProviderRank Req. 17 -> Providers can manage their history
				//			B: Test Negativo: Provider intenta borrr item que no es suyo
				//			C: 10,25% Recorre 8 de las 78 lineas posibles
				//			D: cobertura de datos=1/3
				"provider1", "item3", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateDelete(final String actor, final String item, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final Provider provider = this.providerService.findByPrincipal();
			final Item itemFound = this.itemService.findOne(this.getEntityId(item));
			this.itemService.delete(itemFound);
			Assert.isTrue(!this.itemService.findAll().contains(itemFound));
			this.itemService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
