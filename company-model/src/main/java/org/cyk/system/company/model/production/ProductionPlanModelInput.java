package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class ProductionPlanModelInput extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private ProductionPlanModel productionPlanModel;
	
	@ManyToOne @NotNull
	private TangibleProduct tangibleProduct;

	public ProductionPlanModelInput(TangibleProduct tangibleProduct) {
		super();
		this.tangibleProduct = tangibleProduct;
	}
	
	
	
}

