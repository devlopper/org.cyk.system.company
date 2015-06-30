package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.MetricValue;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class ProductionInput extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private Production production;
	
	@ManyToOne @NotNull
	private ProductionPlanModelInput productionPlanModelInput;
	
	@Embedded
	private MetricValue metricValue = new MetricValue();

	public ProductionInput(ProductionPlanModelInput productionPlanModelInput) {
		super();
		this.productionPlanModelInput = productionPlanModelInput;
	}
	
	
}

