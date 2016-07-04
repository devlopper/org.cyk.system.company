package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import javax.persistence.NoResultException;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.search.AbstractFieldValueSearchCriteriaSet;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SalableProductInstanceCashRegisterDaoImpl extends AbstractTypedDao<SalableProductInstanceCashRegister> implements SalableProductInstanceCashRegisterDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySalableProductInstance,readBySalableProductInstanceByCashRegister,readByCriteria,countByCriteria
		,readBySalableProductInstanceByFiniteStateMachineState;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySalableProductInstance, _select().where(SalableProductInstanceCashRegister.FIELD_SALABLE_PRODUCT_INSTANCE));
		registerNamedQuery(readBySalableProductInstanceByCashRegister, _select().where(SalableProductInstanceCashRegister.FIELD_SALABLE_PRODUCT_INSTANCE)
				.and(SalableProductInstanceCashRegister.FIELD_CASH_REGISTER));
		registerNamedQuery(readBySalableProductInstanceByFiniteStateMachineState, _select().where(SalableProductInstanceCashRegister.FIELD_SALABLE_PRODUCT_INSTANCE)
				.and(SalableProductInstanceCashRegister.FIELD_FINITE_STATE_MACHINE_STATE));
		registerNamedQuery(readByCriteria, "SELECT r1 FROM SalableProductInstanceCashRegister r1 WHERE r1.cashRegister.identifier IN :cashRegisters "
				+ " AND r1.finiteStateMachineState.identifier IN :finiteStateMachineStates");
	}
	
	@Override
	public SalableProductInstanceCashRegister readBySalableProductInstanceByCashRegister(SalableProductInstance salableProductInstance,CashRegister cashRegister) {
		return namedQuery(readBySalableProductInstanceByCashRegister).parameter(SalableProductInstanceCashRegister.FIELD_SALABLE_PRODUCT_INSTANCE, salableProductInstance)
				.parameter(SalableProductInstanceCashRegister.FIELD_CASH_REGISTER, cashRegister).ignoreThrowable(NoResultException.class).resultOne();
	}
	
	@Override
	public Collection<SalableProductInstanceCashRegister> readBySalableProductInstance(SalableProductInstance salableProductInstance) {
		return namedQuery(readBySalableProductInstance).parameter(SalableProductInstanceCashRegister.FIELD_SALABLE_PRODUCT_INSTANCE, salableProductInstance)
				.resultMany();
	}
	
	@Override
	public Collection<SalableProductInstanceCashRegister> readBySalableProductInstanceByFiniteStateMachineState(SalableProductInstance salableProductInstance,
			FiniteStateMachineState finiteStateMachineState) {
		return namedQuery(readBySalableProductInstanceByFiniteStateMachineState).parameter(SalableProductInstanceCashRegister.FIELD_SALABLE_PRODUCT_INSTANCE, salableProductInstance)
				.parameter(SalableProductInstanceCashRegister.FIELD_FINITE_STATE_MACHINE_STATE, finiteStateMachineState).resultMany();
	}
	
	@Override
	protected void applySearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractFieldValueSearchCriteriaSet searchCriteria) {
		super.applySearchCriteriaParameters(queryWrapper, searchCriteria);
		queryWrapper.parameterIdentifiers("cashRegisters",((SearchCriteria)searchCriteria).getCashRegisters());
		queryWrapper.parameterIdentifiers("finiteStateMachineStates",((SearchCriteria)searchCriteria).getFiniteStateMachineStates());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SalableProductInstanceCashRegister> readByCriteria(SearchCriteria searchCriteria) {
		String queryName = readByCriteria;
		QueryWrapper<?> queryWrapper = namedQuery(queryName);
		applySearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Collection<SalableProductInstanceCashRegister>) queryWrapper.resultMany();
	}

	@Override
	public Long countByCriteria(SearchCriteria searchCriteria) {
		QueryWrapper<?> queryWrapper = countNamedQuery(countByCriteria);
		applySearchCriteriaParameters(queryWrapper, searchCriteria);
		return (Long) queryWrapper.resultOne();
	}

	

}
