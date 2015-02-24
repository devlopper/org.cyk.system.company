package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Sale;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Binding;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SaleFormModel extends AbstractFormModel<Sale> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText @Binding(field="identificationNumber")
	private String number;
	
	@Input @InputNumber
	private BigDecimal cost;
	
	@Input @InputCalendar
	private Date date;
	
	@Input @InputNumber
	private BigDecimal balance;
	
}
