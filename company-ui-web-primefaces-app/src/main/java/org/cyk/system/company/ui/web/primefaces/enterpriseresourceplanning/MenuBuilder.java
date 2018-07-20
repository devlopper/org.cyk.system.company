package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductFamily;
import org.cyk.system.company.model.product.ProductSize;
import org.cyk.system.company.model.product.ProductType;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionInventory;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransfer;
import org.cyk.system.root.model.mathematics.movement.MovementCollectionValuesTransferAcknowledgement;
import org.cyk.system.root.model.mathematics.movement.MovementGroup;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.model.time.IdentifiablePeriod;
import org.cyk.system.root.model.time.IdentifiablePeriodCollection;
import org.cyk.system.root.model.time.IdentifiablePeriodCollectionType;
import org.cyk.system.root.model.time.IdentifiablePeriodType;
import org.cyk.utility.common.userinterface.command.Menu;

public class MenuBuilder extends org.cyk.ui.web.primefaces.MenuBuilder implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void addMain(Menu menu, Object principal) {
		super.addMain(menu, principal);
		
		menu.addNode("store")
			.addNodeActionListMany(Store.class)
		;
		menu.addNode("product")
			.addNodeActionListMany(Product.class,ProductType.class,ProductCategory.class,ProductFamily.class,ProductSize.class)
		;
		
		menu.addNode("stock")
			.addNodeActionListMany(MovementCollectionInventory.class,MovementGroup.class,MovementCollectionValuesTransfer.class
					,MovementCollectionValuesTransferAcknowledgement.class)
		;
		
		menu.addNode("sale")
			.addNodeActionListMany(Sale.class)
		;
		
		menu.addNode("period")
			.addNodeActionListMany(IdentifiablePeriod.class,IdentifiablePeriodType.class,IdentifiablePeriodCollection.class,IdentifiablePeriodCollectionType.class)
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
