package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;

public abstract class AbstractEnterpriseResourcePlanningBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;

    @Inject protected EnterpriseResourcePlanningFakedDataProducer dataProducer;
    
    protected void installApplication(){
    	CompanyBusinessLayer.getInstance().enableEnterpriseResourcePlanning();
    }
    
    @Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
    
}
