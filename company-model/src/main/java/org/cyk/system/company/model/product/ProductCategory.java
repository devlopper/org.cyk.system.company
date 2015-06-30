package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Division;
import org.cyk.system.root.model.pattern.tree.DataTreeType;

@Getter @Setter @NoArgsConstructor @Entity 
public class ProductCategory extends DataTreeType implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	@ManyToOne
	//@Input @InputChoice @InputOneChoice @InputOneCombo
	protected Division division;
	
	public ProductCategory(DataTreeType parent, String code,String label) {
		super(parent, code,label);
	}
	
	
}
