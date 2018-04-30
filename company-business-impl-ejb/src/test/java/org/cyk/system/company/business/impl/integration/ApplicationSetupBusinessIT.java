package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;

import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.security.Installation;
import org.cyk.utility.common.helper.ClassHelper;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(ApplicationBusinessImpl.Listener.class, ApplicationBusinessAdapter.class);
    }
    
    @Override
    protected void businesses() {
    	System.exit(0);
    }  

    /**/
    
    public static class ApplicationBusinessAdapter extends AbstractBusinessIT.ApplicationBusinessAdapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@Override
		public void installationStarted(Installation installation) {
			super.installationStarted(installation);
			installation.setIsCreateAccounts(Boolean.TRUE);
			installation.getApplication().setUniformResourceLocatorFiltered(Boolean.FALSE);
		}
		
    }
}
