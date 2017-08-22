package org.cyk.system.company.business.impl.aibsbakery;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.root.business.impl.AbstractFakedDataProducer;

public abstract class AibsBakeryAbstractIesaBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;

    @Inject protected AibsBakeryFakedDataProducer dataProducer;
    
    @Override
    protected AbstractFakedDataProducer getFakedDataProducer() {
    	return dataProducer;
    }
    
}
