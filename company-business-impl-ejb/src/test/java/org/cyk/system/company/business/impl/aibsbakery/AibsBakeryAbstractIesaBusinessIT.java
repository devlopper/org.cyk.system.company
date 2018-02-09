package org.cyk.system.company.business.impl.aibsbakery;

import javax.inject.Inject;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;

public abstract class AibsBakeryAbstractIesaBusinessIT extends AbstractBusinessIT {

	private static final long serialVersionUID = -5752455124275831171L;

    @Inject protected AibsBakeryFakedDataProducer dataProducer;
    
}
