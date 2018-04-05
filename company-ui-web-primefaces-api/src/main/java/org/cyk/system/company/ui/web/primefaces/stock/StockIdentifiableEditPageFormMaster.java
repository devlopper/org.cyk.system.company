package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;

import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.utility.common.userinterface.container.Form;

public class StockIdentifiableEditPageFormMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void prepareStockableProduct(Form.Detail detail){
		detail.add(StockableProduct.FIELD_PRODUCT).addBreak();
		detail.add(StockableProduct.FIELD_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE).addBreak();
	}
	
	public static void prepareStockableProductStore(Form.Detail detail){
		detail.add(StockableProductStore.FIELD_PRODUCT_STORE).addBreak();
		detail.add(StockableProductStore.FIELD_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE).addBreak();
	}
	
}
