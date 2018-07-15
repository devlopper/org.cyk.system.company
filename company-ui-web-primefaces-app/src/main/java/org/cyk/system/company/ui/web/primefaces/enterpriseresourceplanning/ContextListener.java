package org.cyk.system.company.ui.web.primefaces.enterpriseresourceplanning;

import java.io.Serializable;

import org.cyk.system.company.ui.web.primefaces.ServletContextListener;
import org.cyk.utility.common.security.Shiro;

@javax.servlet.annotation.WebListener
public class ContextListener extends ServletContextListener implements Serializable {
	private static final long serialVersionUID = -9042005596731665575L;
	
	protected void __addFoldersForUser__(Shiro.Ini ini){
		ini.addFoldersForUser("private1");
	}
	
	@Override
	protected Class<?> __getMenuBuilderClass__() {
		return MenuBuilder.class;
	}
	
}
