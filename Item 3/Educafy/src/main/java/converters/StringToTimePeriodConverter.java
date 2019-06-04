
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.TimePeriodRepository;
import domain.TimePeriod;

@Component
@Transactional
public class StringToTimePeriodConverter implements Converter<String, TimePeriod> {

	@Autowired
	private TimePeriodRepository	timePeriodRepository;


	@Override
	public TimePeriod convert(final String text) {

		final TimePeriod result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.timePeriodRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
