package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	public ProductStore setStoreFromCode(String code){
		this.store = getFromCode(Store.class, code);
		return this;
	}
	
	/**/
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_STORE = "store";
	
	public static final String COLUMN_PRODUCT = FIELD_PRODUCT;
	public static final String COLUMN_STORE = FIELD_STORE;
	
	
}
