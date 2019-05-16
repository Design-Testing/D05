
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.AdministratorRepository;
import repositories.AuditorRepository;
import repositories.CompanyRepository;
import repositories.ProviderRepository;
import repositories.RookyRepository;
import domain.Actor;

@Component
@Transactional
public class StringToActorConverter implements Converter<String, Actor> {

	@Autowired
	private CompanyRepository		companyRepository;

	@Autowired
	private RookyRepository			rookyRepository;

	@Autowired
	private AdministratorRepository	administratorRepository;

	@Autowired
	private ProviderRepository		providerRepository;

	@Autowired
	private AuditorRepository		auditorRepository;


	@Override
	public Actor convert(final String text) {

		Actor result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.companyRepository.findOne(id);
				if (result == null)
					result = this.rookyRepository.findOne(id);
				if (result == null)
					result = this.administratorRepository.findOne(id);
				if (result == null)
					result = this.providerRepository.findOne(id);
				if (result == null)
					result = this.auditorRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
