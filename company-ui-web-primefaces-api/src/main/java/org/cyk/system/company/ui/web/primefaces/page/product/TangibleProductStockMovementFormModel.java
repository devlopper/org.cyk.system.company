package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter
public class TangibleProductStockMovementFormModel extends AbstractFormModel<TangibleProductStockMovement> implements Serializable {

	private static final long serialVersionUID = 6682704920325189413L;

	@Input @InputChoice @InputOneChoice @InputOneCombo
	private TangibleProduct tangibleProduct;
	
	//@Input @InputNumber
	//private BigDecimal currentQuantity = BigDecimal.ZERO;
	
	@Input @InputNumber
	private BigDecimal quantity = BigDecimal.ZERO;
	
	//@Input @InputTextarea
	//private String comments;
	
}
