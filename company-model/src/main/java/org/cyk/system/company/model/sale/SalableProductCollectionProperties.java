package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.file.File;
import org.cyk.system.root.model.party.Party;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class SalableProductCollectionProperties extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@Accessors(chain=true) private Boolean isStockMovementCollectionUpdatable;
	@Accessors(chain=true) private Boolean isBalanceMovementCollectionUpdatable;
	
	/**/
	
	public static final String FIELD_VALUE_ADDED_TAX_RATE = "valueAddedTaxRate";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_QUANTITY_MULTIPLE = "quantityMultiple";
	public static final String FIELD_IS_PRODUCT_STOCKABLE = "isProductStockable";
	public static final String FIELD_PRODUCT_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "productStockQuantityMovementCollectionInitialValue";
	
	public static final String COLUMN_VALUE_ADDED_TAX_RATE = FIELD_VALUE_ADDED_TAX_RATE;
}
