package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.store.Store;
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
	
	@ManyToOne @JoinColumn(name=COLUMN_SALABLE_PRODUCT) private SalableProduct salableProduct;
	@ManyToOne @JoinColumn(name=COLUMN_STORE) private Store store;
	
	@Override
	public SalableProductStore setCode(String code) {
		return (SalableProductStore) super.setCode(code);
	}
	
	/**/
	
	public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
	public static final String FIELD_STORE = "store";
	
	public static final String COLUMN_SALABLE_PRODUCT = FIELD_SALABLE_PRODUCT;
	public static final String COLUMN_STORE = FIELD_STORE;
	
	
}
