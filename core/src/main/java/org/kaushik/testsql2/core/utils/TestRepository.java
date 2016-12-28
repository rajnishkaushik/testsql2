package org.kaushik.testsql2.core.utils;

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

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.apache.sling.jcr.api.SlingRepository;

public class TestRepository {

	public String GetJCRTreeNodes(Session session, String mypath) {
		String message = "";
		String nodesJson = "";
		String querystmt = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s, '" + mypath + "')";
		// "AND CONTAINS (s.VAR1, 'VAR_Value') AND CONTAINS (s.VAR2, 'VAR_Value')";
		QueryManager manager;
		Query query;
		QueryResult results = null;

		NodeIterator nitr;
		List<Node> nodeList = new ArrayList<Node>();
		long counter = 0;

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
				message = "\t# of records returned:" + counter + "\n";

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

				nodesJson = sw.toString();

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return nodesJson;
	}
}
