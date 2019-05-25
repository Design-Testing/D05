
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
import services.MiscellaneousRecordService;
import services.TeacherService;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.Teacher;

@Controller
@RequestMapping("/miscellaneousRecord")
public class MiscellaneousRecordController extends AbstractController {

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private TeacherService				teacherService;

	@Autowired
	private CurriculumService			curriculumService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculumId) {

		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("curriculumId", curriculumId);
		result.addObject("message", null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId, @RequestParam final int curriculumId) {
		ModelAndView result;
		try {
			final MiscellaneousRecord miscellaneousRecord;
			final Teacher teacher = this.teacherService.findByPrincipal();
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

			Assert.isTrue(this.teacherService.hasMiscellaneousRecord(teacher.getId(), miscellaneousRecordId), "This personal record is not of your property");

			result = new ModelAndView("miscellaneousRecord/edit");
			result.addObject("miscellaneousRecord", miscellaneousRecord);
			result.addObject("curriculumId", curriculumId);
			result.addObject("message", null);

		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int curriculumId, @Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors()) {
			result = this.createEditModelAndView(miscellaneousRecord);
			result.addObject("curriculumId", curriculumId);
		} else
			try {
				this.miscellaneousRecordService.save(miscellaneousRecord, curriculumId);

				final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
				result = new ModelAndView("curriculum/display");
				result.addObject("curriculum", curriculum);
				result.addObject("curriculumId", curriculum.getId());
				result.addObject("message", null);
				result.addObject("buttons", true);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int miscellaneousRecordId) {

		ModelAndView res;

		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

		if (miscellaneousRecord != null) {

			res = new ModelAndView("miscellaneousRecord/display");
			res.addObject("miscellaneousRecord", miscellaneousRecord);
			res.addObject("curriculumId", this.curriculumService.findCurriculumByMiscellaneousRecord(miscellaneousRecord.getId()).getId());

			final Curriculum curriculum = this.curriculumService.findOne(this.curriculumService.findCurriculumByMiscellaneousRecord(miscellaneousRecord.getId()).getId());

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
				else
					res.addObject("buttonsAnonymous", true);
			} else if (logged.getAuthorities().contains(authStudent))
				res.addObject("buttonsAnonymous", true);
			else if (logged.getAuthorities().contains(authCertifier))
				res.addObject("buttonsCertifier", true);

		} else
			res = new ModelAndView("redirect:misc/403");

		return res;

	}
	@RequestMapping(value = "/toFinal", method = RequestMethod.GET)
	public ModelAndView toFinal(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		try {
			final MiscellaneousRecord miscellaneousRecord;
			final Teacher teacher = this.teacherService.findByPrincipal();
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			Assert.isTrue(this.teacherService.hasPersonalRecord(teacher.getId(), miscellaneousRecordId), "This miscellaneous record is not of your property");
			this.miscellaneousRecordService.toFinal(miscellaneousRecord);
			final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(miscellaneousRecord.getId());

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

	@RequestMapping(value = "/certify", method = RequestMethod.GET)
	public ModelAndView certify(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		try {
			final MiscellaneousRecord miscellaneousRecord;
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			this.miscellaneousRecordService.certify(miscellaneousRecord);
			final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(miscellaneousRecord.getId());

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

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		final Curriculum curriculum = this.curriculumService.findCurriculumByMiscellaneousRecord(miscellaneousRecordId);
		this.miscellaneousRecordService.delete(miscellaneousRecord);

		result = new ModelAndView("curriculum/display");
		result.addObject("curriculum", curriculum);
		result.addObject("message", null);
		result.addObject("buttons", true);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("message", message);
		if (miscellaneousRecord.getId() != 0)
			result.addObject("curriculumId", this.curriculumService.findCurriculumByMiscellaneousRecord(miscellaneousRecord.getId()).getId());

		return result;

	}
}
