package org.cyk.system.company.business.impl.integration;

import javax.inject.Inject;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public class ActorBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private EmployeeBusiness employeeBusiness;
            
    @Override
    protected void businesses() {
    	installApplication();
    	
    	Employee employee = new Employee();
    	employee.setPerson(RootRandomDataProvider.getInstance().person());
    	employeeBusiness.create(employee); 
    	
    	debug(employee);
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
