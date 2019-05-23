
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import domain.Curriculum;
import domain.EducationRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.Teacher;

@Service
@Transactional
public class CurriculumService {

	@Autowired
	private CurriculumRepository		curriculumRepository;

	@Autowired
	private TeacherService				teacherService;

	@Autowired
	private PersonalRecordService		personalRecordService;

	@Autowired
	private EducationRecordService		educationRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	public Curriculum create() {
		final Curriculum curriculum = new Curriculum();
		final Teacher teacher = this.teacherService.findByPrincipal();

		final PersonalRecord personalRecord = this.personalRecordService.create();

		personalRecord.setFullName(teacher.getName());
		personalRecord.setStatement("Statement " + teacher.getName());

		curriculum.setPersonalRecord(personalRecord);

		final Collection<MiscellaneousRecord> miscellaneousRecord = new ArrayList<MiscellaneousRecord>();
		curriculum.setMiscellaneousRecords(miscellaneousRecord);

		final Collection<EducationRecord> educationRecord = new ArrayList<EducationRecord>();
		curriculum.setEducationRecords(educationRecord);

		return curriculum;

	}

	public Curriculum createForNewTeacher() {
		final Curriculum curriculum = new Curriculum();

		final PersonalRecord personalRecord = this.personalRecordService.create();

		personalRecord.setFullName("full name");
		personalRecord.setStatement("statement");

		curriculum.setPersonalRecord(personalRecord);

		final Collection<MiscellaneousRecord> miscellaneousRecord = new ArrayList<MiscellaneousRecord>();
		curriculum.setMiscellaneousRecords(miscellaneousRecord);

		final Collection<EducationRecord> educationRecord = new ArrayList<EducationRecord>();
		curriculum.setEducationRecords(educationRecord);

		return curriculum;

	}

	public Collection<Curriculum> findAll() {
		Collection<Curriculum> res = new ArrayList<>();
		res = this.curriculumRepository.findAll();
		Assert.notNull(res, "Find all returns a null collection");
		return res;
	}

	public Curriculum findOne(final int curriculumId) {
		Assert.isTrue(curriculumId != 0);
		final Curriculum res = this.curriculumRepository.findOne(curriculumId);
		Assert.notNull(res);
		return res;
	}

	public Curriculum save(final Curriculum curriculum) {
		Assert.notNull(curriculum);
		final Curriculum res;
		final Teacher teacher = this.teacherService.findByPrincipal();
		if (curriculum.getId() != 0)
			Assert.isTrue(this.teacherService.findTeacherByCurriculum(curriculum.getId()).equals(teacher), "logged actor doesnt match curriculum's owner");
		else
			curriculum.setTeacher(teacher);
		res = this.curriculumRepository.save(curriculum);
		return res;
	}

	public void delete(final Curriculum curriculum) {
		Assert.notNull(curriculum);
		Assert.isTrue(curriculum.getId() != 0);
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Curriculum retrieved = this.findOne(curriculum.getId());
		Assert.isTrue(this.teacherService.findTeacherByCurriculum(retrieved.getId()) == teacher, "Not possible to delete the curriculum of other teacher.");
		this.curriculumRepository.delete(retrieved.getId());
	}

	public Collection<Curriculum> findCurriculumByTeacher(final int id) {
		final Collection<Curriculum> result = this.curriculumRepository.findCurriculumByTeacher(id);
		return result;
	}

	public Curriculum findCurriculumByPersonalRecord(final int id) {
		final Curriculum result = this.curriculumRepository.findCurriculumByPersonalRecord(id);
		Assert.notNull(result, "findCurriculumByPersonalRecord returns null");
		return result;
	}

	public Curriculum findCurriculumByEducationRecord(final int id) {
		final Curriculum result = this.curriculumRepository.findCurriculumByEducationRecord(id);
		Assert.notNull(result, "findCurriculumByEducationRecord returns null");
		return result;
	}

	public Curriculum findCurriculumByMiscellaneousRecord(final int id) {
		final Curriculum result = this.curriculumRepository.findCurriculumByMiscellaneousRecord(id);
		Assert.notNull(result, "findCurriculumByMiscellanousRecord returns null");
		return result;
	}

	public void flush() {
		this.curriculumRepository.flush();
	}

}
