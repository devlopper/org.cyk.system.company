package org.cyk.system.company.ui.web.primefaces.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.utility.common.annotation.user.interfaces.Binding;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar;
import org.cyk.utility.common.annotation.user.interfaces.InputCalendar.Format;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SaleQueryResultFormModel extends AbstractFormModel<Sale> implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @InputText @Binding(field="identificationNumber")
	private String number;
	
	@Input @InputText
	private String customer;
	
	@Input @InputNumber
	private BigDecimal cost;
	
	@Input @InputCalendar(format=Format.DATETIME_SHORT)
	private Date date;
	
	@Input @InputNumber
	private BigDecimal balance;
	
	@Override
	public void read() {
		super.read();
		if(identifiable.getCustomer()!=null)
			customer = identifiable.getCustomer().getCode()+"/"+
				identifiable.getCustomer().getPerson().getNames();
	}
	
}
