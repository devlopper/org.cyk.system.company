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
import org.cyk.system.root.business.impl.RootBusinessLayer;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.party.person.Person;
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
		module = createModuleCommandable(SalableProduct.class, null);
		if(userSession.hasRole(Role.MANAGER)){
			addChild(userSession,module,createListCommandable(SalableProduct.class, null));
			addChild(userSession,module,createListCommandable(SalableProductInstance.class, null));
			addChild(userSession,module,createListCommandable(SalableProductInstanceCashRegister.class, null));
			
			//addChild(createSelectManyCommandable(SalableProductInstanceCashRegister.class
			//		, CompanyBusinessLayer.getInstance().getActionUpdateSalableProductInstanceCashRegisterState(),null));
				
			addChild(userSession,module,createListCommandable(Employee.class, null));
			addChild(userSession,module,createListCommandable(Customer.class, null));
		}
		
		FiniteStateMachine finiteStateMachine = CompanyBusinessLayer
				.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
		for(FiniteStateMachineAlphabet finiteStateMachineAlphabet : RootBusinessLayer.getInstance().getFiniteStateMachineAlphabetBusiness().findByMachine(finiteStateMachine))
			addChild(userSession,module,(Commandable) createSelectManyCommandable(SalableProductInstanceCashRegister.class
					, CompanyBusinessLayer.getInstance().getActionUpdateSalableProductInstanceCashRegisterState(),null)
					.addParameter(finiteStateMachineAlphabet).setLabel(finiteStateMachineAlphabet.getName()));
		
		return module;
	}
	
	public Commandable getSaleCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		module = createModuleCommandable(Sale.class, null);
		if(Boolean.TRUE.equals(userSession.getIsAdministrator()) || CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson((Person) userSession.getUser())!=null){
			addChild(userSession,module,createListCommandable(Sale.class, null));
		}
		if(userSession.hasRole(Role.MANAGER)){
			addChild(userSession,module,createListCommandable(CashRegister.class, null));
			addChild(userSession,module,createListCommandable(Cashier.class, null));
		}
		return module;
	}

	public static GiftCardSystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new GiftCardSystemMenuBuilder();
		return INSTANCE;
	}
}
