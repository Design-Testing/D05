
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.TimePeriod;

@Component
@Transactional
public class TimePeriodToStringConverter implements Converter<TimePeriod, String> {

	@Override
	public String convert(final TimePeriod timePeriod) {

		String result;

		if (timePeriod == null)
			result = null;
		else
			result = String.valueOf(timePeriod.getId());

		return result;

	}

}
