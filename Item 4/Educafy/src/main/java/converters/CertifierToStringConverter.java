
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Certifier;

@Component
@Transactional
public class CertifierToStringConverter implements Converter<Certifier, String> {

	@Override
	public String convert(final Certifier certifier) {

		String result;

		if (certifier == null)
			result = null;
		else
			result = String.valueOf(certifier.getId());

		return result;

	}

}
