package org.cyk.system.company.business.impl.sale; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.sale.SalableProductInstanceCashRegisterBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceDao;
import org.cyk.system.root.business.api.mathematics.machine.FiniteStateMachineStateIdentifiableGlobalIdentifierBusiness;
import org.cyk.system.root.business.impl.AbstractTypedBusinessService;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;

public class SalableProductInstanceCashRegisterBusinessImpl extends AbstractTypedBusinessService<SalableProductInstanceCashRegister, SalableProductInstanceCashRegisterDao> implements SalableProductInstanceCashRegisterBusiness,Serializable {

	private static final long serialVersionUID = -7830673760640348717L;

	@Inject private SalableProductInstanceDao salableProductInstanceDao;
	
	@Inject
	public SalableProductInstanceCashRegisterBusinessImpl(SalableProductInstanceCashRegisterDao dao) {
		super(dao);
	}
	
	@Override @TransactionAttribute(TransactionAttributeType.NEVER)
	public Collection<SalableProductInstanceCashRegister> findByCriteria(SearchCriteria searchCriteria) {
		//prepareFindByCriteria(searchCriteria);
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
		SalableProductInstanceCashRegister inDatabase = dao.readBySalableProductInstanceByCashRegister(salableProductInstanceCashRegister.getSalableProductInstance()
				, salableProductInstanceCashRegister.getCashRegister());
		exceptionUtils().exception(inDatabase!=null, "exception.SalableProductInstanceCashRegisterAlreadyExists");
		salableProductInstanceCashRegister = super.create(salableProductInstanceCashRegister);
		inject(FiniteStateMachineStateIdentifiableGlobalIdentifierBusiness.class).create(salableProductInstanceCashRegister,salableProductInstanceCashRegister.getFiniteStateMachineState());
		return salableProductInstanceCashRegister;
	}
	
	@Override
	public SalableProductInstanceCashRegister update(SalableProductInstanceCashRegister salableProductInstanceCashRegister) {
		FiniteStateMachineState finiteStateMachineState = salableProductInstanceCashRegister.getFiniteStateMachineState();
		salableProductInstanceCashRegister = super.update(salableProductInstanceCashRegister);
		inject(FiniteStateMachineStateIdentifiableGlobalIdentifierBusiness.class).create(salableProductInstanceCashRegister,finiteStateMachineState);
		return salableProductInstanceCashRegister;
	}

	@Override
	public Collection<SalableProductInstanceCashRegister> create(Collection<String> salableProductInstanceCodes, CashRegister cashRegister,FiniteStateMachineState finiteStateMachineState) {
		Collection<SalableProductInstance> salableProductInstances = salableProductInstanceDao.read(salableProductInstanceCodes);
		Collection<SalableProductInstanceCashRegister> salableProductInstanceCashRegisters = new ArrayList<>();
		for(String code : salableProductInstanceCodes){
			SalableProductInstanceCashRegister salableProductInstanceCashRegister = new SalableProductInstanceCashRegister();
			salableProductInstanceCashRegister.setProcessing(cashRegister.getProcessing());
			salableProductInstanceCashRegister.setCashRegister(cashRegister);
			finiteStateMachineState.setProcessing(cashRegister.getProcessing());
			salableProductInstanceCashRegister.setFiniteStateMachineState(finiteStateMachineState);
			for(SalableProductInstance salableProductInstance : salableProductInstances)
				if(salableProductInstance.getCode().equals(code)){
					salableProductInstanceCashRegister.setSalableProductInstance(salableProductInstance);
					break;
				}
			salableProductInstanceCashRegisters.add(salableProductInstanceCashRegister);
		}
		create(salableProductInstanceCashRegisters);
		return salableProductInstanceCashRegisters;
	}

	@Override
	public Collection<SalableProductInstanceCashRegister> create(Collection<String> salableInstanceCodes, CashRegister cashRegister) {
		return create(salableInstanceCodes, cashRegister,inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine().getInitialState());
	}
	
}
