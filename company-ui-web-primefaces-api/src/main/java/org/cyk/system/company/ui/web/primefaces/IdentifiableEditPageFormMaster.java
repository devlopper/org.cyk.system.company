package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductStore;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductStore;
import org.cyk.system.company.model.sale.SalableProductStoreCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.model.stock.StockableProductStoresTransfer;
import org.cyk.system.company.model.stock.StockableProductStoresTransferAcknowledgement;
import org.cyk.system.company.ui.web.primefaces.product.ProductIdentifiablePages;
import org.cyk.system.company.ui.web.primefaces.sale.SaleIdentifiableEditPageFormMaster;
import org.cyk.system.company.ui.web.primefaces.stock.StockIdentifiableEditPageFormMaster;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.userinterface.container.form.FormDetail;

public class IdentifiableEditPageFormMaster extends org.cyk.ui.web.primefaces.IdentifiableEditPageFormMaster implements Serializable {
	private static final long serialVersionUID = -6211058744595898478L;
	
	static {
		//ClassHelper.getInstance().map(MovementCollectionValuesTransferItemCollectionEditFormMasterPrepareListener.class, MovementCollectionValuesTransferItemCollectionEditFormMasterPrepareAdapter.class);
	}
	
	@Override
	protected void ____addName____() {
		if(Sale.class.equals(getPropertiesMap().getActionOnClass())){
			
		}else
			super.____addName____();
	}
	
	@Override
	protected void __prepare__() {
		super.__prepare__();
		FormDetail detail = getDetail();
		Class<?> actionOnClass = (Class<?>) getPropertiesMap().getActionOnClass();
		detail.setFieldsObjectFromMaster();
		
		if(ClassHelper.getInstance().isInstanceOf(Product.class, actionOnClass)){
			ProductIdentifiablePages.prepareProductEditFormMaster(detail,actionOnClass);
		}else if(ProductStore.class.equals(actionOnClass)){
			ProductIdentifiablePages.prepareProductStoreEditFormMaster(detail,actionOnClass);
		}else if(SalableProduct.class.equals(actionOnClass)){
			SaleIdentifiableEditPageFormMaster.prepareSalableProduct(detail);
		}else if(SalableProductStore.class.equals(actionOnClass)){
			SaleIdentifiableEditPageFormMaster.prepareSalableProductStore(detail);
		}else if(SalableProductStoreCollection.class.equals(actionOnClass)){						
			SaleIdentifiableEditPageFormMaster.prepareSalableProductStoreCollection(detail,null,Boolean.FALSE);
		}else if(Sale.class.equals(actionOnClass)){
			//SaleIdentifiableEditPageFormMaster.prepareSalableProductCollection(detail,Sale.FIELD_SALABLE_PRODUCT_COLLECTION,Boolean.FALSE);
			SaleIdentifiableEditPageFormMaster.prepareSalableProductStoreCollection(detail,Sale.FIELD_SALABLE_PRODUCT_STORE_COLLECTION,Boolean.FALSE);
		}else if(StockableProduct.class.equals(actionOnClass)){
			StockIdentifiableEditPageFormMaster.prepareStockableProduct(detail);
		}else if(StockableProductStore.class.equals(actionOnClass)){
			StockIdentifiableEditPageFormMaster.prepareStockableProductStore(detail);
		}else if(StockableProductStoresTransfer.class.equals(actionOnClass)){
			StockIdentifiableEditPageFormMaster.prepareStockableProductStoresTransfer(detail, actionOnClass);
		}else if(StockableProductStoresTransferAcknowledgement.class.equals(actionOnClass)){
			StockIdentifiableEditPageFormMaster.prepareStockableProductStoresTransferAcknowledgement(detail, actionOnClass);
		}
	}
	

	
}
