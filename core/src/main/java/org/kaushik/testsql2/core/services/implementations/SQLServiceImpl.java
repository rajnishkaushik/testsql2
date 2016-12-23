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
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.apache.sling.jcr.api.SlingRepository;
import org.kaushik.testsql2.core.services.interfaces.SQLService;

@Component
@Service
public class SQLServiceImpl implements SQLService {

	@Reference
	private SlingRepository slingRepository;

	private Session session;

	@Override
	public String SearchWithSQL2() {

		// ResourceResolver resourceResolver;
		String message = "";
		String querystmt = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s,'/content/energy')";
		// "AND CONTAINS (s.VAR1, 'VAR_Value') AND CONTAINS (s.VAR2,
		// 'VAR_Value')";
		QueryManager manager;
		Query query;
		QueryResult results = null;

		NodeIterator nitr;
		List<Node> nodeList = new ArrayList<Node>();
		long counter = 0;

		try {
			session = slingRepository.loginService("limited-reader-viewtrol-subservice", null);

			if (session != null) {
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

					try {
						writer.object();
						writer.key("node-counter");
						writer.value(counter);

						writer.key("nodes");
						writer.array();

						for (Node node : nodeList) {
							message += "\t" + node.getPath() + "\n";
							writer.object();
							writer.key("node-path");
							writer.value(node.getPath());
							writer.endObject();
						}
						writer.endArray();
						writer.endObject();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					message = sw.toString();

				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (javax.jcr.LoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// close the session
		session.logout();

		return message;
	}

}
