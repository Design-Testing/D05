
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CurriculaRepository;
import repositories.TeacherRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Curriculum;
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
	private CurriculaService	curriculaService;

	@Autowired
	private CurriculaRepository	curriculaRepository;

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

	public Teacher save(final Teacher teacher) {
		Assert.notNull(teacher);
		Teacher result;

		if (teacher.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.TEACHER, teacher);
			result = this.teacherRepository.save(teacher);
			//			this.folderService.setFoldersByDefault(result);

			final Curriculum curricula = this.curriculaService.createForNewTeacher();
			curricula.setTeacher(result);
			final Curriculum res = this.curriculaRepository.save(curricula);
			Assert.notNull(res);

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

	public Teacher findTeacherByCurricula(final int curriculaId) {
		final Teacher result = this.teacherRepository.findTeacherByCurricula(curriculaId);
		return result;
	}

	public Teacher findTeacherByPersonalRecord(final int personalRecorId) {
		final Teacher result = this.teacherRepository.findTeacherByPersonalRecord(personalRecorId);
		return result;
	}

	public Teacher findTeacherByMiscellaneous(final int miscellaneousRecordId) {
		final Teacher result = this.teacherRepository.findTeacherByMiscellaneous(miscellaneousRecordId);
		return result;
	}

	public Teacher findTeacherByEducationRecords(final int educationRecordId) {
		final Teacher result = this.teacherRepository.findTeacherByEducationRecords(educationRecordId);
		return result;
	}

	public Boolean hasPersonalRecord(final int teacherId, final int personalRecordId) {
		final Boolean result = this.teacherRepository.hasPersonalRecord(teacherId, personalRecordId);
		Assert.notNull(result, "hasPersonalData returns null");
		return result;
	}

	public Boolean hasEducationRecord(final int teacherId, final int educationRecordId) {
		final Boolean result = this.teacherRepository.hasEducationRecord(teacherId, educationRecordId);
		Assert.notNull(result, "hasEducationData returns null");
		return result;
	}

	public Boolean hasMiscellaneousRecord(final int teacherId, final int dataId) {
		final Boolean result = this.teacherRepository.hasMiscellaneousRecord(teacherId, dataId);
		Assert.notNull(result, "hasMiscellanousData returns null");
		return result;
	}

	public Boolean hasCurricula(final int teacherId, final int miscellaneousRecordId) {
		final Boolean result = this.teacherRepository.hasCurricula(teacherId, miscellaneousRecordId);
		Assert.notNull(result, "hasCurricula returns null");
		return result;
	}

	//	public Teacher findTeacherByCopyCurricula(final int curriculaId) {
	//		final Teacher result = this.teacherRepository.findTeacherByCopyCurricula(curriculaId);
	//		Assert.notNull(result, "teacher found by copy of curricula is null");
	//		return result;
	//	}

}
