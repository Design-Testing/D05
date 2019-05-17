
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EducationDataRepository;
import domain.Curriculum;
import domain.EducationData;
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

	public EducationData create() {
		final EducationData eData = new EducationData();
		eData.setDegree("");
		eData.setInstitution("");
		eData.setStartDate(new Date(System.currentTimeMillis() - 1));
		eData.setMark(0);
		return eData;
	}

	public Collection<EducationData> findAll() {
		final Collection<EducationData> res = this.educationDataRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public EducationData findOne(final int id) {
		Assert.isTrue(id != 0);
		final EducationData res = this.educationDataRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public EducationData save(final EducationData educationData, final int curriculaId) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(educationData);
		if (educationData.getEndDate() != null)
			Assert.isTrue(educationData.getEndDate().after(educationData.getStartDate()), "End date must be after start date");
		if (educationData.getId() != 0)
			Assert.isTrue(this.hackerService.hasEducationData(me.getId(), educationData.getId()), "This personal data is not of your property");
		final EducationData res = this.educationDataRepository.save(educationData);

		Assert.notNull(res);

		final Curriculum curricula = this.curriculaService.findOne(curriculaId);
		if (educationData.getId() == 0) {
			final Collection<EducationData> misc = curricula.getEducations();
			misc.add(educationData);
			curricula.setEducations(misc);
			this.curriculaService.save(curricula);
		}
		return res;
	}

	public void delete(final EducationData mR) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.hackerService.findRookyByEducationDatas(mR.getId()) == me, "No puede borrar un EducationData que no pertenezca a su historia.");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final EducationData res = this.findOne(mR.getId());

		final Curriculum curricula = this.curriculaService.findCurriculaByEducationData(res.getId());

		final Collection<EducationData> educationDatas = curricula.getEducations();

		Assert.isTrue(this.hackerService.hasEducationData(me.getId(), res.getId()), "This personal data is not of your property");

		educationDatas.remove(res);

		this.educationDataRepository.delete(res.getId());

		curricula.setEducations(educationDatas);
		this.curriculaService.save(curricula);

	}

	final EducationData makeCopyAndSave(final EducationData ed, final Curriculum curricula) {
		EducationData result = this.create();
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
