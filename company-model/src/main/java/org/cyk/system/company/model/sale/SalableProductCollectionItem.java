package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

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
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.InstanceHelper;
import org.cyk.utility.common.helper.NumberHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SalableProductCollectionItem extends AbstractCollectionItem<SalableProductCollection> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT) @NotNull @Accessors(chain=true) private SalableProduct salableProduct;
	/**
	 * positive is out. negative is back in
	 */
	//@Deprecated
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantityf;//TODO use number of proceed elements of cost to track this value
	/*
	 *we can use metric to track those values 
	 */
	/*
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal reduction=BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal commission = BigDecimal.ZERO;
	*/
	@Embedded private Cost cost;
	
	@Embedded private Balance balance = new Balance();
	
	@Transient private BigDecimal quantifiedPrice;
	@Transient private Collection<SaleProductInstance> instances;
	@Transient private IdentifiableRuntimeCollection<SalableProductCollectionItemSaleCashRegisterMovement> salableProductCollectionItemSaleCashRegisterMovements = new IdentifiableRuntimeCollection<>();
	@Transient private Boolean productQuantity;
	
	@Override
	public SalableProductCollectionItem setCollection(SalableProductCollection collection) {
		return (SalableProductCollectionItem) super.setCollection(collection);
	}
	
	@Override
	public SalableProductCollectionItem setCollectionFromCode(String code) {
		return setCollection(InstanceHelper.getInstance().getByIdentifier(SalableProductCollection.class, code, ClassHelper.Listener.IdentifierType.BUSINESS));
	}
	
	public SalableProductCollectionItem setSalableProductFromCode(String code) {
		return setSalableProduct(InstanceHelper.getInstance().getByIdentifier(SalableProduct.class, code, ClassHelper.Listener.IdentifierType.BUSINESS));
	}
	
	public Cost getCost(){
		if(this.cost == null)
			this.cost = new Cost();
		return this.cost;
	}
	
	public BigDecimal getQuantifiedPrice(){
		if(quantifiedPrice==null && salableProduct!=null && salableProduct.getPrice()!=null && getCost().getNumberOfProceedElements()!=null)
			quantifiedPrice = salableProduct.getPrice().multiply(getCost().getNumberOfProceedElements());
		return quantifiedPrice;
	}
	
	public Collection<SaleProductInstance> getInstances(){
		if(instances==null)
			instances = new ArrayList<>();
		return instances;
	}
	
	public SalableProductCollectionItem setCostCommission(Object commission) {
		getCost().setCommission(NumberHelper.getInstance().get(BigDecimal.class, commission, null));
		return this;
	}

	public SalableProductCollectionItem setCostNumberOfProceedElements(Object numberOfProceedElements) {
		getCost().setNumberOfProceedElements(NumberHelper.getInstance().get(BigDecimal.class, numberOfProceedElements, null));
		return this;
	}

	public SalableProductCollectionItem setCostReduction(Object reduction) {
		getCost().setReduction(NumberHelper.getInstance().get(BigDecimal.class, reduction, null));
		return this;
	}

	public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
	//@Deprecated public static final String FIELD_QUANTITY = "quantity";
	//public static final String FIELD_REDUCTION = "reduction";
	//public static final String FIELD_COMMISSION = "commission";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	
	public static final String COLUMN_SALABLE_PRODUCT = FIELD_SALABLE_PRODUCT;
	
}
