package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;

@Stateless
public class SalableProductInstanceCashRegisterBusinessImpl extends AbstractTypedBusinessService<SalableProductInstanceCashRegister, SalableProductInstanceCashRegisterDao> implements SalableProductInstanceCashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductInstanceCashRegisterBusinessImpl(SalableProductInstanceCashRegisterDao dao) {
		super(dao);
	}
	
	
	
}
