
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import services.TimePeriodService;
import controllers.teacher.ReservationTeacherController;
import domain.TimePeriod;

@Controller
@RequestMapping("/timePeriod")
public class TimePeriodController extends AbstractController {

	@Autowired
	private TimePeriodService				timePeriodService;

	@Autowired
	private ReservationTeacherController	reservationTeacherController;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int timePeriodId) {
		ModelAndView result;
		TimePeriod timePeriod;

		timePeriod = this.timePeriodService.findOne(timePeriodId);
		if (timePeriod != null) {
			result = this.createEditModelAndView(timePeriod);
			result.addObject("id", timePeriod.getId());
		} else
			result = new ModelAndView("redirect:misc/403");
		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final TimePeriod timePeriod, final BindingResult binding) {
		ModelAndView res = null;

		if (binding.hasErrors())
			res = this.createEditModelAndView(timePeriod);

		else if (timePeriod != null)
			try {
				this.timePeriodService.save(timePeriod);
				res = this.reservationTeacherController.display(timePeriod.getReservation().getId());
			} catch (final Throwable oops) {
				String msg = "tp.commit.error";
				res = this.createEditModelAndView(timePeriod);
				final boolean n = (timePeriod.getStartHour() != null) && (timePeriod.getEndHour() != null);
				if (n && !timePeriod.getReservation().getStatus().equals("PENDING"))
					msg = "tp.no.pending";
				if (n && this.timePeriodService.getTimePeriod(timePeriod))
					msg = "tp.busy";
				if (n && this.timePeriodService.equals(timePeriod, this.timePeriodService.findOne(timePeriod.getId())))
					msg = "tp.no.change";
				if (n && !this.timePeriodService.checkTimePeriodHours(timePeriod))
					msg = "tp.hours.error";
				if (!n)
					msg = "tp.null.error";
				res.addObject("msg", msg);
			}
		return res;
	}
	protected ModelAndView createEditModelAndView(final TimePeriod timePeriod) {

		ModelAndView res;

		res = this.createEditModelAndView(timePeriod, null);

		return res;

	}

	protected ModelAndView createEditModelAndView(final TimePeriod timePeriod, final String messageCode) {

		ModelAndView res;

		res = new ModelAndView("timePeriod/edit");
		res.addObject("timePeriod", timePeriod);

		res.addObject("message", messageCode);
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;

	}

}
