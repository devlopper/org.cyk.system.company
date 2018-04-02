package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;
import org.cyk.utility.common.helper.ClassHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(genderType=GenderType.MALE,crudStrategy=CrudStrategy.BUSINESS) @Accessors(chain=true)
public class SalableProduct extends AbstractEnumeration implements Serializable {
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT,unique=true) @NotNull private Product product;
	@ManyToOne @JoinColumn(name=COLUMN_PROPERTIES) @NotNull private SalableProductProperties properties;
	
	@Override
	public SalableProduct setCode(String code) {
		return (SalableProduct) super.setCode(code);
	}
	
	public Product getProduct(Boolean instanciateIfValueIsNull){
		return readFieldValue(FIELD_PRODUCT, instanciateIfValueIsNull);
	}
	
	public SalableProduct setProductFromCode(String code){
		product = getFromCode(Product.class, code);
		return this;
	}
	
	public SalableProduct setProductIsStockable(Boolean isStockable){
		getProduct(Boolean.TRUE).setStockable(isStockable);
		return this;
	}
	
	public SalableProduct setProductProviderPartyFromCode(String code){
		getProduct(Boolean.TRUE).setProviderPartyFromCode(code);
		return this;
	}
	
	public SalableProductProperties getProperties(Boolean createIfNull){
		if(this.properties == null && Boolean.TRUE.equals(createIfNull))
			this.properties = ClassHelper.getInstance().instanciateOne(SalableProductProperties.class);
		return this.properties;
	}
	
	public SalableProduct setPropertiesPriceFromObject(Object value){
		getProperties(Boolean.TRUE).setPriceFromObject(value);
		return this;
	}
	
	public SalableProduct setProductStockQuantityMovementCollectionInitialValueFromObject(Object value){
		getProduct(Boolean.TRUE).setStockQuantityMovementCollectionInitialValueFromObject(value);
		return this;
	}
	
	@Override
	public SalableProduct setImage(File image) {
		return (SalableProduct) super.setImage(image);
	}
	
	@Override
	public SalableProduct setCascadeOperationToMaster(Boolean cascadeOperationToMaster) {
		return (SalableProduct) super.setCascadeOperationToMaster(cascadeOperationToMaster);
	}
	
	@Override
	public SalableProduct setCascadeOperationToMasterFieldNames(Collection<String> cascadeOperationToMasterFieldNames) {
		return (SalableProduct) super.setCascadeOperationToMasterFieldNames(cascadeOperationToMasterFieldNames);
	}
	
	@Override
	public SalableProduct addCascadeOperationToChildrenFieldNames(String... fieldNames) {
		return (SalableProduct) super.addCascadeOperationToChildrenFieldNames(fieldNames);
	}
	
	@Override
	public SalableProduct addCascadeOperationToMasterFieldNames(String... fieldNames) {
		return (SalableProduct) super.addCascadeOperationToMasterFieldNames(fieldNames);
	}
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_VALUE_ADDED_TAX_RATE = "valueAddedTaxRate";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_QUANTITY_MULTIPLE = "quantityMultiple";
	public static final String FIELD_IS_PRODUCT_STOCKABLE = "isProductStockable";
	public static final String FIELD_PRODUCT_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE = "productStockQuantityMovementCollectionInitialValue";
	public static final String FIELD_PROPERTIES = "properties";
	
	public static final String COLUMN_PRODUCT = FIELD_PRODUCT;
	public static final String COLUMN_VALUE_ADDED_TAX_RATE = FIELD_VALUE_ADDED_TAX_RATE;
	public static final String COLUMN_PROPERTIES = FIELD_PROPERTIES;
}
