
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AssesmentService;
import services.CommentService;
import domain.Assesment;
import domain.Comment;

@Controller
@RequestMapping("/assesment")
public class AssesmentController extends AbstractController {

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private CommentService		commentService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int assesmentId) {
		final ModelAndView result;
		final Assesment assesment;
		Collection<Comment> comments;

		assesment = this.assesmentService.findOne(assesmentId);
		comments = this.commentService.findAllCommentsByAssesment(assesmentId);

		if (assesment != null) {
			result = new ModelAndView("assesment/display");
			result.addObject("assesment", assesment);
			result.addObject("comments", comments);
			result.addObject("lang", this.lang);
		} else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}
	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Assesment> assesments;

		assesments = this.assesmentService.findAll();

		result = new ModelAndView("assesment/list");
		result.addObject("assesments", assesments);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "assesment/list.do");

		return result;
	}
}
