package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.system.root.model.pattern.tree.AbstractDataTreeType;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.FEMALE)
@FieldOverride(name=ProductFamily.FIELD___PARENT__,type=ProductFamily.class)
public class ProductFamily extends AbstractDataTreeType implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
}
