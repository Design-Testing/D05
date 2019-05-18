
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculaRepository;
import domain.Curriculum;
import domain.EducationRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.PositionData;
import domain.Rooky;

@Service
@Transactional
public class CurriculaService {

	@Autowired
	private CurriculaRepository			curriculaRepository;

	@Autowired
	private RookyService				hackerService;

	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private PositionDataService			positionDataService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;


	public Curriculum create() {
		final Curriculum curricula = new Curriculum();
		final Rooky hacker = this.hackerService.findByPrincipal();

		final PersonalRecord personalData = this.personalDataService.create();

		personalData.setFullName(hacker.getName());
		personalData.setStatement("Statement " + hacker.getName());
		personalData.setPhone(hacker.getPhone());

		curricula.setPersonalRecord(personalData);

		final Collection<PositionData> positionData = new ArrayList<PositionData>();
		curricula.setPositions(positionData);

		final Collection<MiscellaneousRecord> miscellaneousData = new ArrayList<MiscellaneousRecord>();
		curricula.setMiscellaneous(miscellaneousData);

		final Collection<EducationRecord> educationData = new ArrayList<EducationRecord>();
		curricula.setEducations(educationData);

		return curricula;

	}

	public Curriculum createForNewRooky() {
		final Curriculum curricula = new Curriculum();

		final PersonalRecord personalData = this.personalDataService.create();

		personalData.setFullName("full name");
		personalData.setStatement("statement");

		curricula.setPersonalRecord(personalData);

		final Collection<PositionData> positionData = new ArrayList<PositionData>();
		curricula.setPositions(positionData);

		final Collection<MiscellaneousRecord> miscellaneousData = new ArrayList<MiscellaneousRecord>();
		curricula.setMiscellaneous(miscellaneousData);

		final Collection<EducationRecord> educationData = new ArrayList<EducationRecord>();
		curricula.setEducations(educationData);

		return curricula;

	}

	public Collection<Curriculum> findAll() {
		Collection<Curriculum> res = new ArrayList<>();
		res = this.curriculaRepository.findAll();
		Assert.notNull(res, "Find all returns a null collection");
		return res;
	}

	public Curriculum findOne(final int curriculaId) {
		Assert.isTrue(curriculaId != 0);
		final Curriculum res = this.curriculaRepository.findOne(curriculaId);
		Assert.notNull(res);
		return res;
	}

	public Curriculum save(final Curriculum curricula) {
		Assert.notNull(curricula);
		final Curriculum res;
		final Rooky hacker = this.hackerService.findByPrincipal();
		if (curricula.getId() != 0)
			Assert.isTrue(this.hackerService.findRookyByCurricula(curricula.getId()).equals(hacker), "logged actor doesnt match curricula's owner");
		else
			curricula.setRooky(hacker);
		res = this.curriculaRepository.save(curricula);
		return res;
	}

	public void delete(final Curriculum curricula) {
		Assert.notNull(curricula);
		Assert.isTrue(curricula.getId() != 0);
		final Rooky hacker = this.hackerService.findByPrincipal();
		final Curriculum retrieved = this.findOne(curricula.getId());
		Assert.isTrue(this.hackerService.findRookyByCurricula(retrieved.getId()) == hacker, "Not possible to delete the curricula of other hacker.");
		this.curriculaRepository.delete(retrieved.getId());
	}

	/**
	 * The average, minimum, maximum and standard deviation of the number of curricula per hacker
	 * 
	 * @author a8081
	 */
	public Double[] getStatisticsOfCurriculaPerRooky() {
		final Double[] res = this.curriculaRepository.getStatisticsOfCurriculaPerRooky();
		Assert.notNull(res);
		return res;
	}

	public Collection<Curriculum> findCurriculaByRooky(final int id) {
		final Collection<Curriculum> result = this.curriculaRepository.findCurriculaByRooky(id);
		return result;
	}

	public Curriculum findCurriculaByPersonalData(final int id) {
		final Curriculum result = this.curriculaRepository.findCurriculaByPersonalData(id);
		Assert.notNull(result, "findCurriculaByPersonalData returns null");
		return result;
	}

	public Curriculum findCurriculaByEducationData(final int id) {
		final Curriculum result = this.curriculaRepository.findCurriculaByEducationData(id);
		Assert.notNull(result, "findCurriculaByEducationData returns null");
		return result;
	}

	public Curriculum findCurriculaByPositionData(final int id) {
		final Curriculum result = this.curriculaRepository.findCurriculaByPositionData(id);
		Assert.notNull(result, "findCurriculaByPositionData returns null");
		return result;
	}

	public Curriculum findCurriculaByMiscellaneousData(final int id) {
		final Curriculum result = this.curriculaRepository.findCurriculaByMiscellaneousData(id);
		Assert.notNull(result, "findCurriculaByMiscellanousData returns null");
		return result;
	}

	public Curriculum makeCopyAndSave(final Curriculum curricula) {
		Curriculum result = this.create();
		result.setRooky(curricula.getRooky());

		final PersonalRecord pd = this.personalDataService.makeCopyAndSave(curricula.getPersonalRecord());
		result.setPersonalRecord(pd);
		final Collection<EducationRecord> eds = new ArrayList<EducationRecord>();
		result.setEducations(eds);
		final Collection<MiscellaneousRecord> mds = new ArrayList<MiscellaneousRecord>();
		result.setMiscellaneous(mds);
		final Collection<PositionData> pds = new ArrayList<PositionData>();
		result.setPositions(pds);
		result = this.save(result);

		for (final EducationRecord ed : curricula.getEducations())
			eds.add(this.educationDataService.makeCopyAndSave(ed, result));
		result.setEducations(eds);

		for (final MiscellaneousRecord md : curricula.getMiscellaneous())
			mds.add(this.miscellaneousDataService.makeCopyAndSave(md, result));
		result.setMiscellaneous(mds);

		for (final PositionData pod : curricula.getPositions())
			pds.add(this.positionDataService.makeCopyAndSave(pod, result));
		result.setPositions(pds);

		result.setRooky(null);
		Assert.notNull(result, "copy of curricula is null");
		result = this.curriculaRepository.save(result);
		Assert.notNull(result, "retrieves copy curricula is null");

		return result;
	}

	public void flush() {
		this.curriculaRepository.flush();
	}

	public Collection<Curriculum> findCurriculasByCompany(final int id) {
		final Collection<Curriculum> result = this.curriculaRepository.findCurriculasByCompany(id);
		Assert.notNull(result, "set of curriculas found of a compy is null");
		return result;
	}
}
