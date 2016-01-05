package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment.Horizontal;
import org.cyk.utility.common.annotation.user.interfaces.style.Style;

@Getter @Setter
public class StockDashBoardReportTableDetails implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String code;
	@Input @InputText private String name;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String stockQuantity;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String useQuantity;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String usedQuantity;
	
	public StockDashBoardReportTableDetails(TangibleProduct tangibleProduct) {
		this.code = tangibleProduct.getCode();
		this.name = tangibleProduct.getName();
		/*this.stockQuantity = RootBusinessLayer.getInstance().getNumberBusiness().format(tangibleProduct.getStockQuantity());
		this.useQuantity = RootBusinessLayer.getInstance().getNumberBusiness().format(tangibleProduct.getUseQuantity());
		this.usedQuantity = RootBusinessLayer.getInstance().getNumberBusiness().format(tangibleProduct.getUsedQuantity());*/
	}
}