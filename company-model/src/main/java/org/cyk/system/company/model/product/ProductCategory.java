package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.cyk.system.root.model.file.Script;
import org.cyk.system.root.model.pattern.tree.AbstractDataTreeType;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.ModelBean.GenderType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS,genderType=GenderType.FEMALE)
@FieldOverride(name=ProductCategory.FIELD_PARENT,type=ProductCategory.class)
public class ProductCategory extends AbstractDataTreeType implements Serializable  {
	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne @JoinColumn(name=COLUMN_PRODUCT_CODE_GENERATION_SCRIPT) @Accessors(chain=true) private Script productCodeGenerationScript;
	
	public static final String FIELD_PRODUCT_CODE_GENERATION_SCRIPT = "productCodeGenerationScript";
	
	public static final String COLUMN_PRODUCT_CODE_GENERATION_SCRIPT = FIELD_PRODUCT_CODE_GENERATION_SCRIPT;
}
