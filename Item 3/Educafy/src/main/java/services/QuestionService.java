
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Question;

@Service
@Transactional
public class QuestionService {

	@Autowired
	private QuestionRepository	questionRepository;

	@Autowired
	private TeacherService		teacherService;


	public Question create() {
		final Question question = new Question();
		return question;
	}

	public Question findOne(final int questionId) {
		Assert.isTrue(questionId != 0);
		final Question result = this.questionRepository.findOne(questionId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Question> findAll() {
		Collection<Question> res = new ArrayList<>();
		res = this.questionRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Question save(final Question question) {
		Assert.notNull(question);
		this.teacherService.findByPrincipal();
		final Question result;
		result = this.questionRepository.save(question);
		return result;

	}

	public void delete(final Question question) {
		Assert.notNull(question);
		Assert.isTrue(question.getId() != 0);
		final Question retrieved = this.findOne(question.getId());
		this.teacherService.findByPrincipal();
		this.questionRepository.delete(retrieved);
	}

	/* ========================= OTHER METHODS =========================== */

}
