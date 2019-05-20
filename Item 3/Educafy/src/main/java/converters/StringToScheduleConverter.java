
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ScheduleRepository;
import domain.Schedule;

@Component
@Transactional
public class StringToScheduleConverter implements Converter<String, Schedule> {

	@Autowired
	private ScheduleRepository	scheduleRepository;


	@Override
	public Schedule convert(final String text) {

		final Schedule result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.scheduleRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
