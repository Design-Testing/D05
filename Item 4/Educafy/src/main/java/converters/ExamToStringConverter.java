
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Exam;

@Component
@Transactional
public class ExamToStringConverter implements Converter<Exam, String> {

	@Override
	public String convert(final Exam exam) {

		String result;

		if (exam == null)
			result = null;
		else
			result = String.valueOf(exam.getId());

		return result;

	}

}
