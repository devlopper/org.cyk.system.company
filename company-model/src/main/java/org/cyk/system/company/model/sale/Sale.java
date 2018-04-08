package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@ModelBean(genderType=GenderType.FEMALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class Sale extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT_STORE_COLLECTION) protected SalableProductStoreCollection salableProductStoreCollection;
	
	@Transient private Customer customer;
	@Transient private MovementCollection balanceMovementCollection;
	
	public Sale setBalanceMovementCollectionValue(BigDecimal value){
		balanceMovementCollection.setValue(value);
		return this;
	}
	
	/**/
	
	public static final String FIELD_CUSTOMER = "customer";
	public static final String FIELD_SALABLE_PRODUCT_STORE_COLLECTION = "salableProductStoreCollection";
	public static final String FIELD_BALANCE_MOVEMENT_COLLECTION = "balanceMovementCollection";
	
	/**/
	
	public static final String COLUMN_SALABLE_PRODUCT_STORE_COLLECTION = FIELD_SALABLE_PRODUCT_STORE_COLLECTION;
	
	/**/
	
}
