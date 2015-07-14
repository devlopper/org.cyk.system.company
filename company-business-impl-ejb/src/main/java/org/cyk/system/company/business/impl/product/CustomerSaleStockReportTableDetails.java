package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Customer;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment.Horizontal;
import org.cyk.utility.common.annotation.user.interfaces.style.Style;

@Getter @Setter
public class CustomerSaleStockReportTableDetails implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String registrationCode;
	@Input @InputText private String names;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleStockInputCount;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String saleStockOutputCount;
	
	public CustomerSaleStockReportTableDetails(Customer customer) {
		this.registrationCode = customer.getRegistration().getCode();
		this.names = customer.getPerson().getNames();
		this.saleStockInputCount = RootBusinessLayer.getInstance().getNumberBusiness().format(customer.getSaleStockInputCount());
		this.saleStockOutputCount = RootBusinessLayer.getInstance().getNumberBusiness().format(customer.getSaleStockOutputCount());
	}
}