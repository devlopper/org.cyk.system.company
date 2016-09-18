package org.cyk.system.company.ui.web.primefaces.iesa;

import java.io.Serializable;

import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.UserSession;

public class PrimefacesManager extends org.cyk.system.company.ui.web.primefaces.adapter.enterpriseresourceplanning.PrimefacesManager implements Serializable {

	private static final long serialVersionUID = -8716834916609095637L;

	@Override
	public SystemMenu getSystemMenu(UserSession userSession) {
		return SystemMenuBuilder.getInstance().build(userSession);
	}
	
}
