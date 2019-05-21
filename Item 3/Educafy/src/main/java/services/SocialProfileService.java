
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	@Autowired
	private ActorService			actorService;


	public SocialProfile create() {
		final SocialProfile sp = new SocialProfile();
		final Actor principal = this.actorService.findByPrincipal();
		sp.setActor(principal);
		return sp;

	}

	public SocialProfile findOne(final Integer sProfileId) {
		Assert.isTrue(sProfileId != 0);
		final SocialProfile res = this.socialProfileRepository.findOne(sProfileId);
		Assert.notNull(res);
		return res;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> res = new ArrayList<>();
		res = this.socialProfileRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		final SocialProfile sProfile = this.socialProfileRepository.save(socialProfile);
		Assert.notNull(sProfile);
		return sProfile;
	}

	public void delete(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		final SocialProfile res = this.findOne(socialProfile.getId());
		this.socialProfileRepository.delete(res);
	}

	public Collection<SocialProfile> findByActor(final Integer actorId) {
		Assert.isTrue(actorId != 0);
		final Collection<SocialProfile> res = this.socialProfileRepository.findByActor(actorId);
		Assert.notNull(res);
		return res;

	}

}
