package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.spreadsheet.AbstractSpreadSheetTemplateRow;

/**
 * Resource used in the production process in order to produce output
 * @author Komenan.Christian
 *
 */
@Getter @Setter @NoArgsConstructor @Entity
public class ProductionPlanResource extends AbstractSpreadSheetTemplateRow<ProductionPlan> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull private Product product;

	public ProductionPlanResource(ProductionPlan productionPlan,Product product) {
		super(productionPlan); 
		this.product = product;
	}
	
}

