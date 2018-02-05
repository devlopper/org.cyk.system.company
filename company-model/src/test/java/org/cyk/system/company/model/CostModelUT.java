package org.cyk.system.company.model;

import java.math.BigDecimal;

import org.cyk.utility.test.unit.AbstractUnitTest;
import org.junit.Test;

public class CostModelUT extends AbstractUnitTest {
	private static final long serialVersionUID = 124355073578123984L;

	@Test
    public void convert() {
		System.out.println("CostModelUT.convert() : "+new Cost());
		System.out.println("CostModelUT.convert() : "+new Cost().setNumberOfProceedElements(new BigDecimal(2)));
		System.out.println("CostModelUT.convert() : "+new Cost().setTax(new BigDecimal(3)));
		System.out.println("CostModelUT.convert() : "+new Cost().setNumberOfProceedElements(new BigDecimal(2)).setValue(new BigDecimal(5)));
    }
	
	
	
}
