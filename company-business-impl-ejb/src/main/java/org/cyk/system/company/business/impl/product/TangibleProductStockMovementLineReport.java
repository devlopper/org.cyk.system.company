package org.cyk.system.company.business.impl.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProductStockMovement;
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.utility.common.annotation.user.interfaces.Input;

@Getter @Setter
public class TangibleProductStockMovementLineReport implements Serializable {
	
	private static final long serialVersionUID = -4946585596435850782L;

	@Input private String date;
	@Input private String tangibleProduct;
	@Input private String quantity;
	
	private String comments;
	
	public TangibleProductStockMovementLineReport(TangibleProductStockMovement tangibleProductStockMovement) {
		date = RootBusinessLayer.getInstance().getTimeBusiness().formatDateTime(tangibleProductStockMovement.getDate());
		tangibleProduct = tangibleProductStockMovement.getTangibleProduct().getUiString();
		quantity = RootBusinessLayer.getInstance().getNumberBusiness().format(tangibleProductStockMovement.getQuantity());
	}
	
}
