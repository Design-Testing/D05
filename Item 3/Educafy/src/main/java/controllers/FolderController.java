
package controllers;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import domain.Actor;
import domain.Folder;
import forms.FolderForm;

@Controller
@RequestMapping("/folder")
public class FolderController extends AbstractController {

	@Autowired
	private FolderService		folderService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageController	messageController;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Actor actor = this.actorService.findByPrincipal();
		final Collection<Folder> folders = this.folderService.findAllByUserId(actor.getUserAccount().getId());

		res = new ModelAndView("folder/list");
		res.addObject("folders", folders);
		res.addObject("requestURI", "folder/list.do");

		return res;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Folder folder;

		folder = this.folderService.create();
		res = this.createEditModelAndView(folder);

		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;

		folder = this.folderService.findOne(folderId);
		if (folder.getIsSystemFolder()) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", "You can't edit or delete system folder");
			return result;
		} else if (!folder.getActor().equals(this.actorService.findByPrincipal())) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", "You can't edit or delete anyone else's folder");
			return result;
		} else {
			Assert.notNull(folder);
			result = this.createEditModelAndView(folder);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final FolderForm folderForm, final BindingResult binding) {
		ModelAndView res;
		final Actor principal = this.actorService.findByPrincipal();

		if (binding.hasErrors())
			res = this.createEditModelAndView(folderForm);
		else
			try {
				final Folder folder = this.folderService.reconstruct(folderForm, binding);
				this.folderService.save(folder, principal);
				res = new ModelAndView("redirect:list.do");
			} catch (final ValidationException oops) {
				res = this.createEditModelAndView(folderForm, "folder.commit.error");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(folderForm, "folder.commit.error");
			}
		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int folderId) {
		ModelAndView result;
		final Folder folder = this.folderService.findOne(folderId);
		if (folder.getIsSystemFolder()) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", "You can't edit or delete system folder");
		} else
			try {
				this.folderService.delete(folder);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(folder, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;

		folder = this.folderService.findOne(folderId);

		if (folder == null)
			result = new ModelAndView("redirect:misc/403");
		else {
			result = this.messageController.list(folderId);
			result.addObject("folder", folder);
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView res;
		res = this.createEditModelAndView(folder, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final Folder folder, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("folder/edit");
		res.addObject("folderForm", this.constructPruned(folder));
		res.addObject("message", messageCode);
		return res;
	}

	private FolderForm constructPruned(final Folder folder) {
		final FolderForm pruned = new FolderForm();
		pruned.setId(folder.getId());
		pruned.setVersion(folder.getVersion());
		pruned.setName(folder.getName());
		return pruned;
	}

	protected ModelAndView createEditModelAndView(final FolderForm folder) {
		ModelAndView res;
		res = this.createEditModelAndView(folder, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final FolderForm folder, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("folder/edit");
		res.addObject("folderForm", folder);
		res.addObject("message", messageCode);
		return res;
	}
}
