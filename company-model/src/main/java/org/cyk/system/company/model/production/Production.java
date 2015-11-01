package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheet;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Production extends AbstractSpreadSheet<ProductionPlan,ProductionPlanResource,ProductionSpreadSheetTemplateColumn,ProductionSpreadSheetCell> implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;
	
	
}
