package org.kaushik.testsql2.core.services.implementations;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.io.JSONWriter;
import org.kaushik.testsql2.core.services.interfaces.SQLService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
@Service
public class SQLServiceImpl implements SQLService {

	// Inject a Sling ResourceResolverFactory
	@Reference
	private ResourceResolverFactory resolverFactory;

	private Session session;

	@Override
	public String SearchWithSQL2() {

		ResourceResolver resourceResolver;
		String message = "";
		String querystmt = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s,'/content/mywebsite')";
		// "AND CONTAINS (s.countrylongname, 'Brazil') AND CONTAINS (s.countryshortname, 'Brazil')";
		QueryManager manager;
		Query query;
		QueryResult results = null;

		NodeIterator nitr;
		List<Node> nodeList = new ArrayList<Node>();
		long counter = 0;

		try {
			resourceResolver = resolverFactory
					.getAdministrativeResourceResolver(null);
			session = resourceResolver.adaptTo(Session.class);

			try {
				manager = session.getWorkspace().getQueryManager();
				query = manager.createQuery(querystmt, Query.JCR_SQL2);
				results = query.execute();

				nitr = results.getNodes();

				while (nitr.hasNext()) {
					Node node = nitr.nextNode();
					nodeList.add(node);
				}

				counter = results.getRows().getSize();
				message += "\t# of records returned:" + counter + "\n";
				
				StringWriter sw = new StringWriter();
				JSONWriter writer = new JSONWriter(sw);
				
				
				for (Node node : nodeList) {
					message += "\t" + node.getPath() + "\n";
					
					try {
						writer.object().key("path").value(node.getPath());
						writer.endObject();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				message = sw.toString();

				try {
					// Place the results in XML to return to client
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder builder;
					builder = factory.newDocumentBuilder();

					Document doc = builder.newDocument();

					// Start building the XML to pass back to the AEM client
					Element root = doc.createElement("results");
					doc.appendChild(root);

					/*
					 * // iterating over the results for (Hit hit :
					 * result.getHits()) { String path = hit.getPath(); //
					 * Create a result element Element resultel =
					 * doc.createElement("result"); root.appendChild(resultel);
					 * 
					 * Element pathel = doc.createElement("path");
					 * pathel.appendChild(doc.createTextNode(path));
					 * resultel.appendChild(pathel); }
					 */

					// Convert the XML to a string to return to the web
					//message = convertToString(doc);

				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (LoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// close the session
		session.logout();

		return message;
	}

	private String convertToString(Document xml) {
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(xml);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
