package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.CompanyConstant;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductInstance;
import org.cyk.system.company.model.sale.SalableProductInstanceCashRegister;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.mathematics.machine.FiniteStateMachineAlphabetBusiness;
import org.cyk.system.root.business.api.mathematics.machine.FiniteStateMachineStateBusiness;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachine;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.model.security.Role;
import org.cyk.ui.api.command.AbstractCommandable.Builder;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.AbstractSystemMenuBuilder;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class GiftCardSystemMenuBuilder extends AbstractSystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static GiftCardSystemMenuBuilder INSTANCE;
	
	public static final String ACTION_SELL_GIFT_CARD = "asgc";
	public static final String ACTION_USE_GIFT_CARD = "augc";
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		addBusinessMenu(userSession,systemMenu,getCashierCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getGiftCardCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getSaleCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getReportCommandable(userSession, null));
		return systemMenu;
	}
	
	public Commandable getCashierCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		module = createModuleCommandable(Cashier.class, null);
		if(userSession.hasRole(Role.MANAGER)){
			module.addChild(createListCommandable(Cashier.class, null));
			module.addChild(createListCommandable(CashRegister.class, null));
			module.addChild(createListCommandable(Employee.class, null));
		}
		return module;
	}
	
	public Commandable getGiftCardCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		module = createModuleCommandable(SalableProduct.class, null);
		if(userSession.hasRole(Role.MANAGER)){
			addChild(userSession,module,createListCommandable(SalableProduct.class, null));
			addChild(userSession,module,createListCommandable(SalableProductInstance.class, null));
			addChild(userSession,module,createListCommandable(SalableProductInstanceCashRegister.class, null));
			
			//addChild(userSession,module,createListCommandable(FiniteStateMachineStateLog.class, null));
		}
		
		FiniteStateMachine finiteStateMachine = CompanyBusinessLayer
				.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
		for(FiniteStateMachineAlphabet finiteStateMachineAlphabet : inject(FiniteStateMachineAlphabetBusiness.class).findByMachine(finiteStateMachine))
			if(ArrayUtils.contains(new String[]{CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_SELL,CompanyConstant.GIFT_CARD_WORKFLOW_ALPHABET_USE}
				, finiteStateMachineAlphabet.getCode()))
				;
			else
				addChild(userSession,module,(Commandable) createSelectManyCommandable(SalableProductInstanceCashRegister.class
					, CompanyBusinessLayer.getInstance().getActionUpdateSalableProductInstanceCashRegisterState(),null)
					.addParameter(finiteStateMachineAlphabet).setLabel(finiteStateMachineAlphabet.getName()));
		
		return module;
	}
	
	public Commandable getSaleCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		module = createModuleCommandable(Sale.class, null);
		if(Boolean.TRUE.equals(userSession.getIsAdministrator()) || CompanyBusinessLayer.getInstance().getCashierBusiness().findByPerson((Person) userSession.getUser())!=null){
			//addChild(userSession,module,createListCommandable(Sale.class, null));
			//addChild(userSession,module,createSelectOneCommandable(SaleProductInstance.class, "retour", null));
			addChild(userSession,module,(Commandable) createCreateCommandable(Sale.class, null).setLabel(getText("action.sellgiftcard")).addActionParameter(ACTION_SELL_GIFT_CARD));
			addChild(userSession,module,(Commandable) createCreateCommandable(Sale.class, null).setLabel(getText("action.usegiftcard")).addActionParameter(ACTION_USE_GIFT_CARD));
		}
		if(userSession.hasRole(Role.MANAGER)){
			//module.addChild(Builder.create("field.transfer", null, CompanyWebManager.getInstance().getOutcomeSalableProductInstanceCashRegisterStateLogList()));
			//c.addParameter(CompanyReportRepository.getInstance().getParameterCustomerReportType(), CompanyReportRepository.getInstance().getParameterCustomerReportBalance());
			//c.addParameter(CompanyReportRepository.getInstance().getParameterCustomerBalanceType(), CompanyReportRepository.getInstance().getParameterCustomerBalanceCredence());
		}
		return module;
	}
	
	public Commandable getReportCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = null;
		module = createModuleCommandable("command.report", null);
		if(userSession.hasRole(Role.MANAGER)){
			FiniteStateMachine finiteStateMachine = CompanyBusinessLayer
					.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration().getSalableProductInstanceCashRegisterFiniteStateMachine();
			for(FiniteStateMachineState finiteStateMachineState : inject(FiniteStateMachineStateBusiness.class).findByMachine(finiteStateMachine))
				addChild(userSession,module,(Commandable) Builder.create(null, null, CompanyWebManager.getInstance()
						.getOutcomeSalableProductInstanceCashRegisterStateLogList()).addParameter(finiteStateMachineState).setLabel(finiteStateMachineState.getName()));
		}
		return module;
	}

	public static GiftCardSystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new GiftCardSystemMenuBuilder();
		return INSTANCE;
	}
}
