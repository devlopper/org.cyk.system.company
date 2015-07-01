package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.userinterface.InputName;
import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheetTemplateColumn;

@Getter @Setter @Entity
public class ProductionPlanModelMetric extends AbstractSpreadSheetTemplateColumn<ProductionPlanModel> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	public ProductionPlanModelMetric() {}

	public ProductionPlanModelMetric(InputName inputName) {
		super(inputName);
	}
	
}

