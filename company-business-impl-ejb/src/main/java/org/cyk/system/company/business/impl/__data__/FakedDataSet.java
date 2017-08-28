package org.cyk.system.company.business.impl.__data__;

import java.io.Serializable;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.sale.CustomerBusiness;
import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.root.business.api.party.person.PersonRelationshipBusiness;
import org.cyk.system.root.business.impl.DataSet;
import org.cyk.system.root.model.RootConstant;
import org.cyk.system.root.model.party.person.PersonRelationship;
import org.cyk.utility.common.helper.RandomHelper;

public class FakedDataSet extends DataSet implements Serializable {

	private static final long serialVersionUID = 2282674526022995453L;
	
	public static final String CASH_REGISTER_001 = RandomHelper.getInstance().get(String.class),CASH_REGISTER_002 = RandomHelper.getInstance().get(String.class)
			,CASH_REGISTER_003 = RandomHelper.getInstance().get(String.class);
	public static final String SALE_001 = RandomHelper.getInstance().get(String.class),SALE_002 = RandomHelper.getInstance().get(String.class);
	public static final String CUSTOMER_001 = RandomHelper.getInstance().get(String.class),CUSTOMER_002 = RandomHelper.getInstance().get(String.class);
	public static final String CUSTOMER_001_FATHER = RandomHelper.getInstance().get(String.class),CUSTOMER_002_MOTHER = RandomHelper.getInstance().get(String.class);
	public static final String CUSTOMER_003_FATHER = RandomHelper.getInstance().get(String.class),CUSTOMER_004_MOTHER = RandomHelper.getInstance().get(String.class);
	
	public FakedDataSet() {
		super(CompanyBusinessLayer.class);
		addInstances(CashRegister.class, inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
		addInstances(CashRegister.class, inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
		addInstances(CashRegister.class, inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_003));
		
		addInstances(Customer.class, inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_001));
		addInstances(Customer.class, inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_002));
		
		addInstances(Customer.class, inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_001_FATHER));
		addInstances(Customer.class, inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_002_MOTHER));
		addInstances(Customer.class, inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_003_FATHER));
		addInstances(Customer.class, inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_004_MOTHER));
		
		addInstances(PersonRelationship.class,
				inject(PersonRelationshipBusiness.class).instanciateOne(CUSTOMER_001_FATHER, RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_FATHER, CUSTOMER_001
						,RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_SON)
				,inject(PersonRelationshipBusiness.class).instanciateOne(CUSTOMER_002_MOTHER, RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_MOTHER, CUSTOMER_001
						,RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_SON)
				,inject(PersonRelationshipBusiness.class).instanciateOne(CUSTOMER_003_FATHER, RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_FATHER, CUSTOMER_002
						,RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_DAUGHTER)
				,inject(PersonRelationshipBusiness.class).instanciateOne(CUSTOMER_004_MOTHER, RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_MOTHER, CUSTOMER_002
						,RootConstant.Code.PersonRelationshipTypeRole.FAMILY_PARENT_DAUGHTER)
				);
	}
	
	protected void createCustomers(){
		/*
		Customer customer = null;
		
    	customer = inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_001_FATHER);
    	customer.setName("Komenan");
    	customer.getPerson().setName("Komenan");
    	customer.getPerson().setLastnames("Yao Christian");
    	create(customer);
    	
    	customer = inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_002_MOTHER);
    	customer.setName("Gnangnan");
    	customer.getPerson().setName("Gnangnan");
    	customer.getPerson().setLastnames("Sandrine Meliane");
    	create(customer);
    	
    	customer = inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_003_FATHER);
    	customer.setName("Zadi");
    	customer.getPerson().setName("Zadi");
    	customer.getPerson().setLastnames("Yves");
    	create(customer);
    	
    	customer = inject(CustomerBusiness.class).instanciateOneRandomly(CUSTOMER_004_MOTHER);
    	customer.setName("Koudou");
    	customer.getPerson().setName("Koudou");
    	customer.getPerson().setLastnames("zouzou léa");
    	create(customer);
    	*/
	}
	
}
