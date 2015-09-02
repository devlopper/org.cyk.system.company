package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
public class SaleStockOutput extends SaleStock implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@ManyToOne @NotNull
	private SaleStockInput saleStockInput; 
	
	@ManyToOne @NotNull
	private SaleCashRegisterMovement saleCashRegisterMovement;

	@Column(precision=10,scale=FLOAT_SCALE,nullable=false) @NotNull private BigDecimal remainingNumberOfGoods;
	
	public SaleStockOutput(SaleStockInput saleStockInput,
			SaleCashRegisterMovement saleCashRegisterMovement,TangibleProductStockMovement tangibleProductStockMovement) {
		super(tangibleProductStockMovement);
		this.saleStockInput = saleStockInput;
		this.saleCashRegisterMovement = saleCashRegisterMovement;
	}
	
	public static final String FIELD_SALE_STOCK_INPUT = "saleStockInput";
	public static final String FIELD_SALE_CASH_REGISTER_MOVEMENT = "saleCashRegisterMovement";
	public static final String FIELD_REMAINING_NUMBER_OF_GOODS = "remainingNumberOfGoods";
}
