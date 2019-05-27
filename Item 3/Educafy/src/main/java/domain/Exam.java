
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class Exam extends DomainEntity {

	private String					title;
	private Double					score;
	private Collection<Question>	questions;
	private String					status;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Range(min = 0, max = 10)
	public Double getScore() {
		return this.score;
	}

	public void setScore(final Double score) {
		this.score = score;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(final Collection<Question> questions) {
		this.questions = questions;
	}

	@NotBlank
	@Pattern(regexp = "^(PENDING|INPROGRESS|SUBMITTED|EVALUATED)$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

}
