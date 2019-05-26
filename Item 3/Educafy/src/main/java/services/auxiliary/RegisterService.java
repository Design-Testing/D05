
package services.auxiliary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import security.UserAccount;
import security.UserAccountRepository;
import services.AdministratorService;
import services.CertifierService;
import services.StudentService;
import services.TeacherService;
import services.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Certifier;
import domain.Student;
import domain.Teacher;
import forms.ActorForm;

@Service
@Transactional
public class RegisterService {

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private StudentService			studentService;

	@Autowired
	private CertifierService		certifierService;

	@Autowired
	private TeacherService			teacherService;


	public Administrator saveAdmin(final Administrator admin, final BindingResult binding) {
		Administrator result;
		final UserAccount ua = admin.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (admin.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			admin.setUserAccount(ua);
			result = this.administratorService.save(admin);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Administrator old = this.administratorService.findOne(admin.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.administratorService.save(admin);

		}

		return result;
	}

	public Student saveStudent(final Student student, final BindingResult binding) {
		Student result;
		final UserAccount ua = student.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (student.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			student.setUserAccount(ua);
			result = this.studentService.save(student);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Student old = this.studentService.findOne(student.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.studentService.save(student);

		}

		return result;
	}

	public Teacher saveTeacher(final Teacher teacher, final BindingResult binding) {
		Teacher result;
		final UserAccount ua = teacher.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (teacher.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			teacher.setUserAccount(ua);
			result = this.teacherService.save(teacher);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Teacher old = this.teacherService.findOne(teacher.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.teacherService.save(teacher);

		}

		return result;
	}

	public Certifier saveCertifier(final Certifier certifier, final BindingResult binding) {
		Certifier result;
		final UserAccount ua = certifier.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (certifier.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			certifier.setUserAccount(ua);
			result = this.certifierService.save(certifier);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Certifier old = this.certifierService.findOne(certifier.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.certifierService.save(certifier);

		}

		return result;
	}

	public ActorForm inyect(final Actor actor) {
		final ActorForm result = new ActorForm();

		result.setAddress(actor.getAddress());
		result.setEmail(actor.getEmail());
		result.setId(actor.getId());
		result.setName(actor.getName());
		result.setPhone(actor.getPhone());
		result.setPhoto(actor.getPhoto());
		result.setSurname(actor.getSurname());
		result.setVersion(actor.getVersion());
		// CreditCard

		result.setUserAccountpassword(actor.getUserAccount().getPassword());
		result.setUserAccountuser(actor.getUserAccount().getUsername());
		result.setVersion(actor.getVersion());

		return result;
	}

}
