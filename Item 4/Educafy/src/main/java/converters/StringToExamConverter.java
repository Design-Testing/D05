
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ExamRepository;
import domain.Exam;

@Component
@Transactional
public class StringToExamConverter implements Converter<String, Exam> {

	@Autowired
	private ExamRepository	examRepository;


	@Override
	public Exam convert(final String text) {

		final Exam result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.examRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
