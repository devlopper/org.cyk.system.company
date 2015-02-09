package org.cyk.system.company.ui.web.primefaces;

import java.io.Serializable;

import javax.servlet.annotation.WebListener;

import org.cyk.ui.api.MenuManager.Type;
import org.cyk.ui.api.UserSession;
import org.cyk.ui.api.command.UIMenu;
import org.cyk.ui.web.primefaces.AbstractContextListener;

@WebListener
public class ContextListener extends AbstractContextListener implements Serializable {

	private static final long serialVersionUID = -9042005596731665575L;
	
	@Override
	public void menu(UserSession session, UIMenu menu, Type type) {
		switch(type){
		case APPLICATION:
			
			break;
		default:break;
		}	
	}
	
}
