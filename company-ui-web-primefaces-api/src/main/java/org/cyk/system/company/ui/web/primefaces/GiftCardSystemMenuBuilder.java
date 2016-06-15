package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.root.model.security.Role;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.AbstractSystemMenuBuilder;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class GiftCardSystemMenuBuilder extends AbstractSystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static GiftCardSystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		addBusinessMenu(userSession,systemMenu,getGiftCardCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getSaleCommandable(userSession, null));
		return systemMenu;
	}
	
	public Commandable getGiftCardCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		if(userSession.hasRole(Role.MANAGER)){
			module = createModuleCommandable(SalableProduct.class, null);
			module.addChild(createListCommandable(SalableProduct.class, null));
			module.addChild(createListCommandable(SalableProductInstance.class, null));
			module.addChild(createListCommandable(SalableProductInstanceCashRegister.class, null));
			
			module.addChild(createSelectManyCommandable(SalableProductInstanceCashRegister.class
					, CompanyBusinessLayer.getInstance().getActionProcessSalableProductInstanceCashRegisterWorkFlow(),null));
			
			//FiniteStateMachine finiteStateMachine = CompanyBusinessLayer
			//		.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
			
			//for(FiniteStateMachineState finiteStateMachineState : RootBusinessLayer.getInstance().getFiniteStateMachineStateBusiness().findByMachine(finiteStateMachine))
			//	module.addChild(createCreateManyCommandable(SalableProductInstanceCashRegister.class, null).addParameter(finiteStateMachineState).setLabel(finiteStateMachineState.getName()));
			
			module.addChild(createListCommandable(Employee.class, null));
			module.addChild(createListCommandable(Customer.class, null));
		}
		return module;
	}
	
	public Commandable getSaleCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		if(userSession.hasRole(Role.MANAGER)){
			module = createModuleCommandable(Sale.class, null);
			module.addChild(createListCommandable(Sale.class, null));
			module.addChild(createListCommandable(CashRegister.class, null));
			module.addChild(createListCommandable(Cashier.class, null));
		}
		return module;
	}

	public static GiftCardSystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new GiftCardSystemMenuBuilder();
		return INSTANCE;
	}
}
