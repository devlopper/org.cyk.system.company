package org.cyk.system.company.model.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.Value;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class ResellerProduction extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private ResellerProductionPlan resellerProductionPlan;
	@ManyToOne @NotNull private Production production;
	
	/* Taking */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal takenQuantity = BigDecimal.ZERO;
	
	/* Sale */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal soldQuantity = BigDecimal.ZERO;
	
	@AttributeOverrides(value={
			@AttributeOverride(name="user",column=@Column(name="sold_amount_user"))
			,@AttributeOverride(name="system",column=@Column(name="sold_amount_computed"))
			,@AttributeOverride(name="gap",column=@Column(name="sold_amount_gap"))
	})
	@Embedded private Value amount = new Value();
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal amountGapCumul = BigDecimal.ZERO;
	
	/* Return */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal returnedQuantity = BigDecimal.ZERO;
	
	/**/
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal discount = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal commission = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal payable = BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal payableCumul = BigDecimal.ZERO;
	
	/**/
	
	public ResellerProduction(ResellerProductionPlan resellerProductionPlan, Production production) {
		super();
		this.resellerProductionPlan = resellerProductionPlan;
		this.production = production;
	}
	
	/**/
	
	public static final String FIELD_RESELLER_PRODUCTION_PLAN = "resellerProductionPlan";
	public static final String FIELD_PRODUCTION = "production";
}
