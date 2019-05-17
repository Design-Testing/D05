
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EducationDataRepository;
import domain.Curriculum;
import domain.EducationRecord;
import domain.Rooky;

@Service
@Transactional
public class EducationDataService {

	@Autowired
	private EducationDataRepository	educationDataRepository;

	@Autowired
	private RookyService			hackerService;

	@Autowired
	private CurriculaService		curriculaService;


	//Metodos CRUD

	public EducationRecord create() {
		final EducationRecord eData = new EducationRecord();
		eData.setDegree("");
		eData.setInstitution("");
		eData.setStartDate(new Date(System.currentTimeMillis() - 1));
		eData.setMark(0);
		return eData;
	}

	public Collection<EducationRecord> findAll() {
		final Collection<EducationRecord> res = this.educationDataRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public EducationRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final EducationRecord res = this.educationDataRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public EducationRecord save(final EducationRecord educationData, final int curriculaId) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(educationData);
		if (educationData.getEndDate() != null)
			Assert.isTrue(educationData.getEndDate().after(educationData.getStartDate()), "End date must be after start date");
		if (educationData.getId() != 0)
			Assert.isTrue(this.hackerService.hasEducationData(me.getId(), educationData.getId()), "This personal data is not of your property");
		final EducationRecord res = this.educationDataRepository.save(educationData);

		Assert.notNull(res);

		final Curriculum curricula = this.curriculaService.findOne(curriculaId);
		if (educationData.getId() == 0) {
			final Collection<EducationRecord> misc = curricula.getEducations();
			misc.add(educationData);
			curricula.setEducations(misc);
			this.curriculaService.save(curricula);
		}
		return res;
	}

	public void delete(final EducationRecord mR) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.hackerService.findRookyByEducationDatas(mR.getId()) == me, "No puede borrar un EducationData que no pertenezca a su historia.");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final EducationRecord res = this.findOne(mR.getId());

		final Curriculum curricula = this.curriculaService.findCurriculaByEducationData(res.getId());

		final Collection<EducationRecord> educationDatas = curricula.getEducations();

		Assert.isTrue(this.hackerService.hasEducationData(me.getId(), res.getId()), "This personal data is not of your property");

		educationDatas.remove(res);

		this.educationDataRepository.delete(res.getId());

		curricula.setEducations(educationDatas);
		this.curriculaService.save(curricula);

	}

	final EducationRecord makeCopyAndSave(final EducationRecord ed, final Curriculum curricula) {
		EducationRecord result = this.create();
		result.setDegree(ed.getDegree());
		result.setEndDate(ed.getEndDate());
		result.setInstitution(ed.getInstitution());
		result.setMark(ed.getMark());
		result.setStartDate(ed.getStartDate());
		Assert.notNull(result, "copy os education data is null");
		result = this.save(result, curricula.getId());
		Assert.notNull(result, "retrieved copy od education data is null");
		return result;

	}

	public void flush() {
		this.educationDataRepository.flush();
	}

}
