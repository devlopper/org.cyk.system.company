package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.spreadsheet.AbstractSpreadSheetTemplateColumn;
import org.cyk.system.root.model.userinterface.InputName;

@Getter @Setter @Entity
public class ProductionPlanMetric extends AbstractSpreadSheetTemplateColumn<ProductionPlan> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	public ProductionPlanMetric() {}

	public ProductionPlanMetric(ProductionPlan productionPlan,InputName inputName) {
		super(productionPlan,inputName);
	}
	
}

