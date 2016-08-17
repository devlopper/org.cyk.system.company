package org.cyk.system.company.business.impl.unit;

import java.math.BigDecimal;

import org.cyk.system.root.business.api.AbstractBusinessException;
import org.cyk.system.root.business.api.mathematics.NumberBusiness;
import org.cyk.utility.common.ObjectFieldValues;
import org.cyk.utility.common.test.TestEnvironmentListener;
import org.cyk.utility.test.unit.AbstractUnitTest;
import org.junit.Assert;

public class AbstractBusinessUT extends AbstractUnitTest {

	private static final long serialVersionUID = 124355073578123984L;

	static {
		TestEnvironmentListener.COLLECTION.add(new TestEnvironmentListener.Adapter.Default(){
			private static final long serialVersionUID = -2347039842308401189L;
			@Override
			protected Throwable getThrowable(Throwable throwable) {
				return commonUtils.getThrowableInstanceOf(throwable, AbstractBusinessException.class);
			}
			@Override
    		public void assertEquals(String message, Object expected, Object actual) {
    			Assert.assertEquals(message, expected, actual);
    		}
    		@Override
    		public String formatBigDecimal(BigDecimal value) {
    			return inject(NumberBusiness.class).format(value);
    		}
    	});
	}
	
	protected void assertEquals(Object actual,ObjectFieldValues expected){
		for(TestEnvironmentListener listener : TestEnvironmentListener.COLLECTION)
			listener.assertEquals(actual, expected);
	}
	
}
