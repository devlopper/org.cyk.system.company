package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE)
public class TangibleProduct extends Product implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@Transient private Boolean isStockable;
	
	@Override
	public TangibleProduct setName(String name) {
		return (TangibleProduct) super.setName(name);
	}
	
	public static final String FIELD_IS_STOCKABLE = "isStockable";
}
