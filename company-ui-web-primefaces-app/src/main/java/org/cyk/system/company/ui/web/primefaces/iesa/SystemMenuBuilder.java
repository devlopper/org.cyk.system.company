package org.cyk.system.company.ui.web.primefaces.iesa;

import java.io.Serializable;

import org.cyk.ui.api.command.menu.SystemMenu;
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
	
	public static SystemMenuBuilder getInstance(){
		if(INSTANCE==null)
			INSTANCE = new SystemMenuBuilder();
		return INSTANCE;
	}
}
