
package controllers.auditor;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import services.AuditorService;
import services.PositionService;
import controllers.AbstractController;
import domain.Audit;
import domain.Auditor;
import domain.Position;
import forms.AuditForm;

@Controller
@RequestMapping("/audit/auditor")
public class AuditAuditorController extends AbstractController {

	@Autowired
	private AuditService	auditService;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private PositionService	positionService;

	final String			lang	= LocaleContextHolder.getLocale().getLanguage();


	//este método es al hacer click en auditar a una posición
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		ModelAndView result = new ModelAndView();
		final Auditor auditor = this.auditorService.findByPrincipal();

		try {
			final AuditForm auditForm = new AuditForm();

			result = new ModelAndView("audit/edit"); //TODO lo lleva a la vista de edición para crear el audit
			result.addObject("auditForm", auditForm);
			result.addObject("auditor", auditor);
			result.addObject("positionId", positionId);

		} catch (final Throwable oops) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", oops.getMessage());
		}

		return result;
	}

	//cuando haces click en editar en el listado audits en modo draft
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int auditId, @RequestParam final int positionId) {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.findOne(auditId);

		if (audit != null) {
			result = this.createEditModelAndView(audit);
			result.addObject("audit", audit);
			result.addObject("positionId", positionId);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	//para guardar una vez en método de edición
	//puede llegar aquí porque está auditando por primra vez o porque quire editar una audición
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final AuditForm auditForm, final BindingResult binding, @RequestParam final int positionId) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("audit/edit");
			result.addObject("auditForm", auditForm);
			result.addObject("positionId", positionId);
			result.addObject("errors", binding.getAllErrors());
		} else
			try {
				final Audit audit = this.auditService.reconstruct(auditForm, binding, positionId);
				this.auditService.save(audit, positionId);
				result = this.listDraft();
			} catch (final ValidationException oops) {
				result = new ModelAndView("audit/edit");
				result.addObject("auditForm", auditForm);
			} catch (final Throwable oops) {
				result = new ModelAndView("audit/edit");
				result.addObject("message", "audit.error");
			}

		return result;
	}
	//este método es al hacer click en pasar a modo final
	@RequestMapping(value = "/toFinal", method = RequestMethod.GET)
	public ModelAndView toFinal(@RequestParam final int auditId) {
		ModelAndView result = new ModelAndView();
		final Auditor auditor = this.auditorService.findByPrincipal();
		final Audit audit;

		try {

			audit = this.auditService.toFinalMode(auditId);

			result = this.listFinal();

		} catch (final Throwable oops) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", oops.getMessage());
		}

		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int auditId) {
		final ModelAndView result;
		final Audit audit;

		audit = this.auditService.findOne(auditId);

		if (audit != null) {
			result = new ModelAndView("audit/display");
			result.addObject("audit", audit);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// LIST  draft audits by auditor
	@RequestMapping(value = "/listDraft", method = RequestMethod.GET)
	public ModelAndView listDraft() {
		final ModelAndView result;
		final Collection<Audit> audits;
		final Auditor logged = this.auditorService.findByPrincipal();
		audits = this.auditService.findAllDraftByAuditor(logged.getId());

		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "audit/auditor/listDraft.do");
		result.addObject("buttons", true);

		return result;
	}

	// LIST  final audits by auditor
	@RequestMapping(value = "/listFinal", method = RequestMethod.GET)
	public ModelAndView listFinal() {
		final ModelAndView result;
		final Collection<Audit> audits;
		final Auditor logged = this.auditorService.findByPrincipal();
		audits = this.auditService.findAllFinalByAuditor(logged.getId());

		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "audit/auditor/listFinal.do");
		result.addObject("buttons", false);

		return result;
	}

	// LIST all final audits of a position
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll(@RequestParam final int positionId) {
		final ModelAndView result;
		final Collection<Audit> audits = this.auditService.findAllByPosition(positionId);

		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "audit/auditor/listAll.do");
		result.addObject("buttons", true);

		return result;
	}

	// LIST  draft audits by auditor
	@RequestMapping(value = "/listFreePositions", method = RequestMethod.GET)
	public ModelAndView listFreePositions() {
		final ModelAndView result;
		final Collection<Position> positions;
		final Auditor logged = this.auditorService.findByPrincipal();
		positions = this.positionService.findFreePositionsByAuditor(logged.getId());

		result = new ModelAndView("audit/listPositions");
		result.addObject("positions", positions);

		result.addObject("lang", this.lang);
		result.addObject("requestURI", "audit/auditor/listFreePositions.do");
		result.addObject("buttons", true);

		return result;
	}

	//cuando haces click en borrar en el listado audits en modo draft
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int auditId) {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.findOne(auditId);

		try {

			this.auditService.delete(audit);

			result = this.listDraft();

		} catch (final Throwable oops) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", oops.getMessage());
		}

		return result;
	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Audit audit) {
		ModelAndView result;

		result = this.createEditModelAndView(audit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Audit audit, final String messageCode) {
		Assert.notNull(audit);
		final ModelAndView result;

		result = new ModelAndView("audit/edit");
		result.addObject("auditForm", this.auditService.inyect(audit)); // this.constructPruned(parade));

		result.addObject("message", messageCode);

		return result;
	}

}
