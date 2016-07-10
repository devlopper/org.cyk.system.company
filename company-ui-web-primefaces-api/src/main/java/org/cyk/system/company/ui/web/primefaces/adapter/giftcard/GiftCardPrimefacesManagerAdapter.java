package org.cyk.system.company.ui.web.primefaces.adapter.giftcard;

import java.io.Serializable;

import org.cyk.system.company.ui.web.primefaces.GiftCardSystemMenuBuilder;
import org.cyk.ui.api.command.menu.SystemMenu;
import org.cyk.ui.web.primefaces.AbstractPrimefacesManager;
import org.cyk.ui.web.primefaces.UserSession;

public class GiftCardPrimefacesManagerAdapter extends AbstractPrimefacesManager.AbstractPrimefacesManagerListener.Adapter implements Serializable {

	private static final long serialVersionUID = -8716834916609095637L;
	
	@Override
	public SystemMenu getSystemMenu(UserSession userSession) {
		return GiftCardSystemMenuBuilder.getInstance().build(userSession);
	}
}
