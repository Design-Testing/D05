
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CertifierRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Certifier;

@Service
@Transactional
public class CertifierService {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CertifierRepository	certifierRepository;


	public Certifier findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Certifier certifier = this.findByUserId(user.getId());
		Assert.notNull(certifier);
		final boolean bool = this.actorService.checkAuthority(certifier, Authority.CERTIFIER);
		Assert.isTrue(bool);

		return certifier;
	}

	public Certifier findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Certifier certifier = this.certifierRepository.findByUserId(id);
		return certifier;
	}

}
