package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.structure.EmployeeBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.root.business.impl.RootBusinessLayer;

public class LookupIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
            
    @Override
    protected void businesses() {
    	assertThat("Employee injected", RootBusinessLayer.getInstance().inject(EmployeeBusiness.class)!=null);
    	assertThat("Employee injected", CompanyBusinessLayer.getInstance().inject(EmployeeBusiness.class)!=null);
    }
    
   
    

}
