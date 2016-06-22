package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

@Stateless
public class SalableProductInstanceCashRegisterBusinessImpl extends AbstractTypedBusinessService<SalableProductInstanceCashRegister, SalableProductInstanceCashRegisterDao> implements SalableProductInstanceCashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject
	public SalableProductInstanceCashRegisterBusinessImpl(SalableProductInstanceCashRegisterDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SalableProductInstanceCashRegister> findByCriteria(SearchCriteria searchCriteria) {
		prepareFindByCriteria(searchCriteria);
		return dao.readByCriteria(searchCriteria);
	}

	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Long countByCriteria(SearchCriteria searchCriteria) {
		return dao.countByCriteria(searchCriteria);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public SalableProductInstanceCashRegister findBySalableProductInstanceByCashRegister(SalableProductInstance salableProductInstance, CashRegister cashRegister) {
		return dao.readBySalableProductInstanceByCashRegister(salableProductInstance,cashRegister);
	}
	
	@Override
	public SalableProductInstanceCashRegister create(SalableProductInstanceCashRegister salableProductInstanceCashRegister) {
		salableProductInstanceCashRegister = super.create(salableProductInstanceCashRegister);
		RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness().create(salableProductInstanceCashRegister,salableProductInstanceCashRegister.getFiniteStateMachineState());
		return salableProductInstanceCashRegister;
	}
	
	@Override
	public SalableProductInstanceCashRegister update(SalableProductInstanceCashRegister salableProductInstanceCashRegister) {
		FiniteStateMachineState finiteStateMachineState = salableProductInstanceCashRegister.getFiniteStateMachineState();
		salableProductInstanceCashRegister = super.update(salableProductInstanceCashRegister);
		RootBusinessLayer.getInstance().getFiniteStateMachineStateLogBusiness().create(salableProductInstanceCashRegister,finiteStateMachineState);
		return salableProductInstanceCashRegister;
	}
	
}
