package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;

public class LookupIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
            
    @Override
    protected void businesses() {
    	assertThat("Employee injected", inject(EmployeeBusiness.class)!=null);
    	assertThat("Employee injected", inject(EmployeeBusiness.class)!=null);
    }
    
   
    

}
