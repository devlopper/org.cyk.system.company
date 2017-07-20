package org.cyk.system.company.business.impl.stock;

import java.io.Serializable;

import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

public class StockableTangibleProductDetails extends AbstractOutputDetails<StockableTangibleProduct> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String product;
	
	public StockableTangibleProductDetails(StockableTangibleProduct stockableTangibleProduct) {
		super(stockableTangibleProduct);
		product = formatUsingBusiness(stockableTangibleProduct);
	}
}