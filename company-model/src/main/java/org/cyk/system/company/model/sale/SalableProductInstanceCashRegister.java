package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.search.AbstractFieldValueSearchCriteriaSet;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class SalableProductInstanceCashRegister extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;
	
	@ManyToOne @NotNull private SalableProductInstance salableProductInstance;
	
	@ManyToOne @NotNull private CashRegister cashRegister;

	@ManyToOne private FiniteStateMachineState finiteStateMachineState;
	
	/**/
	
	@Override
	public String getUiString() {
		return salableProductInstance == null ? super.getUiString() : salableProductInstance.getUiString();
	}
	
	@Override
	public String toString() {
		return salableProductInstance == null ? super.toString() : salableProductInstance.toString();
	}
	
	/**/
	
	public static final String FIELD_SALABLE_PRODUCT_INSTANCE = "salableProductInstance";
	public static final String FIELD_CASH_REGISTER = "cashRegister";
	public static final String FIELD_FINITE_STATE_MACHINE_STATE = "finiteStateMachineState";
	
	/**/
	
	@Getter @Setter
	public static class SearchCriteria extends AbstractFieldValueSearchCriteriaSet implements Serializable {

		private static final long serialVersionUID = 6796076474234170332L;

		private Collection<CashRegister> cashRegisters = new ArrayList<>();
		private Collection<FiniteStateMachineState> finiteStateMachineStates = new ArrayList<>();
		
		public SearchCriteria addCashRegisters(Collection<CashRegister> cashRegisters){
			this.cashRegisters.addAll(cashRegisters);
			return this;
		}
		
		public SearchCriteria addFiniteStateMachineStates(Collection<FiniteStateMachineState> finiteStateMachineStates){
			this.finiteStateMachineStates.addAll(finiteStateMachineStates);
			return this;
		}
	}
}
