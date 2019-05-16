
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PositionService;
import domain.Position;

@Controller
@RequestMapping("/finder")
public class FinderController extends AbstractController {

	@Autowired
	private PositionService	positionService;


	@RequestMapping(value = "/searching", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;
		final String lang = LocaleContextHolder.getLocale().getLanguage();
		result = new ModelAndView("finder/search");
		result.addObject("lang", lang);
		return result;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam final String keyword) {
		ModelAndView result;
		try {
			final List<Position> positions = this.positionService.search(keyword);
			final String lang = LocaleContextHolder.getLocale().getLanguage();
			result = new ModelAndView("finder/search");
			result.addObject("lang", lang);
			result.addObject("postions", positions);
		} catch (final Throwable e) {
			result = new ModelAndView("finder/search");
			result.addObject("errortrace", "finder.commit.error");
		}
		return result;
	}

}
