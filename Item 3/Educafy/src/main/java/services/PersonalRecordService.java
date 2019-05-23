
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PersonalRecordRepository;
import domain.PersonalRecord;
import domain.Teacher;

@Service
@Transactional
public class PersonalRecordService {

	@Autowired
	private PersonalRecordRepository	personalRecordRepository;

	@Autowired
	private TeacherService				teacherService;


	//Metodos CRUD

	public PersonalRecord create() {
		final PersonalRecord res = new PersonalRecord();
		res.setFullName("");
		res.setStatement("");
		res.setGithub("http://www.github.com");
		res.setLinkedin("http://www.linkedin.com");
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
		if (personalRecord.getId() != 0)
			Assert.isTrue(this.teacherService.findTeacherByPersonalRecord(personalRecord.getId()) == me);
		final PersonalRecord saved = this.personalRecordRepository.save(personalRecord);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public PersonalRecord saveCopy(final PersonalRecord personalRecord) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(personalRecord);
		Assert.notNull(personalRecord.getFullName());
		Assert.notNull(personalRecord.getStatement());
		Assert.notNull(personalRecord.getGithub());
		Assert.notNull(personalRecord.getLinkedin());
		if (personalRecord.getId() != 0)
			Assert.isTrue(this.teacherService.findTeacherByPersonalRecord(personalRecord.getId()) == me);
		final PersonalRecord saved = this.personalRecordRepository.save(personalRecord);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	final PersonalRecord makeCopyAndSave(final PersonalRecord p) {
		PersonalRecord result = this.create();
		result.setFullName(p.getFullName());
		result.setStatement(p.getStatement());
		result.setGithub(p.getGithub());
		result.setLinkedin(p.getLinkedin());
		Assert.notNull(result, "copy of personal record is null");
		result = this.save(result);
		Assert.notNull(result, "retrieved personal record is null");
		return result;
	}

	public void flush() {
		this.personalRecordRepository.flush();
	}
}
