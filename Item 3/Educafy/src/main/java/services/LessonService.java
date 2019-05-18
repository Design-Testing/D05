
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LessonRepository;

@Service
@Transactional
public class LessonService {

	@Autowired
	LessonRepository	lessonRepository;


	public Collection<Lesson> findPositions(final String keyword, final Double minSalary, final Double maxSalary, final Date minDeadline, final Date maxDeadline) {
		final Collection<Lesson> res = this.lessonRepository.findLessons(keyword, minSalary, maxSalary, minDeadline, maxDeadline);
		Assert.notNull(res);
		return res;
	}
}
