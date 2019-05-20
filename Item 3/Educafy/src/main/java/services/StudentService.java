
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.CurriculaRepository;
import repositories.StudentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Student;

@Service
@Transactional
public class StudentService {

	@Autowired
	private StudentRepository	studentRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private UserAccountService	userAccountService;

	//	@Autowired
	//	private CurriculaService	curriculaService;

	@Autowired
	private CurriculaRepository	curriculaRepository;

	@Autowired
	private Validator			validator;


	public Student create() {
		final Student student = new Student();
		this.actorService.setAuthorityUserAccount(Authority.STUDENT, student);

		return student;
	}

	public Student findOne(final int studentId) {
		Assert.isTrue(studentId != 0);
		final Student result = this.studentRepository.findOne(studentId);
		Assert.notNull(result);
		return result;
	}

	//	public Student save(final Student student) {
	//		Assert.notNull(student);
	//		Student result;
	//
	//		if (student.getId() == 0) {
	//			final Finder finder = this.finderService.createForNewStudent();
	//			student.setFinder(finder);
	//			this.actorService.setAuthorityUserAccount(Authority.STUDENT, student);
	//			result = this.studentRepository.save(student);
	//			//			this.folderService.setFoldersByDefault(result);
	//
	//			final Curriculum curricula = this.curriculaService.createForNewStudent();
	//			curricula.setStudent(result);
	//			final Curriculum res = this.curriculaRepository.save(curricula);
	//			Assert.notNull(res);
	//
	//		} else {
	//			this.actorService.checkForSpamWords(student);
	//			final Actor principal = this.actorService.findByPrincipal();
	//			Assert.isTrue(principal.getId() == student.getId(), "You only can edit your info");
	//			result = (Student) this.actorService.save(student);
	//		}
	//		return result;
	//	}

	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Student student) {
		Assert.notNull(student);
		Assert.isTrue(this.findByPrincipal().equals(student));
		Assert.isTrue(student.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == student.getId(), "You only can edit your info");
		Assert.isTrue(this.studentRepository.exists(student.getId()));
		this.studentRepository.delete(student);
	}

	//	public void deletePersonalData() {
	//		final Student principal = this.findByPrincipal();
	//		this.finderService.clear(this.finderService.findStudentFinder());
	//		final List<String> s = new ArrayList<>();
	//		s.add("DELETED");
	//		principal.setAddress(null);
	//		principal.setEmail("DELETED@mail.de");
	//		principal.setSurname(s);
	//		//principal.setName("");
	//		principal.setPhone(null);
	//		principal.setPhoto(null);
	//		principal.setSpammer(false);
	//		principal.setVat(0.0);
	//		final Authority ban = new Authority();
	//		ban.setAuthority(Authority.BANNED);
	//		principal.getUserAccount().getAuthorities().add(ban);
	//		this.studentRepository.save(principal);
	//	}

	/* ========================= OTHER METHODS =========================== */

	public Student findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Student student = this.findByUserId(user.getId());
		Assert.notNull(student);
		final boolean bool = this.actorService.checkAuthority(student, Authority.STUDENT);
		Assert.isTrue(bool);

		return student;
	}

	public Student findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Student student = this.studentRepository.findByUserId(id);
		return student;
	}

	/**
	 * The average, minimum, maximum and standard deviation of the number of applications per student
	 * 
	 * @author a8081
	 */
	public Double[] getStatisticsOfApplicationsPerStudent() {
		final Double[] res = this.studentRepository.getStatisticsOfApplicationsPerStudent();
		Assert.notNull(res);
		return res;
	}

	/**
	 * Students who have made more applications
	 * 
	 * @author a8081
	 */
	public Collection<Student> getStudentsMoreApplications() {
		final Collection<Student> res = this.studentRepository.getStudentsMoreApplications();
		Assert.notNull(res);
		return res;
	}

	public void flush() {
		this.studentRepository.flush();
	}

	//	public Student reconstruct(final ActorForm actorForm, final BindingResult binding) {
	//		Student student;
	//
	//		if (actorForm.getId() == 0) {
	//			student = this.create();
	//			student.setName(actorForm.getName());
	//			student.setSurname(actorForm.getSurname());
	//			student.setPhoto(actorForm.getPhoto());
	//			student.setPhone(actorForm.getPhone());
	//			student.setEmail(actorForm.getEmail());
	//			student.setAddress(actorForm.getAddress());
	//			student.setVat(actorForm.getVat());
	//			student.setVersion(actorForm.getVersion());
	//			student.setFinder(this.finderService.create());
	//			//			student.setScore(0.0);
	//			//			student.setSpammer(false);
	//			final UserAccount account = this.userAccountService.create();
	//			final Collection<Authority> authorities = new ArrayList<>();
	//			final Authority auth = new Authority();
	//			auth.setAuthority(Authority.STUDENT);
	//			authorities.add(auth);
	//			account.setAuthorities(authorities);
	//			account.setUsername(actorForm.getUserAccountuser());
	//			account.setPassword(actorForm.getUserAccountpassword());
	//			student.setUserAccount(account);
	//		} else {
	//			student = this.studentRepository.findOne(actorForm.getId());
	//			student.setName(actorForm.getName());
	//			student.setSurname(actorForm.getSurname());
	//			student.setPhoto(actorForm.getPhoto());
	//			student.setPhone(actorForm.getPhone());
	//			student.setEmail(actorForm.getEmail());
	//			student.setAddress(actorForm.getAddress());
	//			student.setVat(actorForm.getVat());
	//			student.setVersion(actorForm.getVersion());
	//			student.setFinder(this.finderService.findStudentFinder());
	//			final UserAccount account = this.userAccountService.findOne(student.getUserAccount().getId());
	//			account.setUsername(actorForm.getUserAccountuser());
	//			account.setPassword(actorForm.getUserAccountpassword());
	//			student.setUserAccount(account);
	//		}
	//
	//		this.validator.validate(student, binding);
	//		if (binding.hasErrors())
	//			throw new ValidationException();
	//
	//		return student;
	//	}
	public Student findStudentByCurricula(final int id) {
		final Student result = this.studentRepository.findStudentByCurricula(id);
		return result;
	}

	public Student findStudentByPersonalData(final int id) {
		final Student result = this.studentRepository.findStudentByPersonalData(id);
		return result;
	}

	public Student findStudentByMiscellaneous(final int id) {
		final Student result = this.studentRepository.findStudentByMiscellaneous(id);
		return result;
	}

	public Student findStudentByEducationDatas(final int id) {
		final Student result = this.studentRepository.findStudentByEducationDatas(id);
		return result;
	}

	public Student findStudentByPositionDatas(final int id) {
		final Student result = this.studentRepository.findStudentByPositionDatas(id);
		return result;
	}

	public Boolean hasPersonalData(final int studentId, final int dataId) {
		final Boolean result = this.studentRepository.hasPersonalData(studentId, dataId);
		Assert.notNull(result, "hasPersonalData returns null");
		return result;
	}

	public Boolean hasEducationData(final int studentId, final int dataId) {
		final Boolean result = this.studentRepository.hasEducationData(studentId, dataId);
		Assert.notNull(result, "hasEducationData returns null");
		return result;
	}

	public Boolean hasPositionData(final int studentId, final int dataId) {
		final Boolean result = this.studentRepository.hasPositionData(studentId, dataId);
		Assert.notNull(result, "hasPositionData returns null");
		return result;
	}

	public Boolean hasMiscellaneousData(final int studentId, final int dataId) {
		final Boolean result = this.studentRepository.hasMiscellaneousData(studentId, dataId);
		Assert.notNull(result, "hasMiscellanousData returns null");
		return result;
	}

	public Boolean hasCurricula(final int studentId, final int dataId) {
		final Boolean result = this.studentRepository.hasCurricula(studentId, dataId);
		Assert.notNull(result, "hasCurricula returns null");
		return result;
	}

	public Student findStudentByCopyCurricula(final int id) {
		final Student result = this.studentRepository.findStudentByCopyCurricula(id);
		Assert.notNull(result, "student found by copy of curricula is null");
		return result;
	}

}
