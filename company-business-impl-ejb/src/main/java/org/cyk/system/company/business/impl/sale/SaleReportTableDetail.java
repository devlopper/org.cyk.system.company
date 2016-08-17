package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;

@Getter @Setter @Deprecated
public class SaleReportTableDetail implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @ReportColumn private String number;
	@Input @ReportColumn private String customer;
	@Input @ReportColumn private String cost;
	@Input @ReportColumn private String date;
	@Input @ReportColumn private String balance;
	
	public SaleReportTableDetail(Sale sale) {
		number = sale.getIdentifier().toString();
		if(sale.getCustomer()!=null)
			customer = sale.getCustomer().getPerson().getNames();
		//cost = inject(NumberBusiness.class).format(sale.getCost().getValue());
		//balance = inject(NumberBusiness.class).format(sale.getBalance().getValue());
		//date = RootBusinessLayer.getInstance().getTimeBusiness().formatDateTime(sale.getDate());
	}
	
}
