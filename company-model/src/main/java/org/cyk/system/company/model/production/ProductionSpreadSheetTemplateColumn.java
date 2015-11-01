package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.userinterface.InputName;
import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheetTemplateColumn;

@Getter @Setter @Entity
public class ProductionSpreadSheetTemplateColumn extends AbstractSpreadSheetTemplateColumn<ProductionPlan> implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	public ProductionSpreadSheetTemplateColumn() {}

	public ProductionSpreadSheetTemplateColumn(InputName inputName) {
		super(inputName);
	}
	
}

