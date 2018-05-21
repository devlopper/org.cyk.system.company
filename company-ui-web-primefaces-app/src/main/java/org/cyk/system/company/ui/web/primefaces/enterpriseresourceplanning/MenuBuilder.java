package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionInventory;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransfer;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransferAcknowledgement;
import org.cyk.system.root.model.mathematics.movement.MovementGroup;
import org.cyk.system.root.model.party.Store;
import org.cyk.utility.common.userinterface.command.Menu;

public class MenuBuilder extends org.cyk.ui.web.primefaces.resources.MenuBuilder implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void addMain(Menu menu, Object principal) {
		super.addMain(menu, principal);
		
		menu.addNode("store")
			.addNodeActionListMany(Store.class)
		;
		menu.addNode("product")
			.addNodeActionListMany(Product.class)
		;
		
		menu.addNode("stock")
			.addNodeActionListMany(MovementCollectionInventory.class,MovementGroup.class,MovementCollectionValuesTransfer.class
					,MovementCollectionValuesTransferAcknowledgement.class)
		;
		
		menu.addNode("sale")
			.addNodeActionListMany(Sale.class)
		;
	}
	
	/*@Override
	protected void addNodeIdentifiablesManage(Menu menu) {
		super.addNodeIdentifiablesManage(menu);
		menu.addNode("store")
			.addNodeActionListMany(Store.class)
		;
		menu.addNode("product")
			.addNodeActionListMany(Product.class)
		;
		
		menu.addNode("stock")
			.addNodeActionListMany(MovementCollectionInventory.class,MovementGroup.class,MovementCollectionValuesTransfer.class
					,MovementCollectionValuesTransferAcknowledgement.class)
		;
		
		menu.addNode("sale")
			.addNodeActionListMany(Sale.class)
		;
				
	}*/
	
}
