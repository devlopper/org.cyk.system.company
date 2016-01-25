package org.cyk.system.company.business.impl.integration.own;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;

public class OwnApplicationSetupBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Inject private OwnFakedDataProducer dataProducer;
    
    @Override
    protected void businesses() {
    	System.exit(0);
    }
    
    @Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
        
}
