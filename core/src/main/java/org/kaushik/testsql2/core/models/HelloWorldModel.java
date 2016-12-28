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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.settings.SlingSettingsService;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory; 
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 
import org.kaushik.testsql2.core.utils.TestRepository;


@Model(adaptables=Resource.class)
public class HelloWorldModel {

    @Inject
    private SlingSettingsService settings;

    @Inject @Named("sling:resourceType") @Default(values="No resourceType")
    protected String resourceType;
    
    @Reference
	private SlingRepository slingRepository;
    
    @Reference
    private ResourceResolverFactory resolverFactory;

    private String message;
    
    @PostConstruct
    protected void init() {
        message = "\tHello World!\n";
        message += "\tThis is instance: " + settings.getSlingId() + "\n";
        message += "\tResource type is: " + resourceType + "\n";
        message += "\tRajnish Kaushik\n";        
    }

    public String getMessage() {
        return message;
    }
}
