package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;

import org.cyk.system.root.model.pattern.tree.AbstractDataTree;
import org.cyk.utility.common.annotation.FieldOverride;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @FieldOverride(name=Division.FIELD_TYPE,type=DivisionType.class)
public class Division extends AbstractDataTree<DivisionType> implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	public Division(AbstractDataTree<DivisionType> parent, DivisionType type, String code) {
		super(parent, type, code);
	}
}
