package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.spreadsheet.AbstractSpreadSheetTemplate;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ProductionPlan extends AbstractSpreadSheetTemplate<ProductionPlanResource, ProductionPlanMetric> implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;
	
	@ManyToOne @NotNull private ProductionUnit productionUnit;
	@ManyToOne @NotNull private ManufacturedProduct manufacturedProduct;
	@ManyToOne @NotNull private ProductionEnergy energy;
	@Temporal(TemporalType.TIMESTAMP) private Date previousReportDate;
	@Temporal(TemporalType.TIMESTAMP) @NotNull private Date nextReportDate;
	@ManyToOne @NotNull private TimeDivisionType reportIntervalTimeDivisionType;
	@Column(length=1024 * 4) private String comments;
	
	public ProductionPlan() {}

	public ProductionPlan(String code,String name,ProductionUnit productionUnit,ManufacturedProduct manufacturedProduct,TimeDivisionType reportIntervalTimeDivisionType) {
		setCode(code);
		setName(name);
		this.productionUnit = productionUnit;
		this.manufacturedProduct = manufacturedProduct;
		this.reportIntervalTimeDivisionType = reportIntervalTimeDivisionType;
	}
	
	public static final String FIELD_PRODUCTION_UNIT = "productionUnit";
	
}
