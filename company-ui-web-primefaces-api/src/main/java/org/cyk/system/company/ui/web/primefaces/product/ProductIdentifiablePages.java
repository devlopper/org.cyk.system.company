package org.cyk.system.company.ui.web.primefaces.product;

import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.ui.web.primefaces.IdentifiableEditPageFormMaster;
import org.cyk.utility.common.userinterface.collection.DataTable;
import org.cyk.utility.common.userinterface.container.form.FormDetail;

public class ProductIdentifiablePages {

	public static void prepareProductEditFormMaster(final FormDetail detail,Class<?> aClass){
		//detail.add(Product.FIELD_GROUP).addBreak();
		detail.add(Product.FIELD_FAMILY).addBreak();
		detail.add(Product.FIELD_CATEGORY).addBreak();
		detail.add(Product.FIELD_SIZE).addBreak();
		detail.add(Product.FIELD_PRICE).addBreak();
		detail.add(Product.FIELD_TANGIBILITY).addBreak();
		detail.add(Product.FIELD_SALABLE).addBreak();
		detail.add(Product.FIELD_SALABLE_PRODUCT_PROPERTIES_PRICE).addBreak();
		
		detail.add(Product.FIELD_STORABLE).addBreak();
		detail.add(Product.FIELD_STORES).addBreak();
		
		detail.add(Product.FIELD_STOCKABLE).addBreak();
		detail.add(Product.FIELD_STOCK_QUANTITY_MOVEMENT_COLLECTION_INITIAL_VALUE).addBreak();
		detail.add(Product.FIELD_PROVIDERABLE).addBreak();
		detail.add(Product.FIELD_PROVIDER_PARTY).addBreak();
		IdentifiableEditPageFormMaster.addImage(detail);
		IdentifiableEditPageFormMaster.addDescription(detail);
	}
	
	public static void processProductColumnsFieldNames(DataTable dataTable,Collection<String> fieldNames) {
		fieldNames.addAll(Arrays.asList(Product.FIELD_FAMILY,Product.FIELD_CATEGORY,Product.FIELD_SIZE));
	}
	
	public static void prepareProductStoreEditFormMaster(final FormDetail detail,Class<?> aClass){
		detail.add(ProductStore.FIELD_PRODUCT).addBreak();
		detail.add(ProductStore.FIELD_STORE).addBreak();
	}
	
	public static void processProductStoreColumnsFieldNames(DataTable dataTable,Collection<String> fieldNames) {
		
	}
	
}
