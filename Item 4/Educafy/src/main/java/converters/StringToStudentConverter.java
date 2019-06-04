
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.StudentRepository;
import domain.Student;

@Component
@Transactional
public class StringToStudentConverter implements Converter<String, Student> {

	@Autowired
	private StudentRepository	studentRepository;


	@Override
	public Student convert(final String text) {

		final Student result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.studentRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
