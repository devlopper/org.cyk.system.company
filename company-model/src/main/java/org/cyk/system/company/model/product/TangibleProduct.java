package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.system.root.model.file.File;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Deprecated
@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.MALE) @Accessors(chain=true)
public class TangibleProduct extends Product implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@Override
	public TangibleProduct setName(String name) {
		return (TangibleProduct) super.setName(name);
	}
	
	@Override
	public TangibleProduct setImage(File image) {
		return (TangibleProduct) super.setImage(image);
	}
	
}
