
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import domain.Finder;
import domain.Lesson;
import domain.Student;

@Service
@Transactional
public class FinderService {

	@Autowired
	private FinderRepository				finderRepository;

	@Autowired
	private ConfigurationParametersService	configParamService;

	@Autowired
	private StudentService					studentService;

	@Autowired
	private LessonService					lessonService;


	//Metodos CRUD

	public Finder create() {
		final Finder finder = new Finder();
		finder.setKeyword("");
		finder.setSubjectLevel(null);
		finder.setSubjectName(null);
		finder.setTeacherName(null);
		final Collection<Lesson> ps = new ArrayList<Lesson>();
		finder.setCreationDate(new Date(System.currentTimeMillis()));
		finder.setLessons(ps);
		return finder;
	}

	public Collection<Finder> findAll() {
		final Collection<Finder> res = this.finderRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Finder findOne(final int finderId) {
		Assert.notNull(finderId);
		Assert.isTrue(finderId != 0);
		final Finder res = this.finderRepository.findOne(finderId);
		Assert.notNull(res);
		return res;
	}

	// Antes de guardar tengo que pasar por este metodo para setearle las nuevas procesiones segun los nuevos parametros
	public Finder find(final Finder finder) {
		this.studentService.findByPrincipal();
		List<Lesson> result = new ArrayList<>(this.lessonService.findLessons(finder.getKeyword(), finder.getSubjectLevel(), finder.getSubjectName(), finder.getTeacherName()));
		final int maxResults = this.configParamService.find().getMaxFinderResults();
		if (result.size() > maxResults) {
			Collections.shuffle(result);
			result = result.subList(0, maxResults);
		}
		finder.setLessons(result);
		return this.save(finder);
	}

	public Finder save(final Finder finder) {
		final Student student = this.studentService.findByPrincipal();
		Assert.notNull(finder);
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(this.finderRepository.findStudentFinder(student.getId()).getId() == finder.getId(), "You're not owner of this finder, you cannot modify it");

		finder.setCreationDate(new Date(System.currentTimeMillis()));
		final Finder res = this.finderRepository.save(finder);
		Assert.notNull(res);

		student.setFinder(finder);
		this.studentService.save(student);
		return res;
	}

	public Finder createForNewStudent() {
		final Finder finder = new Finder();
		finder.setKeyword("");
		finder.setSubjectLevel(null);
		finder.setSubjectName(null);
		finder.setTeacherName(null);
		final Collection<Lesson> ps = new ArrayList<Lesson>();
		finder.setCreationDate(new Date(System.currentTimeMillis()));
		finder.setLessons(ps);
		final Finder res = this.finderRepository.save(finder);
		return res;
	}

	public void delete(final Finder finder) {
		Assert.notNull(finder);
		final Student student = this.studentService.findByPrincipal();
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(this.finderRepository.exists(finder.getId()));
		Assert.isTrue(this.finderRepository.findStudentFinder(student.getId()).getId() == finder.getId(), "You're not owner of this finder, you cannot delete it");
		this.finderRepository.delete(finder);
	}

	public Finder findStudentFinder() {
		final Student principal = this.studentService.findByPrincipal();

		final Finder finder = this.finderRepository.findStudentFinder(principal.getId());
		Assert.notNull(finder);

		// final int finderTime = this.configParamService.find().getFinderTime();
		// final LocalDateTime ldt = new LocalDateTime(finder.getCreationDate());
		// ldt.plusHours(finderTime);

		// if (ldt.isBefore(LocalDateTime.now()))
		if (this.clearCache(finder))
			this.clear(finder);

		return finder;
	}

	public Finder clear(final Finder finder) {
		final Student student = this.studentService.findByPrincipal();
		final Finder result = this.finderRepository.findStudentFinder(student.getId());
		Assert.isTrue(result.equals(finder), "You're not owner of this finder");
		Assert.notNull(result);
		result.setKeyword("");
		result.setSubjectLevel(null);
		result.setSubjectName(null);
		result.setTeacherName(null);
		finder.setCreationDate(new Date(System.currentTimeMillis()));
		result.setLessons(new ArrayList<Lesson>());
		final Finder saved = this.save(result);
		return saved;
	}

	public boolean clearCache(final Finder finder) {
		Assert.notNull(finder);

		final double update = finder.getCreationDate().getTime();
		final double current = new Date(System.currentTimeMillis()).getTime();
		final Double period = (current - update) / 3600000;
		final int max = this.configParamService.find().getFinderTime();

		return max <= period;
	}

	public void flush() {
		this.finderRepository.flush();

	}

}
