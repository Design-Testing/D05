
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.AssesmentRepository;
import domain.Assesment;

@Component
@Transactional
public class StringToAssesmentConverter implements Converter<String, Assesment> {

	@Autowired
	private AssesmentRepository	assesmentRepository;


	@Override
	public Assesment convert(final String text) {

		final Assesment result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.assesmentRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
