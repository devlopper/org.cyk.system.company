package org.cyk.system.company.model.structure;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.pattern.tree.DataTreeType;

@Getter @Setter @NoArgsConstructor @Entity 
public class DivisionType extends DataTreeType implements Serializable  {

	private static final long serialVersionUID = -6838401709866343401L;

	public static final String DEPARTMENT = "DEPARTMENT";
	
	public DivisionType(DataTreeType parent, String code,String label) {
		super(parent, code,label);
	}

	
	
}
