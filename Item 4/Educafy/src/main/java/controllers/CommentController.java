
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
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService		commentService;

	@Autowired
	private AssesmentService	assesmentService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int assesmentId) {
		final ModelAndView result;
		final Collection<Comment> comments;
		Assesment assesment;

		comments = this.commentService.findAllCommentsByAssesment(assesmentId);
		assesment = this.assesmentService.findOne(assesmentId);

		if (assesment != null) {
			result = new ModelAndView("comment/list");
			result.addObject("comments", comments);
			result.addObject("assesment", assesment);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		result.addObject("lang", this.lang);

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId) {
		final ModelAndView result;
		final Comment comment;

		comment = this.commentService.findOne(commentId);

		result = new ModelAndView("comment/display");
		result.addObject("comment", comment);
		result.addObject("lang", this.lang);

		return result;
	}

}
