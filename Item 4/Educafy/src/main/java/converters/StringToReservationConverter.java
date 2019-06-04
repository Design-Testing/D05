
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ReservationRepository;
import domain.Reservation;

@Component
@Transactional
public class StringToReservationConverter implements Converter<String, Reservation> {

	@Autowired
	private ReservationRepository	reservationRepository;


	@Override
	public Reservation convert(final String text) {

		final Reservation result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.reservationRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
