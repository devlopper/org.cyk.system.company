package org.cyk.system.company.ui.web.primefaces.adapter.erp;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInstance;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.AbstractSystemMenuBuilder;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends AbstractSystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		addBusinessMenu(userSession,systemMenu,getProductCommandable(userSession, null));
		return systemMenu;
	}
	
	public Commandable getProductCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable(Product.class, null);
		module.addChild(createListCommandable(TangibleProduct.class, null));
		module.addChild(createListCommandable(IntangibleProduct.class, null));
		/*module.addChild(createListCommandable(ProductCategory.class, null));
		module.addChild(createListCommandable(ProductCollection.class, null));
		module.addChild(createListCommandable(TangibleProductInstance.class, null));
		module.addChild(createListCommandable(TangibleProductInventory.class, null));
		*/
		return module;
	}
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}
}
