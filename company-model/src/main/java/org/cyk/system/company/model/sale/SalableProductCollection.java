package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SalableProductCollection extends AbstractCollection<SalableProductCollectionItem> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private AccountingPeriod accountingPeriod;
	
	@Embedded private Cost cost;
	
	/**/
	
	@Transient protected Boolean autoComputeValueAddedTax = Boolean.TRUE;
	//TODO Use map for this
	@Transient private BigDecimal totalCostValueWithoutReduction;
	@Transient private BigDecimal totalReduction;
	
	/**/
	
	public Cost getCost(){
		if(cost==null)
			cost = new Cost();
		return cost;
	}
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_AUTO_COMPUTE_VALUE_ADDED_TAX = "autoComputeValueAddedTax";
}
