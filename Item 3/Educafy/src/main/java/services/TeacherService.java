
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.TeacherRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Teacher;
import forms.ActorForm;

@Service
@Transactional
public class TeacherService {

	@Autowired
	private TeacherRepository	teacherRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private Validator			validator;


	public Teacher create() {
		final Teacher teacher = new Teacher();
		this.actorService.setAuthorityUserAccount(Authority.TEACHER, teacher);

		return teacher;
	}

	public Teacher findOne(final int teacherId) {
		Assert.isTrue(teacherId != 0);
		final Teacher result = this.teacherRepository.findOne(teacherId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Teacher> findAll() {
		final Collection<Teacher> teachers = this.teacherRepository.findAll();
		Assert.notNull(teachers);
		return teachers;
	}

	public Teacher save(final Teacher teacher) {
		Assert.notNull(teacher);
		Teacher result;

		if (teacher.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.TEACHER, teacher);
			result = this.teacherRepository.save(teacher);
			this.folderService.setFoldersByDefault(result);
		} else {
			this.actorService.checkForSpamWords(teacher);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == teacher.getId(), "You only can edit your info");
			result = (Teacher) this.actorService.save(teacher);
		}
		return result;
	}

	public void delete(final Teacher teacher) {
		Assert.notNull(teacher);
		Assert.isTrue(this.findByPrincipal().equals(teacher));
		Assert.isTrue(teacher.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == teacher.getId(), "You only can edit your info");
		Assert.isTrue(this.teacherRepository.exists(teacher.getId()));
		this.teacherRepository.delete(teacher);
	}

	/* ========================= OTHER METHODS =========================== */

	public Teacher findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Teacher teacher = this.findByUserId(user.getId());
		Assert.notNull(teacher);
		final boolean bool = this.actorService.checkAuthority(teacher, Authority.TEACHER);
		Assert.isTrue(bool);

		return teacher;
	}

	public Teacher findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Teacher teacher = this.teacherRepository.findByUserId(id);
		return teacher;
	}

	public void flush() {
		this.teacherRepository.flush();
	}

	public Teacher reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Teacher teacher;

		if (actorForm.getId() == 0) {
			teacher = this.create();
			teacher.setName(actorForm.getName());
			teacher.setSurname(actorForm.getSurname());
			teacher.setPhoto(actorForm.getPhoto());
			teacher.setPhone(actorForm.getPhone());
			teacher.setEmail(actorForm.getEmail());
			teacher.setAddress(actorForm.getAddress());
			teacher.setVat(actorForm.getVat());
			teacher.setVersion(actorForm.getVersion());
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
			teacher.setUserAccount(account);
		} else {
			teacher = this.teacherRepository.findOne(actorForm.getId());
			teacher.setName(actorForm.getName());
			teacher.setSurname(actorForm.getSurname());
			teacher.setPhoto(actorForm.getPhoto());
			teacher.setPhone(actorForm.getPhone());
			teacher.setEmail(actorForm.getEmail());
			teacher.setAddress(actorForm.getAddress());
			teacher.setVat(actorForm.getVat());
			teacher.setVersion(actorForm.getVersion());
			final UserAccount account = this.userAccountService.findOne(teacher.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			teacher.setUserAccount(account);
		}

		this.validator.validate(teacher, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return teacher;
	}

	public void deletePersonalData() {
		final Teacher principal = this.findByPrincipal();
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
		this.teacherRepository.save(principal);
	}

	public Teacher findTeacherByCurriculum(final int id) {
		final Teacher res = this.teacherRepository.findTeacherByCurriculum(id);
		Assert.notNull(res);
		return res;
	}

	public Teacher findTeacherByPersonalRecord(final int id) {
		final Teacher res = this.teacherRepository.findTeacherByPersonalRecord(id);
		Assert.notNull(res);
		return res;
	}

	public Teacher findTeacherByEducationRecord(final int id) {
		final Teacher res = this.teacherRepository.findTeacherByEducationRecord(id);
		Assert.notNull(res);
		return res;
	}

	public Teacher findTeacherByMiscellaneousRecord(final int id) {
		final Teacher res = this.teacherRepository.findTeacherByMiscellaneousRecord(id);
		Assert.notNull(res);
		return res;
	}

	public boolean hasEducationRecord(final int teacherId, final int recordId) {
		final boolean res = this.teacherRepository.hasEducationRecord(teacherId, recordId);
		return res;
	}

	public boolean hasMiscellaneousRecord(final int teacherId, final int recordId) {
		final boolean res = this.teacherRepository.hasMiscellaneousRecord(teacherId, recordId);
		return res;
	}

	public boolean hasPersonalRecord(final int teacherId, final int recordId) {
		final boolean res = this.teacherRepository.hasPersonalRecord(teacherId, recordId);
		return res;
	}

	public Teacher findTeacherByReservation(final int reservationId) {
		Teacher res;
		res = this.teacherRepository.findTeacherByReservation(reservationId);
		return res;
	}
}
