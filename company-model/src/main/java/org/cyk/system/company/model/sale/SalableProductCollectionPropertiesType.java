package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.system.root.model.pattern.tree.AbstractDataTreeType;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
@FieldOverride(name=SalableProductCollectionPropertiesType.FIELD___PARENT__,type=SalableProductCollectionPropertiesType.class)
public class SalableProductCollectionPropertiesType extends AbstractDataTreeType implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	private Boolean stockMovementCollectionUpdatable;
	private Boolean balanceMovementCollectionUpdatable;
	
	/**/
	
	public static final String FIELD_STOCK_MOVEMENT_COLLECTION_UPDATABLE = "stockMovementCollectionUpdatable";
	public static final String FIELD_BALANCE_MOVEMENT_COLLECTION_UPDATABLE = "balanceMovementCollectionUpdatable";
	
}
