
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.validator.constraints.SafeHtml;

import services.Lesson;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	private String				keyword;
	private String				teacherName;
	private String				subjectName;
	private String				subjectLevel;

	private Collection<Lesson>	lessons;


	@SafeHtml
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	@SafeHtml
	public String getTeacherName() {
		return this.teacherName;
	}

	public void setTeacherName(final String teacherName) {
		this.teacherName = teacherName;
	}

	@SafeHtml
	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(final String subjectName) {
		this.subjectName = subjectName;
	}

	@SafeHtml
	public String getSubjectLevel() {
		return this.subjectLevel;
	}

	public void setSubjectLevel(final String subjectLevel) {
		this.subjectLevel = subjectLevel;
	}

	@ManyToMany
	public Collection<Lesson> getLessons() {
		return this.lessons;
	}

	public void setLessons(final Collection<Lesson> lessons) {
		this.lessons = lessons;
	}

}
