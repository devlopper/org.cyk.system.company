package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.utility.common.annotation.user.interfaces.Input;

@Getter @Setter @Deprecated
public class TangibleProductStockMovementLineReport implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Input private String date;
	@Input private String tangibleProduct;
	@Input private String quantity;
	
	private String comments;
	
	public TangibleProductStockMovementLineReport(StockTangibleProductMovement tangibleProductStockMovement) {
		//date = RootBusinessLayer.getInstance().getTimeBusiness().formatDateTime(tangibleProductStockMovement.getMovement().getBirthDate());
		//tangibleProduct = tangibleProductStockMovement.getTangibleProduct().getUiString();
		//quantity = inject(NumberBusiness.class).format(tangibleProductStockMovement.getMovement().getValue());
	}
	
}
