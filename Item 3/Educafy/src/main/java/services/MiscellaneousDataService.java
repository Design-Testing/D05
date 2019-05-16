
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousDataRepository;
import domain.Curricula;
import domain.MiscellaneousData;
import domain.Rooky;

@Service
@Transactional
public class MiscellaneousDataService {

	@Autowired
	private MiscellaneousDataRepository	miscellaneousDataRepository;

	@Autowired
	private RookyService				hackerService;

	@Autowired
	private CurriculaService			curriculaService;


	//Metodos CRUD

	public MiscellaneousData create() {
		final MiscellaneousData mRecord = new MiscellaneousData();
		mRecord.setFreeText("");
		final Collection<String> attachments = new ArrayList<String>();
		mRecord.setAttachments(attachments);
		return mRecord;
	}

	public Collection<MiscellaneousData> findAll() {
		final Collection<MiscellaneousData> res = this.miscellaneousDataRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public MiscellaneousData findOne(final int id) {
		Assert.isTrue(id != 0);
		final MiscellaneousData res = this.miscellaneousDataRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public MiscellaneousData save(final MiscellaneousData miscellaneousData, final int curriculaId) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(miscellaneousData);

		if (miscellaneousData.getId() != 0)
			Assert.isTrue(this.hackerService.hasMiscellaneousData(me.getId(), miscellaneousData.getId()), "This personal data is not of your property");

		final MiscellaneousData res = this.miscellaneousDataRepository.save(miscellaneousData);

		Assert.notNull(res);

		final Curricula curricula = this.curriculaService.findOne(curriculaId);
		if (miscellaneousData.getId() == 0) {
			final Collection<MiscellaneousData> misc = curricula.getMiscellaneous();
			misc.add(miscellaneousData);
			curricula.setMiscellaneous(misc);
			this.curriculaService.save(curricula);
		}
		return res;
	}

	public void delete(final MiscellaneousData mR) {
		final Rooky me = this.hackerService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.hackerService.findRookyByMiscellaneous(mR.getId()) == me, "No puede borrar un MiscellaneousData que no pertenezca a su historia.");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final MiscellaneousData res = this.findOne(mR.getId());

		final Curricula curricula = this.curriculaService.findCurriculaByMiscellaneousData(res.getId());

		final Collection<MiscellaneousData> miscellaneousDatas = curricula.getMiscellaneous();

		Assert.isTrue(this.hackerService.hasMiscellaneousData(me.getId(), res.getId()), "This personal data is not of your property");

		miscellaneousDatas.remove(res);

		this.miscellaneousDataRepository.delete(res.getId());

		curricula.setMiscellaneous(miscellaneousDatas);
		this.curriculaService.save(curricula);

	}

	public MiscellaneousData makeCopyAndSave(final MiscellaneousData md, final Curricula curricula) {
		MiscellaneousData result = this.create();
		result.setFreeText(md.getFreeText());
		final Collection<String> attachements = new ArrayList<String>();
		if (!md.getAttachments().isEmpty() && md.getAttachments() != null)
			for (final String s : md.getAttachments())
				attachements.add(s);
		result.setAttachments(attachements);
		Assert.notNull(result, "copy of miscellaneous data is null");
		result = this.save(result, curricula.getId());
		Assert.notNull(result, "retrieved copy of miscellaneous data is null");
		return result;
	}

	public void flush() {
		this.miscellaneousDataRepository.flush();

	}

}
