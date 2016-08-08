package org.cyk.system.company.business.impl.unit;

import java.util.Collection;

import org.cyk.system.company.business.impl.model.AccountingPeriod;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.utility.common.ObjectFieldValues;
import org.junit.Test;
import org.mockito.InjectMocks;

public class InstanciationBusinessUT extends AbstractBusinessUT {

	private static final long serialVersionUID = 124355073578123984L;

	@InjectMocks private SaleBusinessImpl saleBusiness;
	
	@Override
	protected void registerBeans(Collection<Object> collection) {
		super.registerBeans(collection);
		collection.add(saleBusiness);
	}
	
	@Test
	public void sale() {
		ObjectFieldValues objectFieldValues = new ObjectFieldValues(Sale.class)
		.set(Sale.FIELD_FINITE_STATE_MACHINE_STATE,"myfinalstate")
		.setBaseName(Sale.FIELD_COST).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,"12",Cost.FIELD_VALUE, "1500",Cost.FIELD_TAX, "3.18",Cost.FIELD_TURNOVER, "12000")
		.setBaseName(Sale.FIELD_BALANCE).set(Balance.FIELD_VALUE,"20000")
		;
		
		objectFieldValues = new ObjectFieldValues(Sale.class)
		//.setBaseName(Sale.FIELD_FINITE_STATE_MACHINE_STATE).set(FiniteStateMachineState.FIELD_CODE,"dfg")
		//.setBaseName(Sale.FIELD_COST).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover)
		//.setBaseName(Sale.FIELD_BALANCE).set(Balance.FIELD_VALUE,balance)
		;
		
		
	}
	
	@Test
	public void accountingPeriod() {
		//ObjectFieldValues objectFieldValues = new ObjectFieldValues(AccountingPeriod.class)
		//.set(AccountingPeriod.FIELD_CLOSED,"myfinalstate");
	}
	
	@Test
	public void ownedCompany() {
		//ObjectFieldValues objectFieldValues = new ObjectFieldValues(AccountingPeriod.class)
		//.set(AccountingPeriod.FIELD_CLOSED,"myfinalstate");
	}
	
	/**/
	
	

}
