
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Assesment;

@Component
@Transactional
public class AssesmentToStringConverter implements Converter<Assesment, String> {

	@Override
	public String convert(final Assesment assesment) {

		String result;

		if (assesment == null)
			result = null;
		else
			result = String.valueOf(assesment.getId());

		return result;

	}

}
