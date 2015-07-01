package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheetTemplate;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ProductionPlanModel extends AbstractSpreadSheetTemplate<ProductionPlanModelInput, ProductionPlanModelMetric> implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;
	
	@ManyToOne @NotNull
	private TimeDivisionType timeDivisionType;
	
	public ProductionPlanModel() {}

	public ProductionPlanModel(String code,String name) {
		this.code = code;
		this.name = name;
	}
	
	
	
}
