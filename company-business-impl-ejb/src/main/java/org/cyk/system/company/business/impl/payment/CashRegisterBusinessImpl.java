package org.cyk.system.company.business.impl.payment;

import java.io.Serializable;

import javax.inject.Inject;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.persistence.api.payment.CashRegisterDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;

public class CashRegisterBusinessImpl extends AbstractTypedBusinessService<CashRegister, CashRegisterDao> implements CashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public CashRegisterBusinessImpl(CashRegisterDao dao) {
		super(dao);
	}
	
	@Override
	public CashRegister create(CashRegister cashRegister) {
		RootBusinessLayer.getInstance().getMovementCollectionBusiness().create(cashRegister.getMovementCollection());
		return super.create(cashRegister);
	}
	
}
