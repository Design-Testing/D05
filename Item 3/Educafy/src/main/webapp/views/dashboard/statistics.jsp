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
				<jstl:forEach items="${studentsTenPercent}" var="c"
					varStatus="in">
					<tr>
						<td><spring:message code="students.ten.percent" /></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	
	<table>
		<spring:message code="dashboard.teachers.ten.percent" />
	<jstl:choose>
			<jstl:when test="${teachersTenPercent.size() eq 1}">
				<tr>
					<td><spring:message code="teachers.ten.percent" /></td>
					<td>${teachersTenPercent}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${teachersTenPercent}" var="c"
					varStatus="in">
					<tr>
						<td><spring:message code="teachers.ten.percent" /></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	
	<table>
		<spring:message code="dashboard.students.top" />
	<jstl:choose>
			<jstl:when test="${studentsTenPercent.size() eq 1}">
				<tr>
					<td><spring:message code="students.top" /></td>
					<td>${studentsTenPercent}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${studentsTenPercent}" var="c"
					varStatus="in">
					<tr>
						<td><spring:message code="students.top" /> <jstl:out value="${in.index + 1}"/></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${studentsTenPercent.size() lt 3}">
		<p>
			<spring:message code="students.top.less" />
		<p />
	</jstl:if>
	
	<table>
		<spring:message code="dashboard.teachers.top" />
	<jstl:choose>
			<jstl:when test="${teachersTenPercent.size() eq 1}">
				<tr>
					<td><spring:message code="teachers.top" /></td>
					<td>${teachersTenPercent}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${teachersTenPercent}" var="c"
					varStatus="in">
					<tr>
						<td><spring:message code="teachers.top" /> <jstl:out value="${in.index + 1}"/></td>
						<td>${c}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${teachersTenPercent.size() lt 3}">
		<p>
			<spring:message code="teachers.top.less" />
		<p />
	</jstl:if>
	
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

	<table>
		<spring:message code="dashboard.position.score" />
		<tr>
			<td><spring:message code="average.position.score" /></td>
			<td>${averagePositionScore}</td>
		</tr>
		<tr>
			<td><spring:message code="min.position.score" /></td>
			<td>${minPositionScore}</td>
		</tr>
		<tr>
			<td><spring:message code="max.position.score" /></td>
			<td>${maxPositionScore}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.position.score" /></td>
			<td>${desviationPositionScore}</td>
		</tr>
		<tr>
			<td><spring:message code="avg.salary.position.score" /></td>
			<td>${avgSalary}</td>
		</tr>
	</table>


	<table>
		<spring:message code="dashboard.applications.rooky" />
		<tr>
			<td><spring:message code="average.applications.rooky" /></td>
			<td>${averageRooky}</td>
		</tr>
		<tr>
			<td><spring:message code="min.applications.rooky" /></td>
			<td>${minRooky}</td>
		</tr>
		<tr>
			<td><spring:message code="max.applications.rooky" /></td>
			<td>${maxRooky}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.applications.rooky" /></td>
			<td>${desviationRooky}</td>
		</tr>
		<tr>
			<td><spring:message code="largest.rooky" /></td>
			<td>${rookyMoreApplications}</td>
		</tr>
	</table>
	<jstl:if test="${rookysMoreApplications.size() gt 1}">
		<p>
			<spring:message code="largest.rooky.two" />
		<p />
	</jstl:if>

	<table>
		<spring:message code="dashboard.positions" />
		<tr>
			<td><spring:message code="largest.position" /></td>
			<td><strong><spring:message code="position" />: <spring:message
						code="position.title" /></strong> ${best.title}, <strong><spring:message
						code="position.ticker" /></strong> ${best.ticker}, <strong><spring:message
						code="position.salary" /></strong> ${best.salary}</td>
		</tr>
		<tr>
			<td><spring:message code="smallest.position" /></td>
			<td><strong><spring:message code="position" />: <spring:message
						code="position.title" /></strong> ${worst.title}, <strong><spring:message
						code="position.ticker" /></strong> ${worst.ticker}, <strong><spring:message
						code="position.salary" /></strong> ${worst.salary}</td>
		</tr>
	</table>

	<table>
		<spring:message code="dashboard.curricula" />
		<tr>
			<td><spring:message code="average.curricula" /></td>
			<td>${averageCurricula}</td>
		</tr>
		<tr>
			<td><spring:message code="min.curricula" /></td>
			<td>${minCurricula}</td>
		</tr>
		<tr>
			<td><spring:message code="max.curricula" /></td>
			<td>${maxCurricula}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.curricula" /></td>
			<td>${desviationCurricula}</td>
		</tr>
	</table>

	<table>
		<spring:message code="dashboard.finders" />
		<tr>
			<td><spring:message code="average.results" /></td>
			<td>${averageResults}</td>
		</tr>
		<tr>
			<td><spring:message code="min.results" /></td>
			<td>${minResults}</td>
		</tr>
		<tr>
			<td><spring:message code="max.results" /></td>
			<td>${maxResults}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.results" /></td>
			<td>${desviationResults}</td>
		</tr>
		<tr>
			<td><spring:message code="ratio.finders" /></td>
			<td>${ratioFinders}</td>
		</tr>
	</table>

	<table>
		<spring:message code="dashboard.sponsorship.provider" />
		<tr>
			<td><spring:message code="average.sponsorship.provider" /></td>
			<td>${averageSponsorshipPerProvider}</td>
		</tr>
		<tr>
			<td><spring:message code="min.sponsorship.provider" /></td>
			<td>${minSponsorshipPerProvider}</td>
		</tr>
		<tr>
			<td><spring:message code="max.sponsorship.provider" /></td>
			<td>${maxSponsorshipPerProvider}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.sponsorship.provider" /></td>
			<td>${desviationSponsorshipPerProvider}</td>
		</tr>
		<jstl:choose>
			<jstl:when test="${providersTenPercent.size() eq 1}">
				<tr>
					<td><spring:message code="provider.ten.percent" /></td>
					<td>${providersTenPercent}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${providersTenPercent}" var="ptp"
					varStatus="in">
					<tr>
						<td><spring:message code="provider.ten.percent" />
							${in.index + 1}</td>
						<td>${ptp}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${providersTenPercent.size() gt 1}">
		<p>
			<spring:message code="provider.ten.percent.two" />
		<p />
	</jstl:if>

	<table>
		<spring:message code="dashboard.sponsorship.position" />
		<tr>
			<td><spring:message code="average.sponsorship.position" /></td>
			<td>${averageSponsorshipPerPosition}</td>
		</tr>
		<tr>
			<td><spring:message code="min.sponsorship.position" /></td>
			<td>${minSponsorshipPerPosition}</td>
		</tr>
		<tr>
			<td><spring:message code="max.sponsorship.position" /></td>
			<td>${maxSponsorshipPerPosition}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.sponsorship.position" /></td>
			<td>${desviationSponsorshipPerPosition}</td>
		</tr>
	</table>

</security:authorize>
