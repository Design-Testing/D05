
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Certifier;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.Teacher;

@Service
@Transactional
public class MiscellaneousRecordService {

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	@Autowired
	private TeacherService					teacherService;

	@Autowired
	private CurriculumService				curriculumService;

	@Autowired
	private CertifierService				certifierService;


	//Metodos CRUD

	public MiscellaneousRecord create() {
		final MiscellaneousRecord mRecord = new MiscellaneousRecord();
		mRecord.setFreeText("");
		final Collection<String> attachments = new ArrayList<String>();
		mRecord.setAttachments(attachments);
		mRecord.setIsCertified(false);
		mRecord.setIsDraft(true);
		return mRecord;
	}

	public Collection<MiscellaneousRecord> findAll() {
		final Collection<MiscellaneousRecord> res = this.miscellaneousRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public MiscellaneousRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord, final int curriculumId) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(miscellaneousRecord);
		Assert.isTrue(miscellaneousRecord.getIsCertified() == false, "Miscellanous record can not be certified");
		Assert.isTrue(miscellaneousRecord.getIsDraft() == true, "Miscellanous record must be in draft mode");
		if (miscellaneousRecord.getId() != 0)
			Assert.isTrue(this.teacherService.hasMiscellaneousRecord(me.getId(), miscellaneousRecord.getId()), "This personal record is not of your property");

		final MiscellaneousRecord res = this.miscellaneousRecordRepository.save(miscellaneousRecord);

		Assert.notNull(res);

		final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
		if (miscellaneousRecord.getId() == 0) {
			final Collection<MiscellaneousRecord> misc = curriculum.getMiscellaneousRecords();
			misc.add(miscellaneousRecord);
			curriculum.setMiscellaneousRecords(misc);
			this.curriculumService.save(curriculum);
		}
		return res;
	}

	public void delete(final MiscellaneousRecord mR) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.teacherService.findTeacherByMiscellaneousRecord(mR.getId()) == me, "No puede borrar un MiscellaneousRecord que no pertenezca a su historia.");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final MiscellaneousRecord res = this.findOne(mR.getId());

		Assert.isTrue(mR.getIsCertified() == false, "Education record can not be certified");
		Assert.isTrue(mR.getIsDraft() == true, "Education record must be in draft mode");

		final Curriculum curriculum = this.curriculumService.findCurriculumByMiscellaneousRecord(res.getId());

		final Collection<MiscellaneousRecord> miscellaneousRecords = curriculum.getMiscellaneousRecords();

		Assert.isTrue(this.teacherService.hasMiscellaneousRecord(me.getId(), res.getId()), "This personal record is not of your property");

		miscellaneousRecords.remove(res);

		this.miscellaneousRecordRepository.delete(res.getId());

		curriculum.setMiscellaneousRecords(miscellaneousRecords);
		this.curriculumService.save(curriculum);

	}

	public MiscellaneousRecord toFinal(final MiscellaneousRecord miscellaneousRecord) {
		final Teacher me = this.teacherService.findByPrincipal();
		Assert.notNull(me);
		Assert.isTrue(this.teacherService.findTeacherByMiscellaneousRecord(miscellaneousRecord.getId()) == me, "No puede borrar un MiscellaneousRecord que no pertenezca a su historia.");
		final MiscellaneousRecord retrieved = this.findOne(miscellaneousRecord.getId());
		Assert.isTrue(retrieved.getIsDraft() == true, "the miscellaneous record is already in final mode");
		miscellaneousRecord.setIsDraft(false);
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		return res;
	}

	public MiscellaneousRecord certify(final MiscellaneousRecord miscellaneousRecord) {
		final Certifier me = this.certifierService.findByPrincipal();
		Assert.notNull(me);
		final MiscellaneousRecord retrieved = this.findOne(miscellaneousRecord.getId());
		Assert.isTrue(retrieved.getIsDraft() == false, "the miscellaneous record is not in final mode");
		miscellaneousRecord.setIsCertified(true);
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		return res;
	}

	public void flush() {
		this.miscellaneousRecordRepository.flush();

	}
}
