
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard comment) {

		String result;

		if (comment == null)
			result = null;
		else
			result = String.valueOf(comment.getId());

		return result;

	}

}
