package org.cyk.system.company.model.production;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.system.root.model.userinterface.spreadsheet.AbstractSpreadSheetTemplate;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ProductionPlan extends AbstractSpreadSheetTemplate<ProductionPlanResource, ProductionPlanMetric> implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;
	
	@ManyToOne @NotNull private ProductionUnit productionUnit;
	@ManyToOne @NotNull private Product product;
	@ManyToOne @NotNull private TimeDivisionType timeDivisionType;
	@Column(length=1024 * 4) private String comments;
	
	public ProductionPlan() {}

	public ProductionPlan(String code,String name,ProductionUnit productionUnit,Product product,TimeDivisionType timeDivisionType) {
		this.code = code;
		this.name = name;
		this.productionUnit = productionUnit;
		this.product = product;
		this.timeDivisionType = timeDivisionType;
	}
	
	
	
}
