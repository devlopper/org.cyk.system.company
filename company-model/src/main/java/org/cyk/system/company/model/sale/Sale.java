package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractSale implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Embedded private Balance balance = new Balance();
	@OneToOne private CashRegisterMovementTermCollection cashRegisterMovementTermCollection;
	
	/**/
	
	@Transient private Collection<SaleCashRegisterMovement> saleCashRegisterMovements = new ArrayList<>();
	
	/**/
	/*
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,identifier,computedIdentifier,date,finiteStateMachineState==null?Constant.EMPTY_STRING:finiteStateMachineState.getCode(),autoComputeValueAddedTax,cost.getLogMessage(),balance.getLogMessage()
				,customer==null?"":customer.getCode(),accountingPeriod.getLogMessage());
	}
	
	@Override
	public String getUiString() {
		return StringUtils.defaultString(getComputedIdentifier(), getIdentifier().toString());
	}
	*/
	/**/
	
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_CASH_REGISTER_MOVEMENT_TERM_COLLECTION = "cashRegisterMovementTermCollection";
	
	//private static final String LOG_FORMAT = Sale.class.getSimpleName()+"(ID=%s|%s Date=%s STATE=%s ATX=%s %s %s CUST=%s %s)";
	
	/**/
	
	@Getter @Setter
	public static class SearchCriteria extends AbstractSale.SearchCriteria implements Serializable {

		private static final long serialVersionUID = 6796076474234170332L;

		private Collection<BalanceType> balanceTypes = new ArrayList<>();
		private Boolean hasAtLeastOneCashRegisterMovement;
		
		public SearchCriteria(){
			this(null,null,null);
		}
		
		public SearchCriteria(Date fromDate,Date toDate) {
			this(fromDate,toDate,null);
		}
		
		public SearchCriteria(Date fromDate,Date toDate,BalanceType balanceType) {
			super(fromDate,toDate);
			if(balanceType!=null)
				this.balanceTypes.add(balanceType);
		}
		/*
		public SearchCriteria(SearchCriteria criteria){
			super(criteria);
			balanceTypes = new ArrayList<>(criteria.balanceTypes);
			customers = new ArrayList<>(criteria.customers);
			hasAtLeastOneCashRegisterMovement = criteria.hasAtLeastOneCashRegisterMovement;
			finiteStateMachineStates = new ArrayList<>(criteria.finiteStateMachineStates);
		}
		
		public SearchCriteria(SearchCriteria criteria){
			super(criteria);
		}*/
		
	}

}
