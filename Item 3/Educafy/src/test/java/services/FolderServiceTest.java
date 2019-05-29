
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Actor;
import domain.Folder;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FolderServiceTest extends AbstractTest {

	@Autowired
	private FolderService	folderService;

	@Autowired
	private ActorService	actorService;


	/* ========================= Test Create and Save Folder =========================== */

	@Test
	public void driverCreateAndSaveFolder() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar una carpeta
				//				B: Test Positivo: Creación correcta de un comment
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "carpeta1", false, new ArrayList<>(), null
			}, {
				//				A: Educafy Crear y guardar un assesment
				//				B: Test Negativo: Creación incorrecta de un comment, text vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "carpeta1", false, new ArrayList<>(), ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Collection<Message>) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String admin, final String name, final Boolean isSystemFolder, final Collection<Message> messages, final Class<?> expected) {

		Class<?> caught;
		final Folder folder;
		final Actor principal = this.actorService.findByPrincipal();

		caught = null;

		try {
			this.authenticate(admin);

			folder = this.folderService.create();
			folder.setName(name);
			folder.setIsSystemFolder(isSystemFolder);
			folder.setMessages(messages);

			this.folderService.save(folder, principal);
			this.folderService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/* ========================= Test Edit Folder =========================== */

	@Test
	public void driverEditFolder() {

		final Object testingData[][] = {
			{
				//				A: Educafy Crear y guardar una carpeta
				//				B: Test Positivo: Creación correcta de un comment
				//				C: % Recorre 196 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "carpeta1", false, new ArrayList<>(), null
			}, {
				//				A: Educafy Crear y guardar un assesment
				//				B: Test Negativo: Creación incorrecta de un comment, text vacío
				//				C: % Recorre 54 de la 196 lineas posibles
				//				D: % cobertura de datos=8/32 (casos cubiertos / combinaciones posibles de atributos entre ellos)
				"admin1", "carpeta1", false, new ArrayList<>(), ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Collection<Message>) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateEdit(final String admin, final String name, final Boolean isSystemFolder, final Collection<Message> messages, final Class<?> expected) {

		Class<?> caught;
		final Folder folder;
		final Actor principal = this.actorService.findByPrincipal();

		caught = null;

		try {
			this.authenticate(admin);

			folder = this.folderService.create();
			folder.setName(name);
			folder.setIsSystemFolder(isSystemFolder);
			folder.setMessages(messages);

			this.folderService.save(folder, principal);
			this.folderService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
