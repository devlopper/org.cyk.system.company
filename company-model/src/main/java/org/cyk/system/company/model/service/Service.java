package org.cyk.system.company.model.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Division;
import org.cyk.system.root.model.pattern.tree.AbstractDataTree;
import org.cyk.utility.common.annotation.user.interfaces.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter @NoArgsConstructor @Entity @FieldOverride(name="type",type=ServiceType.class)
//@ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Service extends AbstractDataTree<ServiceType> implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne
	@Input @InputChoice @InputOneChoice @InputOneCombo
	private Division division;
	
	@Column(precision=10,scale=FLOAT_SCALE)
	@Input @InputNumber
	private BigDecimal price;
	
	public Service(AbstractDataTree<ServiceType> parent, ServiceType type, String code) {
		super(parent, type, code);
	}
}
