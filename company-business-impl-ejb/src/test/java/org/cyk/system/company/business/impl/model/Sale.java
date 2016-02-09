package org.cyk.system.company.business.impl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

/**
 * The exchange of a commodity or money as the price of a good or a service.<br/>
 * A transaction between two parties where the buyer receives goods (tangible or intangible)
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter 
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	//@ManyToOne @NotNull private AccountingPeriod accountingPeriod;
	@Column(unique=true) private String computedIdentifier;
	//@ManyToOne @NotNull private Cashier cashier;
	//@ManyToOne private Customer customer;
	
	//@Column private String externalIdentifier;//This value is used to link to another system (Example : Accounting System)
	
	@Embedded private Cost cost = new Cost();
	
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable=false) @NotNull private Date date;
	
	@Embedded private Balance balance = new Balance();
	
	@ManyToOne @NotNull private FiniteStateMachineState finiteStateMachineState;
	
	@OneToOne private File report;
	
	@Column(length=1024*1) private String comments;
	
	@Transient private Boolean autoComputeValueAddedTax = Boolean.TRUE;
	@Transient private Collection<SaleProduct> saleProducts = new ArrayList<>();
	@Transient private Collection<SaleCashRegisterMovement> saleCashRegisterMovements = new ArrayList<>();
	
	public static final String FIELD_COMPUTED_IDENTIFIER = "computedIdentifier";
	public static final String FIELD_CASHIER = "cashier";
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	public static final String FIELD_FINITE_STATE_MACHINE_STATE = "finiteStateMachineState";
	
}
