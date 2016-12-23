<%@include file="/libs/foundation/global.jsp"%>


<%@ page import="org.kaushik.testsql2.core.services.interfaces.SQLService"%>
<%@ page import="org.apache.sling.jcr.api.SlingRepository"%>
<%@ page import="org.apache.sling.commons.json.io.JSONWriter"%>

<h2>Service queryservice.jsp counts # of nodes under'/content/mywebsite'</h2>

<%
	org.kaushik.testsql2.core.services.interfaces.SQLService sqlService = sling
			.getService(org.kaushik.testsql2.core.services.interfaces.SQLService.class);

	String jsonString = sqlService.SearchWithSQL2();

	out.println("# of records returned: </br>" + jsonString);
%>
