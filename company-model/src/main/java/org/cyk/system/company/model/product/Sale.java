package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Sale extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private AccountingPeriod accountingPeriod;
	@Column(unique=true,nullable=true) private String computedIdentifier;
	@ManyToOne @NotNull private Cashier cashier;
	@ManyToOne private Customer customer;
	
	//@Column private String externalIdentifier;//This value is used to link to another system (Example : Accounting System)
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal cost = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal valueAddedTax = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal turnover = BigDecimal.ZERO;
	
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable=false) @NotNull private Date date;
	@Embedded private Balance balance = new Balance();
	
	@Column(nullable=false) @NotNull private Boolean done = Boolean.FALSE;
	
	@OneToOne private File report;
	
	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.MERGE,orphanRemoval=true)
	private Set<ProductEmployee> performers = new HashSet<>();
	
	@Column(length=1024*1)
	private String comments;
	
	/**/
	
	@Transient private Boolean completed = Boolean.TRUE;
	@Transient private Boolean autoComputeValueAddedTax;
	@Transient private Collection<SaleProduct> saleProducts = new ArrayList<>();
	@Transient private Collection<SaleCashRegisterMovement> saleCashRegisterMovements = new ArrayList<>();
	
	/**/
	
	@Override
	public String getLogMessage() {
		return String.format(DEBUG_FORMAT,cost,turnover,valueAddedTax,balance.getValue(),balance.getCumul(),identifier,computedIdentifier,completed,done
				,customer==null?"":customer.getRegistration().getCode(),autoComputeValueAddedTax);
	}
	
	/**/
	
	public static final String FIELD_DONE = "done";
	public static final String FIELD_COMPUTED_IDENTIFIER = "computedIdentifier";
	public static final String FIELD_CASHIER = "cashier";
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_TURNOVER = "turnover";
	public static final String FIELD_VALUE_ADDED_TAX = "valueAddedTax";
	public static final String FIELD_BALANCE = "balance";
	
	private static final String DEBUG_FORMAT = "COST=%s TURN=%s VAT=%s BAL=(%s,%s) ID=%s CID=%s COMPLETED=%s DONE=%s CUST=%s AUTO_COMPUTE_VAT=%s";
}
