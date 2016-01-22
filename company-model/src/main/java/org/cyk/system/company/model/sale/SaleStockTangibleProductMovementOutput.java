package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity @DiscriminatorValue(value="O")
public class SaleStockTangibleProductMovementOutput extends SaleStockTangibleProductMovement implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private SaleStockTangibleProductMovementInput saleStockInput; 
	
	@ManyToOne @NotNull
	private SaleCashRegisterMovement saleCashRegisterMovement;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal remainingNumberOfGoods = BigDecimal.ZERO;
	
	public SaleStockTangibleProductMovementOutput(SaleStockTangibleProductMovementInput saleStockInput,SaleCashRegisterMovement saleCashRegisterMovement,StockTangibleProductMovement tangibleProductStockMovement) {
		super(tangibleProductStockMovement);
		this.saleStockInput = saleStockInput;
		this.saleCashRegisterMovement = saleCashRegisterMovement;
	}
	
	public static final String FIELD_SALE_STOCK_INPUT = "saleStockInput";
	public static final String FIELD_SALE_CASH_REGISTER_MOVEMENT = "saleCashRegisterMovement";
	public static final String FIELD_REMAINING_NUMBER_OF_GOODS = "remainingNumberOfGoods";
}
