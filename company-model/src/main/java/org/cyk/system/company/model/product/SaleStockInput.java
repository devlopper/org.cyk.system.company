package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.model.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * To rent stock space to customer
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class SaleStockInput extends SaleStock implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@OneToOne @NotNull
	private Sale sale; 
	
	@Column private String externalIdentifier;//This value is used to link to another system
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal remainingNumberOfGoods = BigDecimal.ZERO;
	
	@OneToOne
	private Event event;
	
	@Transient
	private Collection<SaleStockOutput> saleStockOutputs;
	
	/**/
	
	public static final String FIELD_SALE = "sale";
	public static final String FIELD_EXTERNAL_IDENTIFIER = "externalIdentifier";
	public static final String FIELD_REMAINING_NUMBER_OF_GOODS = "remainingNumberOfGoods";
	public static final String FIELD_EVENT = "event";
	
}
