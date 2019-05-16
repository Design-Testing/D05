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
		<spring:message code="dashboard.positions.company" />
		<tr>
			<td><spring:message code="average.positions.company" /></td>
			<td>${averagePositions}</td>
		</tr>
		<tr>
			<td><spring:message code="min.positions.company" /></td>
			<td>${minPositions}</td>
		</tr>
		<tr>
			<td><spring:message code="max.positions.company" /></td>
			<td>${maxPositions}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.positions.company" /></td>
			<td>${desviationPositions}</td>
		</tr>
		<jstl:choose>
			<jstl:when test="${companiesMorePositions.size() eq 1}">
				<tr>
					<td><spring:message code="largest.company" /></td>
					<td>${companyMorePositions}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${companiesMorePositions}" var="c"
					varStatus="in">
					<tr>
						<td><spring:message code="largest.company" /></td>
						<td>${companyMorePositions}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${companiesMorePositions.size() gt 1}">
		<p>
			<spring:message code="largest.company.two" />
		<p />
	</jstl:if>

	<table>
		<spring:message code="dashboard.company.score" />
		<tr>
			<td><spring:message code="average.company.score" /></td>
			<td>${averageCompanyScore}</td>
		</tr>
		<tr>
			<td><spring:message code="min.company.score" /></td>
			<td>${minCompanyScore}</td>
		</tr>
		<tr>
			<td><spring:message code="max.company.score" /></td>
			<td>${maxCompanyScore}</td>
		</tr>
		<tr>
			<td><spring:message code="desviation.company.score" /></td>
			<td>${desviationCompanyScore}</td>
		</tr>
		<jstl:choose>
			<jstl:when test="${companiesHighestScore.size() eq 1}">
				<tr>
					<td><spring:message code="largest.company.score" /></td>
					<td>${companiesHighestScore}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${companiesHighestScore}" var="cscore"
					varStatus="in">
					<tr>
						<td><spring:message code="largest.company.score" />
							${in.index + 1}</td>
						<td>${cscore}</td>
					</tr>
				</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	<jstl:if test="${companiesHighestScore.size() gt 1}">
		<p>
			<spring:message code="largest.company.score.two" />
		<p />
	</jstl:if>

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
