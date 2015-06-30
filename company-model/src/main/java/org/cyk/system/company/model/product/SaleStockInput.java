package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.event.Event;
import org.cyk.utility.common.annotation.ModelBean;
import org.cyk.utility.common.annotation.ModelBean.CrudStrategy;

/**
 * To rent stock space to customer
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @ModelBean(crudStrategy=CrudStrategy.BUSINESS)
public class SaleStockInput extends AbstractIdentifiable implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@OneToOne @NotNull
	private Sale sale; 
	
	@OneToOne @NotNull
	private TangibleProductStockMovement tangibleProductStockMovement;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal remainingNumberOfGoods = BigDecimal.ZERO;
	
	@OneToOne
	private Event event;
	
	@Transient
	private Collection<SaleStockOutput> saleStockOutputs;
	
}
