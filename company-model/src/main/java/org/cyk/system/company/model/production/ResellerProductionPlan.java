package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ResellerProductionPlan extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private Reseller reseller;
	@ManyToOne @NotNull private ProductionPlan productionPlan;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal takingUnitPrice;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal saleUnitPrice;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal commissionRate = BigDecimal.ZERO;
	
	public ResellerProductionPlan(Reseller reseller, ProductionPlan productionPlan) {
		super();
		this.reseller = reseller;
		this.productionPlan = productionPlan;
	}
	
	@Override
	public String getUiString() {
		return reseller.getUiString();
	}
	
	@Override
	public String toString() {
		return reseller.toString();
	}
	
	/**/
	
	public static final String FIELD_RESELLER = "reseller";
	public static final String FIELD_PRODUCTION_PLAN = "productionPlan";
}
