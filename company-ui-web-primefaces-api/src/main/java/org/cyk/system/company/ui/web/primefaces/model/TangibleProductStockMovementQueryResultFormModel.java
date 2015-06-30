package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Getter @Setter
public class TangibleProductStockMovementQueryResultFormModel extends AbstractFormModel<TangibleProductStockMovement> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputChoice @InputOneChoice @InputOneCombo
	private TangibleProduct tangibleProduct;
	
	@Input @InputCalendar(format=Format.DATETIME_SHORT) private Date date;
	@Input @InputNumber private BigDecimal quantity;
	
	//@Input @InputTextarea
	private String comments;
	
}
