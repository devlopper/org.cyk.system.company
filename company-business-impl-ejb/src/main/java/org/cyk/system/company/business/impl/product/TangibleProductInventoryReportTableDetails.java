package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;
import org.cyk.utility.common.annotation.user.interfaces.ReportColumn;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment;
import org.cyk.utility.common.annotation.user.interfaces.style.Alignment.Horizontal;
import org.cyk.utility.common.annotation.user.interfaces.style.Style;

@Getter @Setter @Deprecated
public class TangibleProductInventoryReportTableDetails implements Serializable {
	private static final long serialVersionUID = -6341285110719947720L;
	
	@Input @InputText private String code;
	@Input @InputText private String name;
	@Input @InputText @ReportColumn(style=@Style(alignment=@Alignment(horizontal=Horizontal.RIGHT))) private String quantity;
	
	public TangibleProductInventoryReportTableDetails(TangibleProductInventoryDetail tangibleProductInventoryDetail) {
		this.code = tangibleProductInventoryDetail.getTangibleProduct().getCode();
		this.name = tangibleProductInventoryDetail.getTangibleProduct().getName();
		//this.quantity = inject(NumberBusiness.class).format(tangibleProductInventoryDetail.getQuantity());
	}
}