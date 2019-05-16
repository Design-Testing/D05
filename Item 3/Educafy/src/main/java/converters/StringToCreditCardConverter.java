
package converters;

import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.CreditCard;

@Component
@Transactional
public class StringToCreditCardConverter implements Converter<String, CreditCard> {

	@Override
	public CreditCard convert(final String text) {

		final CreditCard result;
		String parts[];

		if (text == null)
			result = null;
		else
			try {
				parts = text.split("\\|");
				result = new CreditCard();
				result.setHolderName(URLDecoder.decode(parts[0], "UTF-8"));
				result.setMake(URLDecoder.decode(parts[1], "UTF-8"));
				result.setNumber(URLDecoder.decode(parts[2], "UTF-8"));
				result.setExpirationMonth(Integer.valueOf(URLDecoder.decode(parts[3], "UTF-8")));
				result.setExpirationYear(Integer.valueOf(URLDecoder.decode(parts[4], "UTF-8")));
				result.setCvv(URLDecoder.decode(parts[5], "UTF-8"));
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return result;
	}

}
