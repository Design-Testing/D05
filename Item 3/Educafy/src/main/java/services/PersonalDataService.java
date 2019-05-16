
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PersonalDataRepository;
import domain.PersonalData;
import domain.Rooky;

@Service
@Transactional
public class PersonalDataService {

	@Autowired
	private PersonalDataRepository	personalDataRepository;

	@Autowired
	private RookyService			hackerService;


	//Metodos CRUD

	public PersonalData create() {
		final PersonalData res = new PersonalData();
		res.setFullName("");
		res.setStatement("");
		res.setPhone("+34654987654"); //TODO
		res.setGithub("http://www.github.com");
		res.setLinkedin("http://www.linkedin.com");
		return res;
	}

	public Collection<PersonalData> findAll() {
		final Collection<PersonalData> res = this.personalDataRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PersonalData findOne(final int id) {
		Assert.isTrue(id != 0);
		final PersonalData res = this.personalDataRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public PersonalData save(final PersonalData personalData) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(personalData);
		if (personalData.getId() != 0)
			Assert.isTrue(this.hackerService.findRookyByPersonalData(personalData.getId()) == me);
		final PersonalData saved = this.personalDataRepository.save(personalData);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public PersonalData saveCopy(final PersonalData personalData) {
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
		final PersonalData saved = this.personalDataRepository.save(personalData);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	final PersonalData makeCopyAndSave(final PersonalData p) {
		PersonalData result = this.create();
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
