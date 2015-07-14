package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;

@Getter @Setter
public class SaleStockInputReportTableDetail implements Serializable {

	private static final long serialVersionUID = -3328823824725030136L;

	@Input @ReportColumn private String number;
	@Input @ReportColumn private String customer;
	@Input @ReportColumn private String cost;
	@Input @ReportColumn private String date;
	@Input @ReportColumn private String numberOfGoods;
	@Input @ReportColumn private String remainingNumberOfGoods;
	@Input @ReportColumn private String balance;
	
	public SaleStockInputReportTableDetail(SaleStockInput saleStockInput) {
		number = saleStockInput.getSale().getIdentificationNumber();
		if(saleStockInput.getSale().getCustomer()!=null)
			customer = saleStockInput.getSale().getCustomer().getPerson().getNames();
		cost = RootBusinessLayer.getInstance().getNumberBusiness().format(saleStockInput.getSale().getCost());
		balance = RootBusinessLayer.getInstance().getNumberBusiness().format(saleStockInput.getSale().getBalance());
		numberOfGoods = RootBusinessLayer.getInstance().getNumberBusiness().format(saleStockInput.getTangibleProductStockMovement().getQuantity());
		remainingNumberOfGoods = RootBusinessLayer.getInstance().getNumberBusiness().format(saleStockInput.getRemainingNumberOfGoods());
		date = RootBusinessLayer.getInstance().getTimeBusiness().formatDateTime(saleStockInput.getSale().getDate());
	}
	
}
