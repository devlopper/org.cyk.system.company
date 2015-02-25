package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

import org.cyk.system.company.model.structure.Division;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity 
public class TangibleProduct extends Product implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	public TangibleProduct(String code, String name, Division division,ProductCategory category, BigDecimal price) {
		super(code, name, division, category, price);
	}
	
	
}
