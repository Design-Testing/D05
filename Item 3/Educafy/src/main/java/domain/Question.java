
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Question extends DomainEntity {

	private String	title;
	private String	answer;

	private Exam	exam;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	@ManyToOne(optional = false)
	public Exam getExam() {
		return this.exam;
	}

	public void setExam(final Exam exam) {
		this.exam = exam;
	}

}
