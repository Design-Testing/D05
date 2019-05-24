
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculumService;
import services.TeacherService;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.Teacher;

@Controller
@RequestMapping("/curriculum")
public class CurriculumController extends AbstractController {

	@Autowired
	private CurriculumService	curriculumService;

	@Autowired
	private TeacherService		teacherService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Teacher teacher = this.teacherService.findByPrincipal();
		final Curriculum curriculum = teacher.getCurriculum();
		if (!(curriculum == null)) {
			final InceptionRecord inceptionRecord = curriculum.getInceptionRecord();
			final Collection<PeriodRecord> periodRecords = curriculum.getPeriodRecords();
			final Collection<LegalRecord> legalRecords = curriculum.getLegalRecords();
			final Collection<LinkRecord> linkRecords = curriculum.getLinkRecords();
			final Collection<MiscellaneousRecord> miscellaneousRecords = curriculum.getMiscellaneousRecords();

			res = new ModelAndView("curriculum/display");
			res.addObject("curriculum", curriculum);
			res.addObject("buttons", true);
			res.addObject("inceptionRecord", inceptionRecord);
			res.addObject("periodrecords", periodRecords);
			res.addObject("legalRecord", legalRecords);
			res.addObject("linkRecords", linkRecords);
			res.addObject("miscellaneousRecords", miscellaneousRecords);
		} else {
			res = new ModelAndView("curriculum/create");
			res.addObject("curriculum", curriculum);
		}

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Teacher teacher = this.teacherService.findByPrincipal();
		if (teacher.getCurriculum() == null) {
			final Curriculum curriculum = this.curriculumService.create();
			teacher.setCurriculum(curriculum);
			this.curriculumService.save(curriculum);
			result = this.list();
			result.addObject("curriculum", curriculum);
		}
		result = this.list();
		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Curriculum curriculum, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(curriculum);
		else
			try {
				if (curriculum.getVersion() == 0) {
					final Teacher teacher = this.teacherService.findByPrincipal();
					teacher.setCurriculum(curriculum);
					this.curriculumService.save(curriculum);
				}
				result = this.list();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(curriculum, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int curriculumId) {
		ModelAndView result;
		//		final Teacher teacher = this.teacherService.findByPrincipal();
		final Curriculum curriculum = this.curriculumService.findOne(curriculumId);
		this.curriculumService.delete(curriculum);
		result = new ModelAndView("curriculum/create");
		result.addObject("curriculum", curriculum);
		return result;
	}
	protected ModelAndView createEditModelAndView(final Curriculum curriculum) {
		ModelAndView result;

		result = this.createEditModelAndView(curriculum, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Curriculum curriculum, final String message) {
		ModelAndView result;

		result = new ModelAndView("curriculum/display");
		result.addObject("curriculum", curriculum);
		result.addObject("message", message);

		return result;

	}
}
