
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import security.Authority;
import domain.Actor;
import domain.Exam;
import domain.Question;

@Service
@Transactional
public class QuestionService {

	@Autowired
	private QuestionRepository	questionRepository;

	@Autowired
	private ExamService			examService;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private ActorService		actorService;


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

	public Question save(final Question question, final int examId) {
		Assert.notNull(question);
		Assert.isTrue(examId != 0);

		final Actor ppal = this.actorService.findByPrincipal();
		final Boolean isStudent = this.actorService.checkAuthority(ppal, Authority.STUDENT);
		final Boolean isTeacher = this.actorService.checkAuthority(ppal, Authority.TEACHER);

		final Exam exam = this.examService.findOne(examId);
		final Question result;

		if (question.getId() == 0) {
			if (isTeacher)
				Assert.isTrue(this.teacherService.findTeacherByReservation(exam.getReservation().getId()).equals(ppal), "No puede crear una pregunta en un examen de una reserva que no es suya.");
		} else {
			Assert.isTrue(question.getExam().equals(exam), "La pregunta que quiere actualizar no se corresponde con el examen indicado.");
			if (isStudent)
				Assert.isTrue(exam.getReservation().getStudent().equals(ppal), "No puede responder una pregunta que no pertenece a un examen de sus reservas.");
			else
				Assert.isTrue(this.teacherService.findTeacherByReservation(exam.getReservation().getId()).equals(ppal), "No puede actualizar una pregunta que no pertenece a un examen de sus reservas.");
		}
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

	public Collection<Question> findQuestionsByExam(final int examId) {
		Collection<Question> res;
		res = this.questionRepository.findQuestionsByExam(examId);
		return res;
	}

}
