
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PersonalRecordRepository;
import domain.Certifier;
import domain.PersonalRecord;
import domain.Teacher;

@Service
@Transactional
public class PersonalRecordService {

	@Autowired
	private PersonalRecordRepository	personalRecordRepository;

	@Autowired
	private TeacherService				teacherService;

	@Autowired
	private CertifierService			certifierService;


	//Metodos CRUD

	public PersonalRecord create() {
		final PersonalRecord res = new PersonalRecord();
		res.setFullName("");
		res.setStatement("");
		res.setGithub("http://www.github.com");
		res.setLinkedin("http://www.linkedin.com");
		res.setPhoto("http://testPhoto.com");
		res.setIsCertified(false);
		res.setIsDraft(true);
		return res;
	}

	public Collection<PersonalRecord> findAll() {
		final Collection<PersonalRecord> res = this.personalRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PersonalRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final PersonalRecord res = this.personalRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public PersonalRecord save(final PersonalRecord personalRecord) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(personalRecord);
		Assert.isTrue(personalRecord.getIsCertified() == false, "Personal record can not be certified");
		Assert.isTrue(personalRecord.getIsDraft() == true, "Perosnal record must be in draft mode");
		if (personalRecord.getId() != 0)
			Assert.isTrue(this.teacherService.findTeacherByPersonalRecord(personalRecord.getId()) == me);
		final PersonalRecord saved = this.personalRecordRepository.save(personalRecord);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public PersonalRecord toFinal(final PersonalRecord personalRecord) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me);
		Assert.isTrue(this.teacherService.findTeacherByPersonalRecord(personalRecord.getId()) == me);
		final PersonalRecord retrieved = this.findOne(personalRecord.getId());
		Assert.isTrue(retrieved.getIsDraft() == true, "the personal record is already in final mode");
		personalRecord.setIsDraft(false);
		final PersonalRecord res = this.personalRecordRepository.save(personalRecord);
		return res;
	}

	public PersonalRecord certify(final PersonalRecord personalRecord) {
		final Certifier me = this.certifierService.findByPrincipal();
		Assert.notNull(me);
		final PersonalRecord retrieved = this.findOne(personalRecord.getId());
		Assert.isTrue(retrieved.getIsDraft() == false, "the personal record is not in final mode");
		personalRecord.setIsCertified(true);
		final PersonalRecord res = this.personalRecordRepository.save(personalRecord);
		return res;
	}

	public void flush() {
		this.personalRecordRepository.flush();
	}
}
