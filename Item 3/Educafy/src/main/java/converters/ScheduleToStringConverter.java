
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Schedule;

@Component
@Transactional
public class ScheduleToStringConverter implements Converter<Schedule, String> {

	@Override
	public String convert(final Schedule schedule) {

		String result;

		if (schedule == null)
			result = null;
		else
			result = String.valueOf(schedule.getId());

		return result;

	}

}
