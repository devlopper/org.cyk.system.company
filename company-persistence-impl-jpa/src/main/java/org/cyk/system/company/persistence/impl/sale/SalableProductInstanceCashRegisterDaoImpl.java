package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister.SearchCriteria;
import org.cyk.system.company.persistence.api.sale.SalableProductInstanceCashRegisterDao;
import org.cyk.system.root.model.search.AbstractFieldValueSearchCriteriaSet;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;
import org.cyk.system.root.persistence.impl.QueryWrapper;

public class SalableProductInstanceCashRegisterDaoImpl extends AbstractTypedDao<SalableProductInstanceCashRegister> implements SalableProductInstanceCashRegisterDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readByCriteria,countByCriteria;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByCriteria, "SELECT r1 FROM SalableProductInstanceCashRegister r1 WHERE r1.cashRegister.identifier IN :cashRegisters "
				+ " AND r1.finiteStateMachineState.identifier IN :finiteStateMachineStates");
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
