
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.LessonRepository;
import domain.Lesson;

@Component
@Transactional
public class StringToLessonConverter implements Converter<String, Lesson> {

	@Autowired
	private LessonRepository	lessonRepository;


	@Override
	public Lesson convert(final String text) {

		final Lesson result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.lessonRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
