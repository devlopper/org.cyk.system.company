package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;

/**
 * A sale linked to a stock movement of a tangible product.
 * @see Sale
 * @see StockTangibleProductMovement
 * @author Christian Yao Komenan
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @DiscriminatorValue(value="I")
public class SaleStockTangibleProductMovementInput extends SaleStockTangibleProductMovement implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull 
	private Sale sale;
	
	private String externalIdentifier;
	
	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal remainingNumberOfGoods = BigDecimal.ZERO;
	
	@Transient
	private Collection<SaleStockTangibleProductMovementOutput> saleStockOutputs;
	
	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,externalIdentifier, sale,stockTangibleProductMovement,remainingNumberOfGoods);
	}
	
	private static final String LOG_FORMAT = SaleStockTangibleProductMovementInput.class.getSimpleName()+"(EI=%s %s %s R=%s)";
	
	/**/
	
	public static final String FIELD_SALE = "sale";
	public static final String FIELD_EXTERNAL_IDENTIFIER = "externalIdentifier";
	public static final String FIELD_REMAINING_NUMBER_OF_GOODS = "remainingNumberOfGoods";
	public static final String FIELD_EVENT = "event";
	
}
