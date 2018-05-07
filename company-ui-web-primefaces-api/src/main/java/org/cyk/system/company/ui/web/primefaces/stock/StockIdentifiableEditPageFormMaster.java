package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;

import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.system.company.model.stock.StockableProductStore;
import org.cyk.system.company.model.stock.StockableProductStoresTransfer;
import org.cyk.system.company.model.stock.StockableProductStoresTransferAcknowledgement;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransfer;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransferAcknowledgement;
import org.cyk.ui.web.primefaces.mathematics.movement.MovementIdentifiablePages;
import org.cyk.utility.common.helper.FieldHelper;
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
	
	public static void prepareStockableProductStoresTransfer(Form.Detail detail,Class<?> aClass){
		detail.add(MovementCollectionValuesTransfer.FIELD_SENDER).addBreak();
		detail.add(MovementCollectionValuesTransfer.FIELD_RECEIVER).addBreak();
		
		//ClassHelper.getInstance().map(MovementCollectionValuesTransferItemCollectionEditFormMasterPrepareListener.class, MovementCollectionValuesTransferItemCollectionEditFormMasterPrepareAdapter.class,Boolean.TRUE);
		
		MovementIdentifiablePages.prepareMovementCollectionValuesTransferItemCollectionEditFormMaster(detail
				,FieldHelper.getInstance().buildPath(StockableProductStoresTransfer.FIELD_MOVEMENT_COLLECTION_VALUES_TRANSFER,MovementCollectionValuesTransfer.FIELD_ITEMS));
	}
	
	public static void prepareStockableProductStoresTransferAcknowledgement(Form.Detail detail,Class<?> aClass){
		detail.add(MovementCollectionValuesTransferAcknowledgement.FIELD_TRANSFER).addBreak();
		MovementIdentifiablePages.prepareMovementCollectionValuesTransferItemCollectionEditFormMaster(detail
				,FieldHelper.getInstance().buildPath(StockableProductStoresTransferAcknowledgement.FIELD_TRANSFER, MovementCollectionValuesTransferAcknowledgement.FIELD_ITEMS));
	}
	
}
