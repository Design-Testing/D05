
package controllers.provider;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ItemService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Item;
import domain.Provider;
import forms.ItemForm;

@Controller
@RequestMapping("/item/provider")
public class ItemProviderController extends AbstractController {

	@Autowired
	private ItemService		itemService;

	@Autowired
	private ProviderService	providerService;

	final String			lang	= LocaleContextHolder.getLocale().getLanguage();


	// =================LIST=================

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Item> items;

		final UserAccount uaLogged = LoginService.getPrincipal();
		final Authority auProvider = new Authority();
		auProvider.setAuthority("PROVIDER");

		result = new ModelAndView("item/list");

		if (uaLogged.getAuthorities().contains(auProvider)) {
			final Provider provider = this.providerService.findByPrincipal();
			items = this.itemService.findAllByProvider(provider.getId());
			result.addObject("buttons", true);
		} else
			items = this.itemService.findAll();

		result.addObject("items", items);
		result.addObject("lang", this.lang);
		result.addObject("requestURI", "/item/provider/list.do");
		return result;

	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {
		ModelAndView result;
		Collection<Item> items;

		result = new ModelAndView("item/list");

		items = this.itemService.findAll();

		result.addObject("items", items);
		result.addObject("buttons", false);
		result.addObject("lang", this.lang);
		result.addObject("requestURI", "/item/provider/listAll.do");
		return result;

	}

	@RequestMapping(value = "/listByProvider", method = RequestMethod.GET)
	public ModelAndView listByProvider(@RequestParam final int providerId) {
		ModelAndView result;
		Collection<Item> items;

		result = new ModelAndView("item/list");

		items = this.itemService.findAllByProvider(providerId);

		result.addObject("items", items);
		result.addObject("lang", this.lang);
		result.addObject("requestURI", "/item/provider/listByProvider.do");
		return result;

	}

	// =================CREATE=================

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Item item;

		item = this.itemService.create();
		result = new ModelAndView("item/edit");
		final ItemForm itemForm = this.itemService.inyect(item);
		result.addObject("itemForm", itemForm);
		result.addObject("item", item);

		return result;
	}
	// =================EDIT=================

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int itemId) {
		ModelAndView result;
		final Item item;

		final UserAccount uaLogged = LoginService.getPrincipal();
		final Authority auProvider = new Authority();
		auProvider.setAuthority("PROVIDER");

		item = this.itemService.findOne(itemId);

		if ((item != null) && (uaLogged.getAuthorities().contains(auProvider))) {
			final Provider provider = this.providerService.findByPrincipal();
			final Collection<Item> ss = this.itemService.findAllByProvider(provider.getId());
			if (ss.contains(item)) {
				result = new ModelAndView("item/edit");
				final ItemForm itemForm = this.itemService.inyect(item);
				result.addObject("itemForm", itemForm);
				result.addObject("item", item);
			} else
				result = new ModelAndView("redirect:/misc/403.jsp");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ItemForm itemForm, final BindingResult bindingResult) {

		ModelAndView result;
		if (bindingResult.hasErrors()) {
			result = new ModelAndView("item/edit");
			result.addObject("itemForm", itemForm);
		} else
			try {
				final Item item = this.itemService.reconstruct(itemForm, bindingResult);
				this.itemService.save(item);
				result = this.list();
			} catch (final Throwable oops) {
				result = new ModelAndView("administrator/error");
				//result.addObject("trace", oops.getMessage());
				System.out.println(oops.getMessage());
			}
		return result;
	}

	// =================DISPLAY=================

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayItem(@RequestParam final int itemId) {
		final ModelAndView result;
		Item item;

		item = this.itemService.findOne(itemId);

		if (item != null) {
			result = new ModelAndView("item/display");
			result.addObject("item", item);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// =================DELETE=================

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int itemId) {
		ModelAndView result;

		try {
			final Item item = this.itemService.findOne(itemId);
			this.itemService.delete(item);

			result = this.list();

		} catch (final Throwable e) {
			result = new ModelAndView("redirect:misc/403");
		}

		return result;
	}

}
