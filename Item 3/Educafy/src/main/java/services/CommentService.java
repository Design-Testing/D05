
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Assesment;
import domain.Comment;
import domain.Teacher;
import forms.CommentForm;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository	commentRepository;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private AssesmentService	assesmentService;

	@Autowired
	private Validator			validator;


	public Comment create() {
		final Comment comment = new Comment();
		return comment;
	}

	public Comment findOne(final int commentId) {
		Assert.isTrue(commentId != 0);
		final Comment result = this.commentRepository.findOne(commentId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> res = new ArrayList<>();
		res = this.commentRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Comment save(final Comment comment, final int assesmentId) {
		Assert.notNull(comment);
		final Teacher principal = this.teacherService.findByPrincipal();
		final Comment result;
		Assesment assesment;
		assesment = this.assesmentService.findOne(assesmentId);

		if (comment.getId() == 0) {
			Assert.isTrue(assesment.getLesson().getTeacher().equals(principal), "No puede realizar un comentario a una evaluación que no le pertenece.");
			comment.setAssesment(assesment);
			final Date moment = new Date(System.currentTimeMillis() - 1);
			comment.setMoment(moment);
		}
		result = this.commentRepository.save(comment);
		return result;

	}

	public void delete(final Comment comment) {
		Assert.notNull(comment);
		Assert.isTrue(comment.getId() != 0);
		final Comment retrieved = this.findOne(comment.getId());
		final Teacher principal = this.teacherService.findByPrincipal();
		Assert.isTrue(retrieved.getAssesment().getLesson().getTeacher().equals(principal));
		this.commentRepository.delete(retrieved);
	}

	/* ========================= OTHER METHODS =========================== */

	public Comment reconstruct(final CommentForm commentForm, final BindingResult binding) {
		Comment result;

		if (commentForm.getId() == 0)
			result = this.create();
		else
			result = this.findOne(commentForm.getId());

		result.setVersion(commentForm.getVersion());
		result.setText(commentForm.getText());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Collection<Comment> findAllCommentsByTeacherPpal() {
		Collection<Comment> res;
		final Teacher teacher = this.teacherService.findByPrincipal();
		res = this.commentRepository.findAllCommentsByTeacherPpal(teacher.getUserAccount().getId());
		return res;
	}

	public Collection<Comment> findAllCommentsByAssesment(final int assesmentId) {
		Collection<Comment> res;
		res = this.commentRepository.findAllCommentsByAssesment(assesmentId);
		return res;
	}

	public void deleteInBatch(final Collection<Comment> comments) {
		this.commentRepository.deleteInBatch(comments);
	}
}
