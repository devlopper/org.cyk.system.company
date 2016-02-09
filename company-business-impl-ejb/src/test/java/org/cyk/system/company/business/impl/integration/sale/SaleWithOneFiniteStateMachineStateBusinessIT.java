package org.cyk.system.company.business.impl.integration.sale;

import org.cyk.system.company.model.sale.Sale;

public class SaleWithOneFiniteStateMachineStateBusinessIT extends AbstractSaleWithOneFiniteStateMachineStateBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
	@Override
	protected void _1(CreateSaleParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("1");
		companyBusinessTestHelper.assertSale(S1,Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,"1","2000", "0", "2000","2000");
		companyBusinessTestHelper.assertCurrentAccountingPeriod("1", "2000", "0","2000");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"2", "2000","0","2000");
		companyBusinessTestHelper.assertCustomer(CUST1,"1","2000","0","0","2000" );
		companyBusinessTestHelper.assertSaleByCriteria(null,null, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
			, new String[]{S1}, "2000", "0", "2000", "2000", "0");
		
		//companyBusinessTestHelper.assertStockableTangibleProduct(TP1, "98");
		
	}

	@Override
	protected void _2(CreateSaleCashRegisterMovementParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void _3(CreateSaleCashRegisterMovementParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void noTax4MorePaid1(CreateSaleParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void noTax5MorePaid2(CreateSaleParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void noTax6MorePaid3(CreateSaleParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void noTax7AllPaidNoUnitPrice(CreateSaleParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void noTax8AllPaidUnitPriceButCostValueSet(CreateSaleParameters parameters) {
		// TODO Auto-generated method stub
		
	}
                
}
