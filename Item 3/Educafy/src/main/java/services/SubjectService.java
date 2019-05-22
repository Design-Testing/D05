
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SubjectRepository;
import domain.Subject;

@Service
@Transactional
public class SubjectService {

	@Autowired
	private SubjectRepository	subjectRepository;


	public Subject create() {
		return new Subject();
	}

	public Subject findOne(final int subjectId) {
		Assert.isTrue(subjectId != 0);
		final Subject subject = this.subjectRepository.findOne(subjectId);
		Assert.notNull(subject);
		return subject;
	}

	public Collection<Subject> findAll() {
		Collection<Subject> subjects = new ArrayList<>();
		subjects = this.subjectRepository.findAll();
		Assert.notNull(subjects);
		return subjects;
	}

	public Subject save(final Subject subject) {
		Assert.notNull(subject);
		final Subject result;
		result = this.subjectRepository.save(subject);
		return result;
	}

	public void delete(final Subject subject) {
		Assert.notNull(subject);
		Assert.isTrue(subject.getId() != 0);
		final Subject result = this.findOne(subject.getId());
		this.subjectRepository.delete(result);
	}

}
