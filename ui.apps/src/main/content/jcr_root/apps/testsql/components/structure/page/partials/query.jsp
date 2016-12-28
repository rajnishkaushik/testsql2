<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="javax.jcr.RepositoryException"%>
<%@ page import="javax.jcr.query.InvalidQueryException"%>
<%@ page import="javax.jcr.query.Query"%>
<%@ page import="javax.jcr.query.QueryManager"%>
<%@ page import="javax.jcr.query.QueryResult"%>


<%@ page import="org.apache.sling.jcr.api.SlingRepository"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="javax.jcr.Session"%>

<%@ page import="org.apache.sling.api.resource.LoginException"%>
<%@ page import="org.apache.sling.api.resource.Resource"%>
<%@ page import="org.apache.sling.api.resource.ResourceResolver"%>
<%@ page import="org.apache.sling.api.resource.ResourceResolverFactory"%>

<h2>query.jsp counts # of nodes under '/content/testsql'</h2>

<%
	Session session = resourceResolver.adaptTo(Session.class);
	QueryManager manager = session.getWorkspace().getQueryManager();

	String querystmt = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s,'/content/testsql')";
	
	Query query = manager.createQuery(querystmt, Query.JCR_SQL2);
	QueryResult results = null;

	results = query.execute();

	List<Node> nodeList = new ArrayList<Node>();
	NodeIterator nitr = results.getNodes();
	while (nitr.hasNext()) {
		Node node = nitr.nextNode();
		nodeList.add(node);
	}

	long counter = results.getRows().getSize();

	out.println("# of records returned: " + counter + "</br>");

	for (Node node : nodeList) {
		out.println(node.getPath() + "</br>");
	}
	
%>
