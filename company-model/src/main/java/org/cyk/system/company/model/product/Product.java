package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cyk.system.company.model.structure.OwnedCompany;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudInheritanceStrategy;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity 
@Inheritance(strategy=InheritanceType.JOINED)
@ModelBean(crudStrategy=CrudStrategy.ENUMERATION,crudInheritanceStrategy=CrudInheritanceStrategy.CHILDREN_ONLY)
public class Product extends AbstractEnumeration implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne protected ProductCategory category;
	
	@Transient protected OwnedCompany ownedCompany;
	
	public Product(String code, String name, ProductCategory category) {
		super(code, name, null, null);
		this.category = category;
	}
	
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_OWNED_COMPANY = "ownedCompany";

}
