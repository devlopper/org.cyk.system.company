package org.cyk.system.company.ui.web.primefaces;
import java.io.Serializable;

public class ServletContextListener extends org.cyk.ui.web.primefaces.ServletContextListener implements Serializable {
	private static final long serialVersionUID = -3211898049670089807L;

	@Override
	protected Class<?> __getMenuBuilderClass__() {
		return MenuBuilder.class;
	}
	
}
