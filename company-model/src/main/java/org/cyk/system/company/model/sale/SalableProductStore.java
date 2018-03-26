package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity 
@ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.FEMALE)
public class SalableProductStore extends AbstractIdentifiable implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT_STORE,unique=true) @NotNull private ProductStore productStore;
	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT_PROPERTIES) @NotNull private SalableProductProperties salableProductProperties;
	
	@Override
	public SalableProductStore setCode(String code) {
		return (SalableProductStore) super.setCode(code);
	}
	
	public SalableProductStore setProductStoreFromCode(String code){
		productStore = getFromCode(ProductStore.class, code);
		return this;
	}
	
	/**/
	
	public static final String FIELD_PRODUCT_STORE = "productStore";
	public static final String FIELD_SALABLE_PRODUCT_PROPERTIES = "salableProductProperties";
	
	public static final String COLUMN_PRODUCT_STORE = FIELD_PRODUCT_STORE;
	public static final String COLUMN_SALABLE_PRODUCT_PROPERTIES = FIELD_SALABLE_PRODUCT_PROPERTIES;
	
}
