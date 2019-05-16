
package forms;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotBlank;
import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class PositionForm extends DomainEntity {

	private String				title;
	private String				description;
	private Date				deadline;
	private String				profile;
	private Collection<String>	skills;
	private Collection<String>	technologies;
	private Double				salary;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@NotBlank
	@SafeHtml
	public String getProfile() {
		return this.profile;
	}

	public void setProfile(final String profile) {
		this.profile = profile;
	}

	@EachNotBlank
	@ElementCollection
	public Collection<String> getSkills() {
		return this.skills;
	}

	public void setSkills(final Collection<String> skills) {
		this.skills = skills;
	}

	@EachNotBlank
	@ElementCollection
	public Collection<String> getTechnologies() {
		return this.technologies;
	}

	public void setTechnologies(final Collection<String> technologies) {
		this.technologies = technologies;
	}

	@NotNull
	@Min(0)
	public Double getSalary() {
		return this.salary;
	}

	public void setSalary(final Double salary) {
		this.salary = salary;
	}

}
