
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PositionDataRepository;
import domain.Curriculum;
import domain.PositionData;
import domain.Rooky;

@Service
@Transactional
public class PositionDataService {

	@Autowired
	private PositionDataRepository	positionDataRepository;

	@Autowired
	private RookyService			hackerService;

	@Autowired
	private CurriculaService		curriculaService;


	//Metodos CRUD

	public PositionData create() {
		final PositionData pData = new PositionData();
		pData.setTitle("");
		pData.setDescription("");
		pData.setStartDate(new Date(System.currentTimeMillis() - 1));
		return pData;
	}

	public Collection<PositionData> findAll() {
		final Collection<PositionData> res = this.positionDataRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PositionData findOne(final int id) {
		Assert.isTrue(id != 0);
		final PositionData res = this.positionDataRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public PositionData save(final PositionData positionData, final int curriculaId) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(positionData);
		if (positionData.getEndDate() != null)
			Assert.isTrue(positionData.getEndDate().after(positionData.getStartDate()), "End date must be after start date");
		if (positionData.getId() != 0)
			Assert.isTrue(this.hackerService.findRookyByPositionDatas(positionData.getId()) == me);

		final PositionData res = this.positionDataRepository.save(positionData);

		Assert.notNull(res);

		final Curriculum curricula = this.curriculaService.findOne(curriculaId);
		if (positionData.getId() == 0) {
			final Collection<PositionData> misc = curricula.getPositions();
			misc.add(positionData);
			curricula.setPositions(misc);
			this.curriculaService.save(curricula);
		}
		return res;
	}

	public void delete(final PositionData mR) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.hackerService.findRookyByPositionDatas(mR.getId()) == me, "No puede borrar un PositionData que no pertenezca a su historia.");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final PositionData res = this.findOne(mR.getId());

		final Curriculum curricula = this.curriculaService.findCurriculaByPositionData(res.getId());

		final Collection<PositionData> positionDatas = curricula.getPositions();
		Assert.isTrue(this.hackerService.hasPositionData(me.getId(), res.getId()), "This personal data is not of your property");

		positionDatas.remove(res);

		this.positionDataRepository.delete(res.getId());

		curricula.setPositions(positionDatas);
		this.curriculaService.save(curricula);

	}

	final PositionData makeCopyAndSave(final PositionData p, final Curriculum curricula) {
		PositionData result = this.create();
		result.setDescription(p.getDescription());
		result.setEndDate(p.getEndDate());
		result.setStartDate(p.getStartDate());
		result.setTitle(p.getTitle());
		Assert.notNull(result, "copy of position data is null");
		result = this.save(result, curricula.getId());
		Assert.notNull(result, "retrieved of position data is null");
		return result;
	}

	public void flush() {
		this.positionDataRepository.flush();
	}

}
