package org.cyk.system.company.business.impl.integration;

import java.util.Date;

import javax.inject.Inject;

import org.cyk.system.root.business.api.party.ApplicationBusiness;
import org.cyk.system.root.model.party.Application;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.Credentials;
import org.cyk.system.root.model.security.Installation;
import org.cyk.system.root.model.security.License;
import org.cyk.system.root.model.time.Period;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
        return createRootDeployment();
    } 
    
    @Inject private ApplicationBusiness applicationBusiness;
    
    private Installation installation;
    
    @Override
    protected void populate() {
    	installation = new Installation();
    	installation.setAdministratorCredentials(new Credentials("admin", "123"));
    	installation.setApplication(new Application());
    	installation.getApplication().setName("app");
    	installation.setLicense(new License());
    	installation.getLicense().setPeriod(new Period(new Date(), new Date()));
    	installation.setManager(new Person("fn","ln"));
    	installation.setManagerCredentials(new Credentials("man", "123"));
    }
    
    @Override
    protected void businesses() {
    	applicationBusiness.install(installation);
    }

   
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
