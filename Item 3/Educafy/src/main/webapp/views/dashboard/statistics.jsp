<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	<!-- TABLE -->
	<table>
		<spring:message code="dashboard.lesson.price" />
		<tr>
			<td><spring:message code="average.lesson.price" /></td>
			<td>${averagePrice}</td>
		</tr>
		<tr>
			<td><spring:message code="min.lesson.price" /></td>
			<td>${minPrice}</td>
		</tr>
		<tr>
			<td><spring:message code="max.lesson.price" /></td>
			<td>${maxPrice}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.lesson.price" /></td>
			<td>${desviationPrice}</td>
		</tr>
	</table>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.students.ten.percent" />
		<jstl:choose>
			<jstl:when test="${studentsTenPercent.size() eq 1}">
				<tr>
					<td><spring:message code="students.ten.percent" /></td>
					<td>${studentsTenPercent}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${studentsTenPercent}" var="c" varStatus="in">
					<tr>
						<td><spring:message code="students.ten.percent" /></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${empty studentsTenPercent}">
		<p>
			<spring:message code="students.ten.percent.empty" />
		<p />
	</jstl:if>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.teachers.ten.percent" />
		<jstl:choose>
			<jstl:when test="${teachersTenPercent.size() eq 1}">
				<tr>
					<td><spring:message code="teachers.ten.percent" /></td>
					<td>${teachersTenPercent}</td>
				</tr>
			</jstl:when>
			<jstl:when test="${empty teachersTenPercent}">
				<tr>
					<td><spring:message code="teachers.ten.percent.empty" /></td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${teachersTenPercent}" var="c" varStatus="in">
					<tr>
						<td><spring:message code="teachers.ten.percent" /></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.students.top" />
		<jstl:choose>
			<jstl:when test="${topStudents.size() eq 1}">
				<tr>
					<td><spring:message code="students.top" /></td>
					<td>${topStudents}</td>
				</tr>
			</jstl:when>
			<jstl:when test="${topStudents.size() lt 3}">
				<tr>
					<td><spring:message code="students.top.less" /></td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${topStudents}" var="c" varStatus="in">
					<tr>
						<td><spring:message code="students.top" /> <jstl:out
								value="${in.index + 1}" /></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.teachers.top" />
		<jstl:choose>
			<jstl:when test="${topTeachers.size() eq 1}">
				<tr>
					<td><spring:message code="teachers.top" /></td>
					<td>${topTeachers}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${topTeachers}" var="c" varStatus="in">
					<tr>
						<td><spring:message code="teachers.top" /> <jstl:out
								value="${in.index + 1}" /></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${topTeachers.size() lt 3}">
		<p>
			<spring:message code="teachers.top.less" />
		<p />
	</jstl:if>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.reservation.lesson" />
		<tr>
			<td><spring:message code="average.reservation.lesson" /></td>
			<td>${averageReservation}</td>
		</tr>
		<tr>
			<td><spring:message code="min.reservation.lesson" /></td>
			<td>${minReservation}</td>
		</tr>
		<tr>
			<td><spring:message code="max.reservation.lesson" /></td>
			<td>${maxReservation}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.reservation.lesson" /></td>
			<td>${desviationReservation}</td>
		</tr>
	</table>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.lesson.teacher" />
		<tr>
			<td><spring:message code="average.lesson.teacher" /></td>
			<td>${averageLessonPerTeacher}</td>
		</tr>
		<tr>
			<td><spring:message code="min.lesson.teacher" /></td>
			<td>${minLessonPerTeacher}</td>
		</tr>
		<tr>
			<td><spring:message code="max.lesson.teacher" /></td>
			<td>${maxLessonPerTeacher}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.lesson.teacher" /></td>
			<td>${desviationLessonPerTeacher}</td>
		</tr>
	</table>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.exam.pass" />
		<tr>
			<td><spring:message code="average.exam.pass" /></td>
			<td>${averagePassExams}</td>
		</tr>
		<tr>
			<td><spring:message code="min.exam.pass" /></td>
			<td>${minPassExams}</td>
		</tr>
		<tr>
			<td><spring:message code="max.exam.pass" /></td>
			<td>${maxPassExams}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.exam.pass" /></td>
			<td>${desviationPassExams}</td>
		</tr>
	</table>
	<br>
	<br>
	
	<table>
		<spring:message code="dashboard.cost" />
		<tr>
			<td><spring:message code="average.cost" /></td>
			<td>${averageCost}</td>
		</tr>
		<tr>
			<td><spring:message code="min.cost" /></td>
			<td>${minCost}</td>
		</tr>
		<tr>
			<td><spring:message code="max.cost" /></td>
			<td>${maxCost}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.cost" /></td>
			<td>${desviationCost}</td>
		</tr>
	</table>
	<br>
	<br>

	<table>
		<spring:message code="dashboard.ratios" />
		<tr>
			<td><spring:message code="ratio.pending" /></td>
			<td>${pending}</td>
		</tr>
		<tr>
			<td><spring:message code="ratio.accepted" /></td>
			<td>${accepted}</td>
		</tr>
		<tr>
			<td><spring:message code="ratio.rejected" /></td>
			<td>${rejected}</td>
		</tr>
		<tr>
			<td><spring:message code="ratios.final.over.rejected" /></td>
			<td>${finalOverRejected}</td>
		</tr>
		<tr>
			<td><spring:message code="ratio.curriculum" /></td>
			<td>${curriculumRatio}</td>
		</tr>
	</table>

</security:authorize>
