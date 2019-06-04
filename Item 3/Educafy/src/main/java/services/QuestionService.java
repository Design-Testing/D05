
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import security.Authority;
import domain.Actor;
import domain.Exam;
import domain.Question;
import domain.Teacher;

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
		final List<Question> questions = (List<Question>) exam.getQuestions();
		Question result = null;

		if (question.getId() == 0) {
			if (isTeacher) {
				Assert.isTrue(this.teacherService.findTeacherByReservation(exam.getReservation().getId()).equals(ppal), "No puede crear una pregunta en un examen de una reserva que no es suya.");
				result = this.questionRepository.save(question);
				questions.add(result);
			}
		} else {
			Assert.isTrue(exam.getQuestions().contains(question), "La pregunta que quiere actualizar no se corresponde con el examen indicado.");
			if (isStudent) {
				Assert.isTrue(exam.getReservation().getStudent().equals(ppal), "No puede responder una pregunta que no pertenece a un examen de sus reservas.");
				result = this.questionRepository.save(question);
				questions.set(questions.indexOf(question), result);
			} else {
				Assert.isTrue(this.teacherService.findTeacherByReservation(exam.getReservation().getId()).equals(ppal), "No puede actualizar una pregunta que no pertenece a un examen de sus reservas.");
				result = this.questionRepository.save(question);
				questions.set(questions.indexOf(question), result);
			}
		}
		exam.setQuestions(questions);
		this.examService.save(exam, exam.getReservation().getId());
		return result;
	}
	public void delete(final Question question, final Exam exam) {
		Assert.notNull(question);
		Assert.isTrue(question.getId() != 0);
		final Teacher principal = this.teacherService.findByPrincipal();
		Assert.isTrue(this.teacherService.findTeacherByReservation(exam.getReservation().getId()).equals(principal));
		final Collection<Question> questions = exam.getQuestions();
		questions.remove(question);
		exam.setQuestions(questions);
		this.examService.save(exam, exam.getReservation().getId());
		this.questionRepository.delete(question);
	}
	/* ========================= OTHER METHODS =========================== */

	public void deleteInBatch(final Collection<Question> questions) {
		this.questionRepository.deleteInBatch(questions);
	}

	public void flush() {
		this.questionRepository.flush();
	}
}
