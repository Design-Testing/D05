
package controllers.student;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationParametersService;
import services.CreditCardService;
import services.ReservationService;
import services.StudentService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Reservation;

@Controller
@RequestMapping("/creditCard/student")
public class CreditCardStudentController extends AbstractController {

	@Autowired
	CreditCardService				creditCardService;

	@Autowired
	StudentService					studentService;

	@Autowired
	ConfigurationParametersService	configurationParametersService;

	@Autowired
	ActorService					actorService;

	@Autowired
	ReservationService				reservationService;


	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<CreditCard> myCards;

		this.studentService.findByPrincipal();
		myCards = this.creditCardService.findAll();
		result = new ModelAndView("creditCard/list");
		result.addObject("myCards", myCards);
		result.addObject("rol", "student");
		return result;
	}

	// Create --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		CreditCard c;

		c = this.creditCardService.create();
		result = this.createEditModelAndView(c);
		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int creditCardId) {
		ModelAndView result;
		CreditCard creditCard;

		creditCard = this.creditCardService.findOne(creditCardId);

		if (creditCard != null) {
			final Collection<CreditCard> myCards = this.creditCardService.findAll();
			if (myCards.contains(creditCard)) {
				result = new ModelAndView("creditCard/display");
				result.addObject("creditCard", creditCard);
				result.addObject("rol", "student");
			} else
				result = new ModelAndView("redirect:/misc/403.jsp");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int creditCardId) {
		ModelAndView result;
		CreditCard creditCard;

		creditCard = this.creditCardService.findOne(creditCardId);

		if (creditCard != null) {
			final Collection<CreditCard> myCards = this.creditCardService.findAll();
			if (myCards.contains(creditCard))
				result = this.createEditModelAndView(creditCard);
			else
				result = new ModelAndView("redirect:/misc/403.jsp");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final CreditCard creditCard, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(creditCard);
		else
			try {
				this.creditCardService.save(creditCard);
				result = new ModelAndView("redirect:/creditCard/student/list.do");
			} catch (final Throwable oops) {
				String errorMessage = "creditCard.commit.error";
				if (oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();
				if (this.creditCardService.tarjetaCaducada(creditCard))
					errorMessage = "creditCard.tarjeta.caducada";
				result = this.createEditModelAndView(creditCard, errorMessage);
			}
		return result;

	}

	// DELETE --------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int creditCardId) {
		ModelAndView result;

		try {
			this.creditCardService.delete(creditCardId);
			result = new ModelAndView("redirect:/creditCard/student/list.do");
		} catch (final Throwable oops) {
			String msg = "creditCard.delete.error";
			result = this.list();
			final Collection<Reservation> reservations = this.reservationService.findAllByCreditCard(creditCardId);
			if (!reservations.isEmpty())
				msg = "creditCard.delete.error.reservation";
			result.addObject("msg", msg);
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final CreditCard creditCard) {
		ModelAndView result;

		result = this.createEditModelAndView(creditCard, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;
		final Collection<String> brandNames = this.configurationParametersService.find().getCreditCardMake();

		result = new ModelAndView("creditCard/edit");

		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("rol", "student");
		result.addObject("brandNames", brandNames);

		return result;
	}
}
