
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "student"), @Index(columnList = "lesson")
})
public class Assesment extends DomainEntity {

	private int		score;
	private String	comment;

	private Lesson	lesson;
	private Student	student;


	@Range(min = 0, max = 5)
	public int getScore() {
		return this.score;
	}

	public void setScore(final int score) {
		this.score = score;
	}

	@NotBlank
	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@Valid
	@ManyToOne(optional = false)
	public Lesson getLesson() {
		return this.lesson;
	}

	public void setLesson(final Lesson lesson) {
		this.lesson = lesson;
	}

	@Valid
	@ManyToOne(optional = false)
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(final Student student) {
		this.student = student;
	}

}
