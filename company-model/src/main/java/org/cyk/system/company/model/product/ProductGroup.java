package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.system.root.model.pattern.tree.AbstractDataTree;
import org.cyk.utility.common.annotation.FieldOverride;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @FieldOverride(name=ProductGroup.FIELD_TYPE,type=ProductGroupType.class)
public class ProductGroup extends AbstractDataTree<ProductGroupType> implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
}
