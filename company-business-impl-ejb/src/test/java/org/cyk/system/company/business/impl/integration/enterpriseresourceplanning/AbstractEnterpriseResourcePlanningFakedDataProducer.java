package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.impl.AbstractCompanyFakedDataProducer;

import lombok.Getter;

@Getter
public abstract class AbstractEnterpriseResourcePlanningFakedDataProducer extends AbstractCompanyFakedDataProducer implements Serializable {

	private static final long serialVersionUID = -1832900422621121762L;
	
	@Override
	protected void structure() {
		create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_003));
    	
	}

	@Override
	protected void doBusiness(Listener listener) {
		create(inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_001)); //FIXME throw exception when called in structure
    	create(inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_002));
	}

	
}
