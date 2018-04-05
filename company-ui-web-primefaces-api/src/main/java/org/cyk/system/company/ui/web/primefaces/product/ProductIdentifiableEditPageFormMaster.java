package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.ui.web.primefaces.IdentifiableEditPageFormMaster;
import org.cyk.utility.common.userinterface.container.Form;

public class ProductIdentifiableEditPageFormMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void prepareProduct(Form.Detail detail,Class<?> aClass){
		detail.add(Product.FIELD_TANGIBILITY).addBreak();
		//detail.add(Product.FIELD_GROUP).addBreak();
		detail.add(Product.FIELD_CATEGORY).addBreak();
		detail.add(Product.FIELD_PRICE).addBreak();
		detail.add(Product.FIELD_SALABLE).addBreak();
		detail.add(Product.FIELD_SALABLE_PRODUCT_PROPERTIES_PRICE).addBreak();
		//detail.add(Product.FIELD_STORABLE).addBreak();
		detail.add(Product.FIELD_STOCKABLE).addBreak();
		detail.add(Product.FIELD_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE).addBreak();
		detail.add(Product.FIELD_PROVIDERABLE).addBreak();
		detail.add(Product.FIELD_PROVIDER_PARTY).addBreak();
		IdentifiableEditPageFormMaster.addImage(detail);
		IdentifiableEditPageFormMaster.addDescription(detail);
	}
	
	public static void prepareProductStore(Form.Detail detail,Class<?> aClass){
		detail.add(ProductStore.FIELD_PRODUCT).addBreak();
		detail.add(ProductStore.FIELD_STORE).addBreak();
	}
	
}
