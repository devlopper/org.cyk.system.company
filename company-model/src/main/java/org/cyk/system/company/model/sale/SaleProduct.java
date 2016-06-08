package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractIdentifiable;

@Getter @Setter @NoArgsConstructor @Entity
public class SaleProduct extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne private Sale sale;
	@ManyToOne private SalableProduct salableProduct;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal quantity;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal reduction=BigDecimal.ZERO;
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal commission = BigDecimal.ZERO;
	
	@Embedded private Cost cost = new Cost();

	@Transient private Collection<SaleProductInstance> instances;
	
	public Collection<SaleProductInstance> getInstances(){
		if(instances==null)
			instances = new ArrayList<>();
		return instances;
	}
	
	@Override
	public String getUiString() {
		return salableProduct.getUiString();
	}
	
	@Override
	public String toString() {
		return getUiString();
	}
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,salableProduct.getLogMessage(),quantity,commission,reduction,cost.getLogMessage(),sale.getComputedIdentifier());
	}
	
	/**/
	
	private static final String LOG_FORMAT = SaleProduct.class.getSimpleName()+"(%s Q=%s C=%s R=%s %s S=%s)";

}
