
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ItemRepository;
import domain.Item;
import domain.Provider;
import forms.ItemForm;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository	itemRepository;

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private Validator		validator;


	public Item create() {
		final Item item = new Item();
		final Collection<String> links = new ArrayList<String>();
		item.setLinks(links);
		final Provider logged = this.providerService.findByPrincipal();
		item.setProvider(logged);
		return item;
	}

	public Collection<Item> findAll() {
		Collection<Item> res = new ArrayList<>();
		res = this.itemRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Item findOne(final int itemId) {
		Assert.isTrue(itemId != 0);
		final Item res = this.itemRepository.findOne(itemId);
		Assert.notNull(res);
		return res;
	}

	public Item save(final Item item) {
		Assert.notNull(item);
		final Provider principal = this.providerService.findByPrincipal();
		final Item result;

		if (item.getId() != 0)
			Assert.isTrue(principal.equals(item.getProvider()), "No puede actualizar un item que no le pertenece.");
		else
			item.setProvider(principal);

		result = this.itemRepository.save(item);
		return result;
	}

	public void delete(final Item item) {
		Assert.notNull(item);
		Assert.isTrue(item.getId() != 0);
		final Provider principal = this.providerService.findByPrincipal();
		Assert.isTrue(principal.equals(item.getProvider()), "No puede borrar un item que no le pertenece.");
		Assert.isTrue(this.itemRepository.exists(item.getId()));
		this.itemRepository.delete(item);
	}

	/* ========================= OTHER METHODS =========================== */

	public void flush() {
		this.itemRepository.flush();
	}

	public Collection<Item> findAllByProvider(final int providerId) {
		Collection<Item> res;
		res = this.itemRepository.findAllByProvider(providerId);
		return res;
	}

	public Item reconstruct(final ItemForm itemForm, final BindingResult binding) {
		Item result;
		if (itemForm.getId() == 0)
			result = this.create();
		else
			result = this.findOne(itemForm.getId());

		result.setId(itemForm.getId());
		result.setVersion(itemForm.getVersion());
		result.setName(itemForm.getName());
		result.setDescription(itemForm.getDescription());
		result.setPhoto(itemForm.getPhoto());
		result.setLinks(itemForm.getLinks());

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public ItemForm inyect(final Item item) {
		final ItemForm pruned = new ItemForm();

		pruned.setId(item.getId());
		pruned.setVersion(item.getVersion());
		pruned.setName(item.getName());
		pruned.setDescription(item.getDescription());
		pruned.setPhoto(item.getPhoto());
		pruned.setLinks(item.getLinks());

		return pruned;
	}

}
