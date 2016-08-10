package org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.CashRegisterMovementMode;
import org.cyk.system.company.model.payment.CashRegisterMovementTerm;
import org.cyk.system.company.model.payment.CashRegisterMovementTermCollection;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends org.cyk.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = super.build(userSession);
		addBusinessMenu(userSession,systemMenu,getProductCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getPaymentCommandable(userSession, null));
		return systemMenu;
	}
	
	public Commandable getProductCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable(Product.class, null);
		module.addChild(createListCommandable(TangibleProduct.class, null));
		module.addChild(createListCommandable(IntangibleProduct.class, null));
		/*module.addChild(createListCommandable(ProductCategory.class, null));
		module.addChild(createListCommandable(ProductCollection.class, null));
		module.addChild(createListCommandable(ProductCollectionItem.class, null));*/
		//module.addChild(createListCommandable(TangibleProductInstance.class, null));
		/*module.addChild(createListCommandable(TangibleProductInventory.class, null));*/
		return module;
	}
	
	public Commandable getPaymentCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("payment", null);
		module.addChild(createListCommandable(Cashier.class, null));
		module.addChild(createListCommandable(CashRegister.class, null));
		module.addChild(createListCommandable(CashRegisterMovement.class, null));
		module.addChild(createListCommandable(CashRegisterMovementMode.class, null));
		module.addChild(createListCommandable(CashRegisterMovementTerm.class, null));
		module.addChild(createListCommandable(CashRegisterMovementTermCollection.class, null));
		/*module.addChild(createListCommandable(ProductCategory.class, null));
		module.addChild(createListCommandable(ProductCollection.class, null));
		module.addChild(createListCommandable(ProductCollectionItem.class, null));*/
		//module.addChild(createListCommandable(TangibleProductInstance.class, null));
		/*module.addChild(createListCommandable(TangibleProductInventory.class, null));*/
		return module;
	}
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}
}
