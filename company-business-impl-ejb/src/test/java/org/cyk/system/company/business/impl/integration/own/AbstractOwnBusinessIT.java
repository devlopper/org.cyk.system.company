package org.cyk.system.company.business.impl.integration.own;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;

public abstract class AbstractOwnBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;

    @Inject protected OwnFakedDataProducer dataProducer;
     
    protected void installApplication(Boolean fake){
    	super.installApplication(fake);
    }
    
    @Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
    
}
