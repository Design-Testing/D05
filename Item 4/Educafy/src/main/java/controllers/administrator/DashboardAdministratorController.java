
package controllers.administrator;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigurationParametersService;
import services.CurriculumService;
import services.LessonService;
import services.MessageService;
import services.ReservationService;
import services.StudentService;
import services.TeacherService;
import controllers.AbstractController;
import domain.Student;
import domain.Teacher;

@Controller
@RequestMapping(value = "/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	private MessageService					messageService;

	@Autowired
	private LessonService					lessonService;

	@Autowired
	private StudentService					studentService;

	@Autowired
	private TeacherService					teacherService;

	@Autowired
	private CurriculumService				curriculumService;

	@Autowired
	private ReservationService				reservationService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private AdministratorService			administratorService;


	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ModelAndView statistics() {
		this.administratorService.findByPrincipal();
		final ModelAndView result;
		result = new ModelAndView("dashboard/statistics"); // -> list.jsp
		result.addObject("requestURI", "dashboard/admnistrator/statistics.do");

		// Lesson per teacher
		final Double[] lessonTeacherStatistics = this.lessonService.getStatisticsOfLessonsPerTeacher();
		result.addObject("averageLessonPerTeacher", lessonTeacherStatistics[0]);
		result.addObject("minLessonPerTeacher", lessonTeacherStatistics[1]);
		result.addObject("maxLessonPerTeacher", lessonTeacherStatistics[2]);
		result.addObject("desviationLessonPerTeacher", lessonTeacherStatistics[3]);

		// Reservation per lesson
		final Double[] reservationPerLessonStatistics = this.lessonService.getStatisticsOfReservationPerLesson();
		result.addObject("averageReservation", reservationPerLessonStatistics[0]);
		result.addObject("minReservation", reservationPerLessonStatistics[1]);
		result.addObject("maxReservation", reservationPerLessonStatistics[2]);
		result.addObject("desviationReservation", reservationPerLessonStatistics[3]);

		// Lesson price
		final Double[] lessonPrice = this.lessonService.getStatisticsOfLessonPrice();
		result.addObject("averagePrice", lessonPrice[0]);
		result.addObject("minPrice", lessonPrice[1]);
		result.addObject("maxPrice", lessonPrice[2]);
		result.addObject("desviationPrice", lessonPrice[3]);

		// Ratios
		final Double pending = this.reservationService.findPendingReservationRatio();
		final Double accepted = this.reservationService.findAcceptedReservationRatio();
		final Double rejected = this.reservationService.findRejectedReservationRatio();
		final Double finalOverRejected = this.reservationService.findFinalOverRejectedReservationRatio();
		result.addObject("pending", pending);
		result.addObject("accepted", accepted);
		result.addObject("rejected", rejected);
		result.addObject("finalOverRejected", finalOverRejected);

		// Pass exams per student
		final Double[] passExams = this.reservationService.getStatisticsOfPassExams();
		result.addObject("averagePassExams", passExams[0]);
		result.addObject("minPassExams", passExams[1]);
		result.addObject("maxPassExams", passExams[2]);
		result.addObject("desviationPassExams", passExams[3]);

		// Students Ten percent
		final Collection<Student> students = this.studentService.findStudentTenPerCentMoreReservationThanAverage();
		result.addObject("studentsTenPercent", students);

		// Teachers Ten percent
		final Collection<Teacher> teachers = this.teacherService.findTeacherTenPerCentMoreFinalReservationThanAverage();
		result.addObject("teachersTenPercent", teachers);

		// Top-3 students
		final List<Student> topStudents = this.studentService.getStudentsOrderByExamScore();
		result.addObject("topStudents", topStudents);

		// Top-3 teachers
		final List<Teacher> topTeachers = this.teacherService.getTeacherOrderByScore();
		result.addObject("topTeachers", topTeachers);

		// Curriculum ratio
		final Double curriculumRatio = this.curriculumService.findCurriculumRatio();
		result.addObject("curriculumRatio", curriculumRatio);

		// Weekly cost statistics
		final Double[] cost = this.reservationService.getStatisticsOfWeeklyCost();
		result.addObject("averageCost", cost[0]);
		result.addObject("minCost", cost[1]);
		result.addObject("maxCost", cost[2]);
		result.addObject("desviationCost", cost[3]);

		return result;
	}

	@RequestMapping(value = "/dataBreach", method = RequestMethod.GET)
	public ModelAndView launchDeactivate() {
		ModelAndView result;

		this.messageService.dataBreachMessage();

		result = new ModelAndView("welcome/index");
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		String mensaje = null;
		if (lang.equals("en"))
			mensaje = this.configurationParametersService.findWelcomeMessageEn();
		else if (lang.equals("es"))
			mensaje = this.configurationParametersService.findWelcomeMessageEsp();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		final String moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("moment", moment);
		result.addObject("mensaje", mensaje);

		result.addObject("alert", "data.breach.notified");

		return result;

	}

}
