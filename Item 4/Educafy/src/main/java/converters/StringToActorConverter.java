
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.AdministratorRepository;
import repositories.CertifierRepository;
import repositories.StudentRepository;
import repositories.TeacherRepository;
import domain.Actor;

@Component
@Transactional
public class StringToActorConverter implements Converter<String, Actor> {

	@Autowired
	private AdministratorRepository	administratorRepository;

	@Autowired
	private CertifierRepository		certifierRepository;

	@Autowired
	private TeacherRepository		teacherRepository;

	@Autowired
	private StudentRepository		studentRepository;


	@Override
	public Actor convert(final String text) {

		Actor result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.studentRepository.findOne(id);
				if (result == null)
					result = this.teacherRepository.findOne(id);
				if (result == null)
					result = this.administratorRepository.findOne(id);
				if (result == null)
					result = this.certifierRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
