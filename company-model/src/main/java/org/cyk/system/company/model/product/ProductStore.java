package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.store.Store;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity 
@ModelBean(crudStrategy=CrudStrategy.ENUMERATION,genderType=GenderType.FEMALE) @Accessors(chain=true)
@Table(uniqueConstraints={@UniqueConstraint(columnNames={ProductStore.FIELD_PRODUCT,ProductStore.FIELD_STORE})})
public class ProductStore extends AbstractIdentifiable implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT) @NotNull private Product product;
	@ManyToOne @JoinColumn(name=COLUMN_STORE) @NotNull private Store store;
	
	@Override
	public ProductStore setCode(String code) {
		return (ProductStore) super.setCode(code);
	}
	
	public ProductStore setProductFromCode(String code){
		this.product = getFromCode(Product.class, code);
		return this;
	}
	
	public Product getProduct(Boolean instanciateIfValueIsNull){
		return readFieldValue(FIELD_PRODUCT, instanciateIfValueIsNull);
	}
	
	public ProductStore setProductStockable(Boolean isStockable){
		getProduct(Boolean.TRUE).setStockable(isStockable);
		return this;
	}
	
	public ProductStore setProductStockQuantityMovementCollectionInitialValueFromObject(Object value){
		getProduct(Boolean.TRUE).setStockQuantityMovementCollectionInitialValueFromObject(value);
		return this;
	}
	
	public ProductStore setStoreFromCode(String code){
		this.store = getFromCode(Store.class, code);
		return this;
	}
	
	@Override
	public ProductStore addCascadeOperationToMasterFieldNames(String... fieldNames) {
		return (ProductStore) super.addCascadeOperationToMasterFieldNames(fieldNames);
	}
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_STORE = "store";
	
	public static final String COLUMN_PRODUCT = FIELD_PRODUCT;
	public static final String COLUMN_STORE = FIELD_STORE;
	
	@Getter @Setter
	public static class Filter extends AbstractIdentifiable.Filter<ProductStore> implements Serializable {
		private static final long serialVersionUID = -1498269103849317057L;

	}
	
}
