package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.mathematics.Metric;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class ProductionPlanModelMetric extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private ProductionPlanModel productionPlanModel;
	
	@ManyToOne @NotNull
	private Metric metric;

	public ProductionPlanModelMetric(Metric metric) {
		super();
		this.metric = metric;
	}
	
}

