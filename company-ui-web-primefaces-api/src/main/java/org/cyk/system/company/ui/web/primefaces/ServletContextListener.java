package org.cyk.system.company.ui.web.primefaces;
import java.io.Serializable;

import javax.servlet.ServletContextEvent;

import org.cyk.utility.common.helper.ClassHelper;

public class ServletContextListener extends org.cyk.ui.web.primefaces.ServletContextListener implements Serializable {
	private static final long serialVersionUID = -3211898049670089807L;

	@Override
	public void __contextInitialized__(ServletContextEvent event) {
		super.__contextInitialized__(event);
		ClassHelper.getInstance().map(org.cyk.ui.web.primefaces.resources.page.controlpanel.IdentifiableListPage.DataTable.class, IdentifiableListPageDataTable.class);
		ClassHelper.getInstance().map(org.cyk.ui.web.primefaces.resources.page.controlpanel.IdentifiableEditPage.FormMaster.class, IdentifiableEditPageFormMaster.class);
		ClassHelper.getInstance().map(org.cyk.ui.web.primefaces.resources.page.controlpanel.IdentifiableConsultPage.FormMaster.class, IdentifiableConsultPageFormMaster.class);
		ClassHelper.getInstance().map(org.cyk.utility.common.userinterface.collection.DataTable.Listener.class, DataTable.Listener.class);
	}
	
	@Override
	protected Class<?> __getMenuBuilderClass__() {
		return MenuBuilder.class;
	}
	
}
