package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SalableProductCollection extends AbstractCollection<SalableProductCollectionItem> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@Embedded private Cost cost;
	
	@Accessors(chain=true) private Boolean isStockMovementCollectionUpdatable;
	@Accessors(chain=true) private Boolean isBalanceMovementCollectionUpdatable;
	
	/**/
	
	public Cost getCost(){
		if(cost==null)
			cost = new Cost();
		return cost;
	}
	
	@Override
	public SalableProductCollection setItemsSynchonizationEnabled(Boolean synchonizationEnabled) {
		return (SalableProductCollection) super.setItemsSynchonizationEnabled(synchonizationEnabled);
	}
	
	@Override
	public SalableProductCollection add(Collection<SalableProductCollectionItem> items) {
		return (SalableProductCollection) super.add(items);
	}
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_COST = "cost";
	public static final String FIELD_AUTO_COMPUTE_VALUE_ADDED_TAX = "autoComputeValueAddedTax";
	
	public static final String COLUMN_ACCOUNTING_PERIOD = FIELD_ACCOUNTING_PERIOD;
}
