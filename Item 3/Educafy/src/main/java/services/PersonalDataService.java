
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PersonalDataRepository;
import domain.PersonalRecord;
import domain.Rooky;

@Service
@Transactional
public class PersonalDataService {

	@Autowired
	private PersonalDataRepository	personalDataRepository;

	@Autowired
	private RookyService			hackerService;


	//Metodos CRUD

	public PersonalRecord create() {
		final PersonalRecord res = new PersonalRecord();
		res.setFullName("");
		res.setStatement("");
		res.setPhone("+34654987654"); //TODO
		res.setGithub("http://www.github.com");
		res.setLinkedin("http://www.linkedin.com");
		return res;
	}

	public Collection<PersonalRecord> findAll() {
		final Collection<PersonalRecord> res = this.personalDataRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PersonalRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final PersonalRecord res = this.personalDataRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public PersonalRecord save(final PersonalRecord personalData) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(personalData);
		if (personalData.getId() != 0)
			Assert.isTrue(this.hackerService.findRookyByPersonalData(personalData.getId()) == me);
		final PersonalRecord saved = this.personalDataRepository.save(personalData);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public PersonalRecord saveCopy(final PersonalRecord personalData) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(personalData);
		Assert.notNull(personalData.getFullName());
		Assert.notNull(personalData.getStatement());
		Assert.notNull(personalData.getGithub());
		Assert.notNull(personalData.getLinkedin());
		Assert.notNull(personalData.getPhone());
		if (personalData.getId() != 0)
			Assert.isTrue(this.hackerService.findRookyByPersonalData(personalData.getId()) == me);
		final PersonalRecord saved = this.personalDataRepository.save(personalData);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	final PersonalRecord makeCopyAndSave(final PersonalRecord p) {
		PersonalRecord result = this.create();
		result.setFullName(p.getFullName());
		result.setStatement(p.getStatement());
		result.setPhone(p.getPhone());
		result.setGithub(p.getGithub());
		result.setLinkedin(p.getLinkedin());
		Assert.notNull(result, "copy of personal data is null");
		result = this.save(result);
		Assert.notNull(result, "retrieved personal data is null");
		return result;
	}

	public void flush() {
		this.personalDataRepository.flush();
	}

}
