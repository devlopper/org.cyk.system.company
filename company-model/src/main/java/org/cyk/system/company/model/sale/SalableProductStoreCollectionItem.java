package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.product.SalableProductStore;
import org.cyk.system.root.model.AbstractCollectionItem;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE) @Accessors(chain=true)
public class SalableProductStoreCollectionItem extends AbstractCollectionItem<SalableProductStoreCollection> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT_STORE) @NotNull private SalableProductStore salableProductStore;
	
	@Embedded private Cost cost;
	
	@Embedded private Balance balance;
	
	@Transient private BigDecimal quantifiedPrice;
	
	@Override
	public SalableProductStoreCollectionItem setCollection(SalableProductStoreCollection collection) {
		return (SalableProductStoreCollectionItem) super.setCollection(collection);
	}
	
	@Override
	public SalableProductStoreCollectionItem setCollectionFromCode(String code) {
		return setCollection(getFromCode(SalableProductStoreCollection.class, code));
	}
	
	public SalableProductStoreCollectionItem setSalableProductStoreFromCode(String code) {
		return setSalableProductStore(getFromCode(SalableProductStore.class, code));
	}
	
	public Cost getCost(){
		if(this.cost == null)
			this.cost = new Cost();
		return this.cost;
	}
	
	public Balance getBalance(){
		if(this.balance == null)
			this.balance = new Balance();
		return this.balance;
	}
	
	public BigDecimal getQuantifiedPrice(){
		if(quantifiedPrice==null && salableProductStore!=null && salableProductStore.getSalableProduct().getPrice()!=null && getCost().getNumberOfProceedElements()!=null)
			quantifiedPrice = salableProductStore.getSalableProduct().getPrice().multiply(getCost().getNumberOfProceedElements());
		return quantifiedPrice;
	}
	
	public SalableProductStoreCollectionItem setCostCommission(Object commission) {
		getCost().setCommission(getNumberFromObject(BigDecimal.class, commission));
		return this;
	}

	public SalableProductStoreCollectionItem setCostNumberOfProceedElements(Object numberOfProceedElements) {
		getCost().setNumberOfProceedElements(getNumberFromObject(BigDecimal.class, numberOfProceedElements));
		return this;
	}

	public SalableProductStoreCollectionItem setCostReduction(Object reduction) {
		getCost().setReduction(getNumberFromObject(BigDecimal.class, reduction));
		return this;
	}

	public static final String FIELD_SALABLE_PRODUCT_STORE = "salableProductStore";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_BALANCE = "balance";
	
	public static final String COLUMN_SALABLE_PRODUCT_STORE = FIELD_SALABLE_PRODUCT_STORE;
	
}
