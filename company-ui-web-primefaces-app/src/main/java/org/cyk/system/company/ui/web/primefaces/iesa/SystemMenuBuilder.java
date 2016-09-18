package org.cyk.system.company.ui.web.primefaces.iesa;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.structure.Employee;
import org.cyk.system.company.model.structure.Vehicle;
import org.cyk.system.root.model.message.SmtpProperties;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.Commandable;
import org.cyk.ui.web.primefaces.UserSession;

public class SystemMenuBuilder extends org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.SystemMenuBuilder implements Serializable {

	private static final long serialVersionUID = 6995162040038809581L;

	private static SystemMenuBuilder INSTANCE;
	
	@Override
	public SystemMenu build(UserSession userSession) {
		SystemMenu systemMenu = new SystemMenu();
		addBusinessMenu(userSession,systemMenu,getEmployeeCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getFinanceCommandable(userSession, null));
		addBusinessMenu(userSession,systemMenu,getServiceCommandable(userSession, null));
		return systemMenu;
	}
	
	public Commandable getEmployeeCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("commandable.personel.management", null);
		module.setLabel("Gestion du personnel");
		module.addChild(createListCommandable(Employee.class, null));
		module.addChild(createSelectOneCommandable(Employee.class, inject(CompanyBusinessLayer.class).getActionPrintEmployeeEmploymentContract(), null));
		module.addChild(createSelectOneCommandable(Employee.class, inject(CompanyBusinessLayer.class).getActionPrintEmployeeWorkCertificate(), null));
		module.addChild(createSelectOneCommandable(Employee.class, inject(CompanyBusinessLayer.class).getActionPrintEmployeeEmploymentCertificate(), null));
		return module;
	}
	
	public Commandable getFinanceCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("commandable.finance.management", null);
		module.setLabel("Gestion financière");
		module.addChild(createListCommandable(Sale.class, null));
		
		return module;
	}
	
	public Commandable getServiceCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("commandable.service.management", null);
		module.setLabel("Gestion des services");
		module.addChild(createListCommandable(Vehicle.class, null));
		
		return module;
	}
	
	public Commandable getMessageCommandable(UserSession userSession,Collection<UICommandable> mobileCommandables){
		Commandable module = createModuleCommandable("commandable.notification.management", null);
		module.setLabel("Gestion des notifications");
		module.addChild(createListCommandable(SmtpProperties.class, null));
		
		return module;
	}

	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}
}
