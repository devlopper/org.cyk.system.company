package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The exchange of a commodity or money as the price of a good or a service.<br/>
 * A transaction between two parties where the buyer receives goods (tangible or intangible)
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @MappedSuperclass
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class AbstractSale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne protected Customer customer;
	@ManyToOne protected SalableProductCollection salableProductCollection;
	
	/**/
	
	public AccountingPeriod getAccountingPeriod() {
		return salableProductCollection==null ? null : salableProductCollection.getAccountingPeriod();
	}

	public Boolean getAutoComputeValueAddedTax() {
		return salableProductCollection==null ? null :salableProductCollection.getAutoComputeValueAddedTax();
	}
	
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
	
	private static final String LOG_FORMAT = AbstractSale.class.getSimpleName()+"(ID=%s|%s Date=%s STATE=%s ATX=%s %s %s CUST=%s %s)";
	
	/**/
	
	public static final String FINITE_STATE_MACHINE_FINAL_STATE_CODE = "FINITE_STATE_MACHINE_FINAL";
	
}
