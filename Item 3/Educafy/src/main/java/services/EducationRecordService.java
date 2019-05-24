
package services;

import java.sql.Date;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EducationRecordRepository;
import domain.Certifier;
import domain.Curriculum;
import domain.EducationRecord;
import domain.Teacher;

@Service
@Transactional
public class EducationRecordService {

	@Autowired
	private EducationRecordRepository	educationRecordRepository;

	@Autowired
	private TeacherService				teacherService;

	@Autowired
	private CurriculumService			curriculumService;

	@Autowired
	private CertifierService			certifierService;


	//Metodos CRUD

	public EducationRecord create() {
		final EducationRecord eRecord = new EducationRecord();
		eRecord.setDegree("");
		eRecord.setInstitution("");
		eRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		eRecord.setMark(0);
		eRecord.setIsCertified(false);
		eRecord.setIsDraft(true);
		return eRecord;
	}

	public Collection<EducationRecord> findAll() {
		final Collection<EducationRecord> res = this.educationRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public EducationRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final EducationRecord res = this.educationRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public EducationRecord save(final EducationRecord educationRecord, final int curriculumId) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(educationRecord);
		Assert.isTrue(educationRecord.getIsCertified() == false, "Education record can not be certified");
		Assert.isTrue(educationRecord.getIsDraft() == true, "Education record must be in draft mode");
		if (educationRecord.getEndDate() != null)
			Assert.isTrue(educationRecord.getEndDate().after(educationRecord.getStartDate()), "End date must be after start date");
		if (educationRecord.getId() != 0)
			Assert.isTrue(this.teacherService.hasEducationRecord(me.getId(), educationRecord.getId()), "This personal record is not of your property");
		final EducationRecord res = this.educationRecordRepository.save(educationRecord);

		Assert.notNull(res);

		final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
		if (educationRecord.getId() == 0) {
			final Collection<EducationRecord> misc = curriculum.getEducationRecords();
			misc.add(educationRecord);
			curriculum.setEducationRecords(misc);
			this.curriculumService.save(curriculum);
		}
		return res;
	}

	public void delete(final EducationRecord mR) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.teacherService.findTeacherByEducationRecord(mR.getId()) == me, "No puede borrar un EducationRecord que no pertenezca a su historia.");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		Assert.isTrue(mR.getIsCertified() == false, "Education record can not be certified");
		Assert.isTrue(mR.getIsDraft() == true, "Education record must be in draft mode");
		final EducationRecord res = this.findOne(mR.getId());

		final Curriculum curriculum = this.curriculumService.findCurriculumByEducationRecord(res.getId());

		final Collection<EducationRecord> educationRecords = curriculum.getEducationRecords();

		Assert.isTrue(this.teacherService.hasEducationRecord(me.getId(), res.getId()), "This personal record is not of your property");

		educationRecords.remove(res);

		this.educationRecordRepository.delete(res.getId());

		curriculum.setEducationRecords(educationRecords);
		this.curriculumService.save(curriculum);

	}

	public EducationRecord toFinal(final EducationRecord educationRecord) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me);
		Assert.isTrue(this.teacherService.hasEducationRecord(me.getId(), educationRecord.getId()), "This personal record is not of your property");
		final EducationRecord retrieved = this.findOne(educationRecord.getId());
		Assert.isTrue(retrieved.getAttachment().equals(educationRecord.getAttachment()));
		Assert.isTrue(retrieved.getDegree().equals(educationRecord.getDegree()));
		Assert.isTrue(retrieved.getEndDate().equals(educationRecord.getEndDate()));
		Assert.isTrue(retrieved.getStartDate().equals(educationRecord.getStartDate()));
		Assert.isTrue(retrieved.getInstitution().equals(educationRecord.getInstitution()));
		Assert.isTrue(retrieved.getMark().equals(educationRecord.getMark()));
		Assert.isTrue(retrieved.getIsDraft() == true, "the educatin record is already in final mode");
		educationRecord.setIsDraft(false);
		final EducationRecord res = this.educationRecordRepository.save(educationRecord);
		return res;
	}

	public EducationRecord certify(final EducationRecord educationRecord) {
		final Certifier me = this.certifierService.findByPrincipal();
		Assert.notNull(me);
		final EducationRecord retrieved = this.findOne(educationRecord.getId());
		Assert.isTrue(retrieved.getAttachment().equals(educationRecord.getAttachment()));
		Assert.isTrue(retrieved.getDegree().equals(educationRecord.getDegree()));
		Assert.isTrue(retrieved.getEndDate().equals(educationRecord.getEndDate()));
		Assert.isTrue(retrieved.getStartDate().equals(educationRecord.getStartDate()));
		Assert.isTrue(retrieved.getInstitution().equals(educationRecord.getInstitution()));
		Assert.isTrue(retrieved.getMark().equals(educationRecord.getMark()));
		Assert.isTrue(retrieved.getIsDraft() == false, "the education record is not in final mode");
		educationRecord.setIsCertified(true);
		final EducationRecord res = this.educationRecordRepository.save(educationRecord);
		return res;
	}

	public void flush() {
		this.educationRecordRepository.flush();
	}
}
