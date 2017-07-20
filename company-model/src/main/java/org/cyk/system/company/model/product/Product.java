package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudInheritanceStrategy;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter @NoArgsConstructor @Entity 
@Inheritance(strategy=InheritanceType.JOINED)
@ModelBean(crudStrategy=CrudStrategy.ENUMERATION,crudInheritanceStrategy=CrudInheritanceStrategy.CHILDREN_ONLY)
public class Product extends AbstractEnumeration implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @Input @InputChoice @InputOneChoice @InputOneCombo
	protected ProductCategory category;
	
	public Product(String code, String name, ProductCategory category) {
		super(code, name, null, null);
		this.category = category;
	}
	
	public static final String FIELD_CATEGORY = "category";


}
