
package controllers.teacher;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AssesmentService;
import services.CommentService;
import services.TeacherService;
import controllers.AbstractController;
import domain.Assesment;
import domain.Comment;
import domain.Teacher;
import forms.CommentForm;

@Controller
@RequestMapping("/comment/teacher")
public class CommentTeacherController extends AbstractController {

	@Autowired
	private CommentService		commentService;

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private TeacherService		teacherService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE  ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int assesmentId) {
		ModelAndView result;
		final Comment comment = this.commentService.create();
		result = this.createEditModelAndView(comment, assesmentId);
		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/myComments", method = RequestMethod.GET)
	public ModelAndView myComments(@RequestParam final int assesmentId) {
		final ModelAndView result;
		final Collection<Comment> comments;
		Assesment assesment;

		comments = this.commentService.findAllCommentsByTeacherPpal();
		assesment = this.assesmentService.findOne(assesmentId);

		if (assesment != null) {
			result = new ModelAndView("comment/list");
			result.addObject("comments", comments);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		result.addObject("lang", this.lang);
		result.addObject("rol", "teacher");
		result.addObject("requetURI", "comment/teacher/myComments.do");
		result.addObject("principalID", this.teacherService.findByPrincipal().getId());

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commentId) {
		final ModelAndView result;
		final Comment comment;
		final Teacher teacher;

		comment = this.commentService.findOne(commentId);
		teacher = this.teacherService.findByPrincipal();

		result = new ModelAndView("comment/display");
		result.addObject("comment", comment);
		result.addObject("teacher", teacher);
		result.addObject("teacherId", teacher.getId());
		result.addObject("rol", "teacher");
		result.addObject("lang", this.lang);

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int commentId, @RequestParam final int assesmentId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOne(commentId);

		final Teacher teacher = this.teacherService.findByPrincipal();

		if (comment.getAssesment().getLesson().getTeacher().equals(teacher))
			result = this.createEditModelAndView(comment, assesmentId);
		else
			result = new ModelAndView("redirect:misc/403");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final CommentForm commentForm, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String paramAssesmentId;
		Integer assesmentId;

		paramAssesmentId = request.getParameter("lessonId");
		assesmentId = paramAssesmentId.isEmpty() ? null : Integer.parseInt(paramAssesmentId);

		final Comment comment = this.commentService.reconstruct(commentForm, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(comment, assesmentId);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				this.commentService.save(comment, assesmentId);
				result = this.myComments(assesmentId);
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(comment, assesmentId, "commit.assesment.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(comment, assesmentId, "commit.assesment.error");
				result.addObject("errors", binding.getAllErrors());
			}

		return result;
	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Comment comment, final int assesmentId) {
		ModelAndView result;
		result = this.createEditModelAndView(comment, assesmentId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final int assesmentId, final String messageCode) {
		Assert.notNull(comment);
		final ModelAndView result;

		result = new ModelAndView("comment/edit");
		result.addObject("commentForm", this.constructPruned(comment));
		result.addObject("assesmentId", assesmentId);
		result.addObject("message", messageCode);

		return result;
	}

	public CommentForm constructPruned(final Comment comment) {
		final CommentForm pruned = new CommentForm();

		pruned.setId(comment.getId());
		pruned.setVersion(comment.getVersion());
		pruned.setText(comment.getText());

		return pruned;
	}

}
