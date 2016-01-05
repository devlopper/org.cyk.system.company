package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;

@Getter @Setter
public class SaleStockOutputFormModel extends AbstractFormModel<SaleStockOutput> implements Serializable {

	private static final long serialVersionUID = -7403076234556118486L;
	
	@Input @InputNumber
	private BigDecimal numberOfGoods;
	
	@Input @InputNumber
	private BigDecimal paid;
	
	@Override
	public void write() {
		super.write();
		identifiable.getTangibleProductStockMovement().setQuantity(numberOfGoods.abs().negate());
		identifiable.getSaleCashRegisterMovement().setAmountIn(paid);
	}
}