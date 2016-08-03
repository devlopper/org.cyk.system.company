package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.adapter.erp.AbstractContextListener;
import org.cyk.system.company.ui.web.primefaces.adapter.erp.ErpPrimefacesManagerAdapter;
import org.cyk.system.company.ui.web.primefaces.adapter.giftcard.GiftCardPrimefacesManagerAdapter;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		CompanyWebManager.getInstance().getListeners().add(new ErpPrimefacesManagerAdapter());

		//CompanyWebManager.getInstance().getListeners().add(new GiftCardPrimefacesManagerAdapter());
	}
	
}
