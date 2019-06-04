
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.CertifierRepository;
import domain.Certifier;

@Component
@Transactional
public class StringToCertifierConverter implements Converter<String, Certifier> {

	@Autowired
	private CertifierRepository	certifierRepository;


	@Override
	public Certifier convert(final String text) {

		final Certifier result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.certifierRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
