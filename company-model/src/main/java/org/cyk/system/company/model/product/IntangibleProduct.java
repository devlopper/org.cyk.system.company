package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.system.company.model.CompanyConstant;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.FEMALE)
public class IntangibleProduct extends Product implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;

	public static final String STOCKING = IntangibleProduct.class.getSimpleName()+Constant.CHARACTER_UNDESCORE+CompanyConstant.STOCKING;
	
	public IntangibleProduct(String code, String name,ProductCategory category) {
		super(code, name, category);
	}
	
}
