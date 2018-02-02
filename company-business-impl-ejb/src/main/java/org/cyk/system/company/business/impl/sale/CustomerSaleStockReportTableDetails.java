package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.Customer;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment.Horizontal;
import org.cyk.utility.common.annotation.user.interfaces.style.Style;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Deprecated
public class CustomerSaleStockReportTableDetails implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String registrationCode;
	@Input @InputText private String names;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleStockInputCount;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleStockOutputCount;
	
	public CustomerSaleStockReportTableDetails(Customer customer) {
		this.registrationCode = customer.getCode();
		//this.names = customer.getPerson().getNames();
		//this.saleStockInputCount = inject(NumberBusiness.class).format(customer.getSaleStockInputCount());
		//this.saleStockOutputCount = inject(NumberBusiness.class).format(customer.getSaleStockOutputCount());
	}
}