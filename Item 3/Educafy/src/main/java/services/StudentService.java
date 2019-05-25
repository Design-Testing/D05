
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.StudentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Finder;
import domain.Student;
import forms.ActorForm;

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

	@Autowired
	private FolderService		folderService;

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

	public Student save(final Student student) {
		Assert.notNull(student);
		Student result;

		if (student.getId() == 0) {
			final Finder finder = this.finderService.createForNewStudent();
			student.setFinder(finder);
			this.actorService.setAuthorityUserAccount(Authority.STUDENT, student);
			result = this.studentRepository.save(student);
			this.folderService.setFoldersByDefault(result);

		} else {
			this.actorService.checkForSpamWords(student);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == student.getId(), "You only can edit your info");
			result = (Student) this.actorService.save(student);
		}
		return result;
	}

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

	public void deletePersonalData() {
		final Student principal = this.findByPrincipal();
		this.finderService.clear(this.finderService.findStudentFinder());
		final List<String> s = new ArrayList<>();
		s.add("DELETED");
		principal.setAddress(null);
		principal.setEmail("DELETED@mail.de");
		principal.setSurname(s);
		//principal.setName("");
		principal.setPhone(null);
		principal.setPhoto(null);
		principal.setSpammer(false);
		principal.setVat(0.0);
		final Authority ban = new Authority();
		ban.setAuthority(Authority.BANNED);
		principal.getUserAccount().getAuthorities().add(ban);
		this.studentRepository.save(principal);
	}

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

	public void flush() {
		this.studentRepository.flush();
	}

	public Student reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Student student;

		if (actorForm.getId() == 0) {
			student = this.create();
			student.setName(actorForm.getName());
			student.setSurname(actorForm.getSurname());
			student.setPhoto(actorForm.getPhoto());
			student.setPhone(actorForm.getPhone());
			student.setEmail(actorForm.getEmail());
			student.setAddress(actorForm.getAddress());
			student.setVat(actorForm.getVat());
			student.setVersion(actorForm.getVersion());
			student.setFinder(this.finderService.create());
			//			student.setScore(0.0);
			//			student.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.STUDENT);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			student.setUserAccount(account);
		} else {
			student = this.studentRepository.findOne(actorForm.getId());
			student.setName(actorForm.getName());
			student.setSurname(actorForm.getSurname());
			student.setPhoto(actorForm.getPhoto());
			student.setPhone(actorForm.getPhone());
			student.setEmail(actorForm.getEmail());
			student.setAddress(actorForm.getAddress());
			student.setVat(actorForm.getVat());
			student.setVersion(actorForm.getVersion());
			student.setFinder(this.finderService.findStudentFinder());
			final UserAccount account = this.userAccountService.findOne(student.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			student.setUserAccount(account);
		}

		this.validator.validate(student, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return student;
	}

	public Collection<Student> findStudentTenPerCentMoreReservationThanAverage() {
		final Collection<Student> res = this.studentRepository.findTenPerCentMoreReservationThanAverage();
		Assert.notNull(res);
		return res;
	}

	public List<Student> getStudentsOrderByExamScore() {
		List<Student> ls = this.studentRepository.getStudentsOrderByExamScore();
		if (ls.size() > 2)
			ls = ls.subList(0, 3);
		Assert.notNull(ls);
		return ls;
	}

}
