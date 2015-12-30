package org.cyk.system.company.model.product;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.structure.Division;

@Getter @Setter @NoArgsConstructor @Entity 
public class TangibleProduct extends Product implements Serializable  {

	private static final long serialVersionUID = -6128937819261060725L;
	
	public static final String SALE_STOCK = "STOCK_GOODS";
		
	public TangibleProduct(String code, String name, Division division,ProductCategory category) {
		super(code, name, division, category);
	}
	
	
}
