
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
import services.EducationRecordService;
import services.TeacherService;
import domain.Curriculum;
import domain.EducationRecord;
import domain.Teacher;

@Controller
@RequestMapping("/educationRecord")
public class EducationRecordController extends AbstractController {

	@Autowired
	private EducationRecordService	educationRecordService;

	@Autowired
	private TeacherService			teacherService;

	@Autowired
	private CurriculumService		curriculumService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculumId) {

		ModelAndView result;

		final EducationRecord educationRecord = this.educationRecordService.create();

		result = new ModelAndView("educationRecord/edit");
		result.addObject("educationRecord", educationRecord);
		result.addObject("curriculumId", curriculumId);
		result.addObject("message", null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int educationRecordId, @RequestParam final int curriculumId) {
		ModelAndView result;
		try {
			final EducationRecord educationRecord;
			final Teacher teacher = this.teacherService.findByPrincipal();
			educationRecord = this.educationRecordService.findOne(educationRecordId);

			Assert.isTrue(this.teacherService.hasEducationRecord(teacher.getId(), educationRecordId), "This personal record is not of your property");

			result = new ModelAndView("educationRecord/edit");
			result.addObject("educationRecord", educationRecord);
			result.addObject("curriculumId", curriculumId);
			result.addObject("message", null);

		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam final int curriculumId, @Valid final EducationRecord educationRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors()) {
			result = this.createEditModelAndView(educationRecord);
			result.addObject("curriculumId", curriculumId);
		} else
			try {
				this.educationRecordService.save(educationRecord, curriculumId);

				final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
				result = new ModelAndView("curriculum/display");
				result.addObject("curriculum", curriculum);
				result.addObject("curriculumId", curriculum.getId());
				result.addObject("message", null);
				result.addObject("buttons", true);

			} catch (final Throwable e) {
				System.out.println(e.getMessage());
				if (e.getMessage().equals("End date must be after start date")) {
					result = new ModelAndView("educationRecord/edit");
					result.addObject("educationRecord", educationRecord);
					result.addObject("curriculumId", curriculumId);
					result.addObject("message", "alert.dates");
				} else
					result = new ModelAndView("redirect:misc/403");

			}

		return result;
	}

	@RequestMapping(value = "/toFinal", method = RequestMethod.GET)
	public ModelAndView toFinal(@RequestParam final int educationRecordId) {
		ModelAndView result;
		try {
			final EducationRecord educationRecord;
			final Teacher teacher = this.teacherService.findByPrincipal();
			educationRecord = this.educationRecordService.findOne(educationRecordId);
			Assert.isTrue(this.teacherService.hasPersonalRecord(teacher.getId(), educationRecordId), "This education record is not of your property");
			this.educationRecordService.toFinal(educationRecord);
			final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(educationRecord.getId());

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
	public ModelAndView certify(@RequestParam final int educationRecordId) {
		ModelAndView result;
		try {
			final EducationRecord educationRecord;
			educationRecord = this.educationRecordService.findOne(educationRecordId);
			this.educationRecordService.certify(educationRecord);
			final Curriculum curriculum = this.curriculumService.findCurriculumByPersonalRecord(educationRecord.getId());

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
	public ModelAndView display(@RequestParam final int educationRecordId) {

		ModelAndView res;

		final EducationRecord educationRecord = this.educationRecordService.findOne(educationRecordId);

		if (educationRecord != null) {

			res = new ModelAndView("educationRecord/display");
			res.addObject("educationRecord", educationRecord);
			res.addObject("curriculumId", this.curriculumService.findCurriculumByEducationRecord(educationRecord.getId()).getId());
			res.addObject("buttons", false);

			final Curriculum curriculum = this.curriculumService.findOne(this.curriculumService.findCurriculumByEducationRecord(educationRecord.getId()).getId());

			final UserAccount logged = LoginService.getPrincipal();

			final Authority authTeacher = new Authority();
			authTeacher.setAuthority(Authority.TEACHER);
			if (logged.getAuthorities().contains(authTeacher))
				if (curriculum.getTeacher().getId() == this.teacherService.findByPrincipal().getId())
					res.addObject("buttons", true);

		} else
			res = new ModelAndView("redirect:misc/403");

		return res;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int educationRecordId) {
		ModelAndView result;
		final EducationRecord educationRecord = this.educationRecordService.findOne(educationRecordId);
		final Curriculum curriculum = this.curriculumService.findCurriculumByEducationRecord(educationRecordId);
		this.educationRecordService.delete(educationRecord);

		result = new ModelAndView("curriculum/display");
		result.addObject("curriculum", curriculum);
		result.addObject("message", null);
		result.addObject("buttons", true);

		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationRecord educationRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(educationRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final EducationRecord educationRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("educationRecord/edit");
		result.addObject("educationRecord", educationRecord);
		result.addObject("message", message);
		if (educationRecord.getId() != 0)
			result.addObject("curriculumId", this.curriculumService.findCurriculumByEducationRecord(educationRecord.getId()).getId());

		return result;

	}
}
