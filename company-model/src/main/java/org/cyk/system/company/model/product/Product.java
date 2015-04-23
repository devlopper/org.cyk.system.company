package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Division;
import org.cyk.system.root.model.AbstractEnumeration;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudInheritanceStrategy;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanCheck;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter @NoArgsConstructor @Entity 
@Inheritance(strategy=InheritanceType.JOINED)
@ModelBean(crudStrategy=CrudStrategy.ENUMERATION,crudInheritanceStrategy=CrudInheritanceStrategy.CHILDREN_ONLY)
public class Product extends AbstractEnumeration implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne
	@Input @InputChoice @InputOneChoice @InputOneCombo
	protected Division division;
	
	@ManyToOne
	@Input @InputChoice @InputOneChoice @InputOneCombo
	protected ProductCategory category;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	@Input @InputNumber
	protected BigDecimal price;
	
	@Column(nullable=false) @NotNull
	@Input @InputBooleanCheck
	protected Boolean salable = Boolean.TRUE;
	
	/*
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	protected BigDecimal usedCount = BigDecimal.ZERO;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull
	protected BigDecimal turnover = BigDecimal.ZERO;
	*/

	public Product(String code, String name, Division division, ProductCategory category,BigDecimal price) {
		super(code, name, null, null);
		this.division = division;
		this.category = category;
		this.price = price;
	}

}
