
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.CurriculumService;
import services.PersonalRecordService;
import services.TeacherService;
import domain.Curriculum;
import domain.PersonalRecord;
import domain.Teacher;

@Controller
@RequestMapping("/personalRecord")
public class PersonalRecordController extends AbstractController {

	@Autowired
	private PersonalRecordService	personalRecordService;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private CurriculumService		curriculumService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalRecordId) {
		ModelAndView result;
		try {
			final PersonalRecord personalRecord;
			final Teacher teacher = this.teacherService.findByPrincipal();
			personalRecord = this.personalRecordService.findOne(personalRecordId);
			Assert.isTrue(this.teacherService.hasPersonalRecord(teacher.getId(), personalRecordId), "This personal record is not of your property");

			result = this.createEditModelAndView(personalRecord);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/toFinal", method = RequestMethod.GET)
	public ModelAndView toFinal(@RequestParam final int personalRecordId) {
		ModelAndView result;
		try {
			final PersonalRecord personalRecord;
			final Teacher teacher = this.teacherService.findByPrincipal();
			personalRecord = this.personalRecordService.findOne(personalRecordId);
			Assert.isTrue(this.teacherService.hasPersonalRecord(teacher.getId(), personalRecordId), "This personal record is not of your property");
			this.personalRecordService.toFinal(personalRecord);
			final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(personalRecord.getId());

			result = new ModelAndView("curriculum/display");
			result.addObject("curriculum", curriculum);
			result.addObject("curriculumId", curriculum.getId());
			result.addObject("messages", null);
			result.addObject("buttons", true);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PersonalRecord personalRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors()) {
			result = new ModelAndView("personalRecord/edit");
			result.addObject("personalRecord", personalRecord);
		} else
			try {
				this.personalRecordService.save(personalRecord);
				final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(personalRecord.getId());

				result = new ModelAndView("curriculum/display");
				result.addObject("curriculum", curriculum);
				result.addObject("curriculumId", curriculum.getId());
				result.addObject("messages", null);
				result.addObject("buttons", true);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(personalRecord, "general.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/certify", method = RequestMethod.GET)
	public ModelAndView certify(@RequestParam final int personalRecordId) {
		ModelAndView result;
		try {
			final PersonalRecord personalRecord;
			personalRecord = this.personalRecordService.findOne(personalRecordId);
			this.personalRecordService.certify(personalRecord);
			final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(personalRecord.getId());

			result = new ModelAndView("curriculum/display");
			result.addObject("curriculum", curriculum);
			result.addObject("curriculumId", curriculum.getId());
			result.addObject("messages", null);
			result.addObject("buttonsCertifier", true);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int personalRecordId) {

		ModelAndView res;

		final PersonalRecord personalRecord = this.personalRecordService.findOne(personalRecordId);
		final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(personalRecordId);

		if (personalRecord != null) {

			res = new ModelAndView("personalRecord/display");
			res.addObject("personalRecord", personalRecord);
			res.addObject("buttons", false);
			res.addObject("curriculumId", curriculum.getId());

			final UserAccount logged = LoginService.getPrincipal();

			final Authority authTeacher = new Authority();
			authTeacher.setAuthority(Authority.TEACHER);
			final Authority authStudent = new Authority();
			authStudent.setAuthority(Authority.STUDENT);
			final Authority authCertifier = new Authority();
			authCertifier.setAuthority(Authority.CERTIFIER);
			if (logged.getAuthorities().contains(authTeacher)) {
				if (curriculum.getTeacher().getId() == this.teacherService.findByPrincipal().getId())
					res.addObject("buttons", true);
				else {
					Assert.isTrue(personalRecord.getIsDraft() == false, "You can not see a record in draft mode");
					Assert.isTrue(personalRecord.getIsCertified() == true, "You can not see a record that is not certified");
					res.addObject("buttonsAnonymous", true);
				}
			} else if (logged.getAuthorities().contains(authStudent)) {
				Assert.isTrue(personalRecord.getIsDraft() == false, "You can not see a record in draft mode");
				Assert.isTrue(personalRecord.getIsCertified() == true, "You can not see a record that is not certified");
				res.addObject("buttonsAnonymous", true);
			} else if (logged.getAuthorities().contains(authCertifier)) {
				res.addObject("buttonsCertifier", true);
				Assert.isTrue(personalRecord.getIsDraft() == false, "You can not see a record in draft mode");
			}
		} else
			res = new ModelAndView("redirect:misc/403");

		return res;

	}

	protected ModelAndView createEditModelAndView(final PersonalRecord personalRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(personalRecord, null);
		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PersonalRecord personalRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("personalRecord/edit");
		result.addObject("personalRecord", personalRecord);
		result.addObject("message", message);
		if (personalRecord.getId() != 0)
			result.addObject("curriculumId", this.curriculumService.findCurriculumByPersonalRecord(personalRecord.getId()).getId());
		return result;

	}
}
