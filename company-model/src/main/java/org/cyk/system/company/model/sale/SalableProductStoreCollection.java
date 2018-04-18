package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.system.root.model.party.Store;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class SalableProductStoreCollection extends AbstractCollection<SalableProductStoreCollectionItem> implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@Embedded private Cost cost;

	@Accessors(chain=true) private Boolean isStockMovementCollectionUpdatable;
	@Accessors(chain=true) private Boolean isBalanceMovementCollectionUpdatable;
	
	@Transient private Store store;
	
	/**/
	
	public Cost getCost(){
		if(cost==null)
			cost = new Cost();
		return cost;
	}
	
	@Override
	public SalableProductStoreCollection setItemsSynchonizationEnabled(Boolean synchonizationEnabled) {
		return (SalableProductStoreCollection) super.setItemsSynchonizationEnabled(synchonizationEnabled);
	}
	
	@Override
	public SalableProductStoreCollection add(Collection<SalableProductStoreCollectionItem> items) {
		return (SalableProductStoreCollection) super.add(items);
	}
	
	public static final String FIELD_COST = "cost";
	public static final String FIELD_IS_STOCK_MOVEMENT_COLLECTION_UPDATABLE = "isStockMovementCollectionUpdatable";
	public static final String FIELD_IS_BALANCE_MOVEMENT_COLLECTION_UPDATABLE = "isBalanceMovementCollectionUpdatable";
	public static final String FIELD_STORE = "store";
	
}
