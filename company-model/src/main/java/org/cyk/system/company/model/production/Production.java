package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.spreadsheet.AbstractSpreadSheet;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class Production extends AbstractSpreadSheet<ProductionPlan,ProductionPlanResource,ProductionPlanMetric,ProductionValue> implements Serializable {

	private static final long serialVersionUID = -625974035216780560L;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal manufacturedQuantity = BigDecimal.ZERO;
	//@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity = BigDecimal.ZERO;

	@Override
	public String getUiString() {
		return period.getFromDate().toString();
	}
}
