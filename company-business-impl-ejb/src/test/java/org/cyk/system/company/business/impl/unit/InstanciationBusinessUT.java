package org.cyk.system.company.business.impl.unit;

import java.util.Collection;

import org.cyk.system.company.business.impl.model.Sale;
import org.cyk.system.company.business.impl.sale.SaleBusinessImpl;
import org.cyk.utility.common.ObjectFieldValues;
import org.junit.Test;
import org.mockito.InjectMocks;

public class InstanciationBusinessUT extends AbstractBusinessUT {

	private static final long serialVersionUID = 124355073578123984L;

	@InjectMocks private SaleBusinessImpl saleBusiness;
	
	@Override
	protected void registerBeans(Collection<Object> collection) {
		super.registerBeans(collection);
		collection.add(saleBusiness);
	}
	
	@Test
	public void sale() {
		ObjectFieldValues objectFieldValues = new ObjectFieldValues(Sale.class)
		.set(Sale.FIELD_FINITE_STATE_MACHINE_STATE,"myfinalstate")
		//.setBaseName(Sale.FIELD_COST).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,"12",Cost.FIELD_VALUE, "1500",Cost.FIELD_TAX, "3.18",Cost.FIELD_TURNOVER, "12000")
		//.setBaseName(Sale.FIELD_BALANCE).set(Balance.FIELD_VALUE,"20000")
		;
		
		/*Assert.assertNotNull(phoneNumberTypeBusiness.instanciateOne());
		assertEquals(phoneNumberTypeBusiness.instanciateOne("Land"), new ObjectFieldValues(PhoneNumberType.class)
			.set(PhoneNumberType.FIELD_CODE, "Land").set(PhoneNumberType.FIELD_NAME, "Land"));
		assertEquals(phoneNumberTypeBusiness.instanciateOne("My Type"), new ObjectFieldValues(PhoneNumberType.class)
			.set(PhoneNumberType.FIELD_CODE, "MyType").set(PhoneNumberType.FIELD_NAME, "My Type"));
		
		ObjectFieldValues objectFieldValues = new ObjectFieldValues(PhoneNumberType.class).set(PhoneNumberType.FIELD_CODE, "YaYA")
				.set(PhoneNumberType.FIELD_NAME, "My Name");
		assertEquals(phoneNumberTypeBusiness.instanciateOne(objectFieldValues), objectFieldValues);
		
		objectFieldValues = new ObjectFieldValues(PhoneNumberType.class)
				.set(PhoneNumberType.FIELD_NAME, "My Name");
		assertEquals(phoneNumberTypeBusiness.instanciateOne(objectFieldValues), new ObjectFieldValues(PhoneNumberType.class)
				.set(PhoneNumberType.FIELD_NAME, "My Name"));*/
	}
	
	/**/
	
	

}
