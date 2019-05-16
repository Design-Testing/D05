
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity {

	private String				title;
	private String				description;
	private Date				deadline;
	private String				ticker;
	private String				profile;
	private Collection<String>	skills;
	private Collection<String>	technologies;
	private Double				salary;
	private String				mode;

	private Company				company;


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
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{4}-[0-9]{4}$")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
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

	@NotBlank
	@Pattern(regexp = "^(DRAFT|FINAL|CANCELLED)$")
	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	@Valid
	@ManyToOne(optional = false)
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Position [title=" + this.title + ", ticker=" + this.ticker + ", salary=" + this.salary + "]";
	}

}
