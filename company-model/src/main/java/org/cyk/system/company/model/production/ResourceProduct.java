package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity 
public class ResourceProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Product product;
	
	public ResourceProduct(Product product) {
		super();
		this.product = product;
	}
	
	@Override
	public String getUiString() {
		return product.getUiString();
	}
	
	@Override
	public String toString() {
		return product.getUiString();
	}
	
}
