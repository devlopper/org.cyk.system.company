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
import org.cyk.system.root.model.ValueDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @Entity
public class ResellerProduction extends AbstractIdentifiable implements Serializable {

	private static final long serialVersionUID = 1371797411549893368L;

	@ManyToOne @NotNull private Reseller reseller;
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
	@Embedded private ValueDetails amount = new ValueDetails();
	
	/* Return */
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal returnedQuantity = BigDecimal.ZERO;
	
}
