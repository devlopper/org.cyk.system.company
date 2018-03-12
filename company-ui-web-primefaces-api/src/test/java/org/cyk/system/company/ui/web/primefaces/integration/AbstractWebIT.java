package org.cyk.system.company.ui.web.primefaces.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.cyk.ui.web.primefaces.test.automation.SeleniumHelper;
import org.cyk.utility.common.test.Runnable;
import org.cyk.utility.test.integration.ui.web.AbstractIntegrationWebTest;
import org.openqa.selenium.WebDriver;

public abstract class AbstractWebIT extends AbstractIntegrationWebTest {

	private static final long serialVersionUID = 1L;

	protected SeleniumHelper helper;
	//protected Collection<Runnable<?>> runnables = new ArrayList<>();

	{
		helper = SeleniumHelper.getInstance();
		helper.setScheme(scheme);
		helper.setContext(context);
		helper.setHost(host);
		helper.setPort(port);
		authenticationEnabled = Boolean.TRUE;
		users = new String[][]{ {"admin","123"} };
		helper.setContext("company");
	}
	
	@Override
	protected void setDriver(WebDriver driver) {
		super.setDriver(driver);
		helper.setDriver(driver);
	}
	
	@Override
	protected void login(String username,String password) {
		helper.login(username, password);
	}
	
	@Override
	protected void logout(String username) {
		helper.logout(username);
	}
	
	@Override 
	protected void goToLoginPage() {
		helper.goToLoginPage(); 
	}
			
    @Override
	protected void __execute__() {
        getDriver().manage().timeouts().implicitlyWait(helper.getImplicitlyWaitNumberOfMillisecond(), TimeUnit.MILLISECONDS);
        //for(Runnable<?> runnable : runnables)
        //	runnable.run();
	}
    
    /**/

}
