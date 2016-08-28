package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.model.payment.Cashier;
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

	@ManyToOne private Cashier cashier;
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
	
	public static final String FIELD_CASHIER = "cashier";
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_FINITE_STATE_MACHINE_STATE = "finiteStateMachineState";
	
	private static final String LOG_FORMAT = Sale.class.getSimpleName()+"(ID=%s|%s Date=%s STATE=%s ATX=%s %s %s CUST=%s %s)";
	
	/**/
	
	public static final String FINITE_STATE_MACHINE_FINAL_STATE_CODE = "FINITE_STATE_MACHINE_FINAL";
	
}
