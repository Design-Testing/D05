
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Rooky;

@Component
@Transactional
public class RookyToStringConverter implements Converter<Rooky, String> {

	@Override
	public String convert(final Rooky rooky) {

		String result;

		if (rooky == null)
			result = null;
		else
			result = String.valueOf(rooky.getId());

		return result;

	}

}
