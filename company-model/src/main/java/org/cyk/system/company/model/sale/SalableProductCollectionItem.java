package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.system.root.model.IdentifiableRuntimeCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SalableProductCollectionItem extends AbstractCollectionItem<SalableProductCollection> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT) @NotNull private SalableProduct salableProduct;
	/**
	 * positive is out. negative is back in
	 */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity;
	/*
	 *we can use metric to track those values 
	 */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal reduction=BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal commission = BigDecimal.ZERO;
	
	@Embedded private Cost cost;
	
	@Embedded private Balance balance = new Balance();
	
	@Transient private BigDecimal quantifiedPrice;
	@Transient private Collection<SaleProductInstance> instances;
	@Transient private IdentifiableRuntimeCollection<SalableProductCollectionItemSaleCashRegisterMovement> salableProductCollectionItemSaleCashRegisterMovements = new IdentifiableRuntimeCollection<>();
	@Transient private Boolean productQuantity;
	
	public Cost getCost(){
		if(this.cost == null)
			this.cost = new Cost();
		return this.cost;
	}
	
	public BigDecimal getQuantifiedPrice(){
		if(quantifiedPrice==null && salableProduct.getPrice()!=null && quantity!=null)
			quantifiedPrice = salableProduct.getPrice().multiply(quantity);
		return quantifiedPrice;
	}
	
	public Collection<SaleProductInstance> getInstances(){
		if(instances==null)
			instances = new ArrayList<>();
		return instances;
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,getCode(),getQuantity(),cost.getLogMessage());
	}
	private static final String LOG_FORMAT = SalableProductCollectionItem.class.getSimpleName()+"(C=%s Q=%s %s)";
	
	
	public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
	public static final String FIELD_QUANTITY = "quantity";
	public static final String FIELD_REDUCTION = "reduction";
	public static final String FIELD_COMMISSION = "commission";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	
	public static final String COLUMN_SALABLE_PRODUCT = FIELD_SALABLE_PRODUCT;
	
}
