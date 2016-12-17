/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.kaushik.testsql2.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node; 
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.api.resource.LoginException;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory; 
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 


@Model(adaptables=Resource.class)
public class HelloWorldModel {

    @Inject
    private SlingSettingsService settings;

    @Inject @Named("sling:resourceType") @Default(values="No resourceType")
    protected String resourceType;

    private String message;
    
    @Inject
    private ResourceResolverFactory resolverFactory;
    
    private Session session;

    @PostConstruct
    protected void init() {
        message = "\tHello World!\n";
        message += "\tThis is instance: " + settings.getSlingId() + "\n";
        message += "\tResource type is: " + resourceType + "\n";
        message += "\tRajnish Kaushik\n";
        
        
        ResourceResolver resourceResolver;
        String querystmt = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s,'/content/mywebsite')";
    	// "AND CONTAINS (s.countrylongname, 'Brazil') AND CONTAINS (s.countryshortname, 'Brazil')";
        QueryManager manager;
        Query query;
        QueryResult results = null;
        
        NodeIterator nitr;
        List<Node> nodeList = new ArrayList<Node>();
        long counter = 0;
    	
    	try {
			resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
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
				
				for (Node node : nodeList) {
		    		message += "\t" + node.getPath() + "\n";
		    	}
				
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (LoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    }

    public String getMessage() {
        return message;
    }
}
