package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;

public abstract class AbstractEnterpriseResourcePlanningBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;

    protected void installApplication(){
    	CompanyBusinessLayer.getInstance().installApplication(); 
    	CompanyBusinessLayer.getInstance().enableEnterpriseResourcePlanning();
    }
   
}
