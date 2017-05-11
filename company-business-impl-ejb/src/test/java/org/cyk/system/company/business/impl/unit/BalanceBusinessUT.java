package org.cyk.system.company.business.impl.unit;

import java.math.BigDecimal;
import java.util.Collection;

import org.cyk.system.company.business.impl.BalanceBusinessImpl;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.utility.common.helper.FieldHelper;
import org.cyk.utility.test.unit.AbstractUnitTest;
import org.junit.Test;
import org.mockito.InjectMocks;

public class BalanceBusinessUT extends AbstractUnitTest {

	private static final long serialVersionUID = 124355073578123984L;

	@InjectMocks private BalanceBusinessImpl balanceBusiness;
	@InjectMocks private FieldHelper fieldHelper;
	
	@Override
	protected void registerBeans(Collection<Object> collection) {
		super.registerBeans(collection);
		collection.add(balanceBusiness);
		collection.add(fieldHelper);
	}

	@Test
    public void computeCurrent() {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateSaleCashRegisterMovement();
		
		/*Balance balance = balanceBusiness.compute(saleCashRegisterMovement);
		assertBigDecimalValue("Value", "10", balance.getValue());*/
    }
	
	@Test
    public void computePrevious() {
		SaleCashRegisterMovement saleCashRegisterMovement = instanciateSaleCashRegisterMovement();
		
		/*Balance balance = balanceBusiness.compute(saleCashRegisterMovement);
		assertBigDecimalValue("Value", "10", balance.getValue());*/
    }
	
	private SaleCashRegisterMovement instanciateSaleCashRegisterMovement(){
		SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
		saleCashRegisterMovement.setCollection(new SaleCashRegisterMovementCollection());
		
		fieldHelper.set(saleCashRegisterMovement, new BigDecimal("10"), SaleCashRegisterMovement.FIELD_SALE, Sale.FIELD_SALABLE_PRODUCT_COLLECTION
				,SalableProductCollection.FIELD_COST,Cost.FIELD_VALUE);
		
		fieldHelper.set(saleCashRegisterMovement, SaleCashRegisterMovement.FIELD_COLLECTION, SaleCashRegisterMovementCollection.FIELD_CASH_REGISTER_MOVEMENT
				,CashRegisterMovement.FIELD_MOVEMENT,Movement.FIELD_VALUE);
		
		return saleCashRegisterMovement;
	}
	
}
