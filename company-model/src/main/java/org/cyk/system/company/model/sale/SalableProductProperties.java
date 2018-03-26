package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.product.Product;
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
public class SalableProductProperties extends AbstractIdentifiable implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_VALUE_ADDED_TAX_RATE) private ValueAddedTaxRate valueAddedTaxRate;
	
	/**
	 * The unit price of the product. null means the price will be determine at runtime
	 */
	@Column(precision=10,scale=FLOAT_SCALE) private BigDecimal price;
	@Column(precision=3,scale=FLOAT_SCALE) private BigDecimal quantityMultiple;
	
	@Transient private Class<? extends Product> productClass;
	@Transient private Boolean isProductStockable;
	@Transient private Party productProviderParty;
	@Transient private BigDecimal productStockQuantityMovementCollectionInitialValue;
	
	public SalableProductProperties setProductProviderPartyFromCode(String code){
		productProviderParty = getFromCode(Party.class, code);
		return this;
	}
	
	public SalableProductProperties setPriceFromObject(Object value){
		this.price = getNumberFromObject(BigDecimal.class, value);
		return this;
	}
	
	public SalableProductProperties setProductStockQuantityMovementCollectionInitialValueFromObject(Object value){
		this.productStockQuantityMovementCollectionInitialValue = getNumberFromObject(BigDecimal.class, value);
		return this;
	}
	
	@Override
	public SalableProductProperties setImage(File image) {
		return (SalableProductProperties) super.setImage(image);
	}
	
	@Override
	public SalableProductProperties setCascadeOperationToMaster(Boolean cascadeOperationToMaster) {
		return (SalableProductProperties) super.setCascadeOperationToMaster(cascadeOperationToMaster);
	}
	
	@Override
	public SalableProductProperties setCascadeOperationToMasterFieldNames(Collection<String> cascadeOperationToMasterFieldNames) {
		return (SalableProductProperties) super.setCascadeOperationToMasterFieldNames(cascadeOperationToMasterFieldNames);
	}
	
	/**/
	
	public static final String FIELD_VALUE_ADDED_TAX_RATE = "valueAddedTaxRate";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_QUANTITY_MULTIPLE = "quantityMultiple";
	public static final String FIELD_IS_PRODUCT_STOCKABLE = "isProductStockable";
	public static final String FIELD_PRODUCT_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "productStockQuantityMovementCollectionInitialValue";
	
	public static final String COLUMN_VALUE_ADDED_TAX_RATE = FIELD_VALUE_ADDED_TAX_RATE;
}
