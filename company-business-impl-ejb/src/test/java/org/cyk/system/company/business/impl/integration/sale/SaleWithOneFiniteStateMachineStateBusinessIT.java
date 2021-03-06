package org.cyk.system.company.business.impl.integration.sale;


public class SaleWithOneFiniteStateMachineStateBusinessIT extends AbstractSaleWithOneFiniteStateMachineStateBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("0");
		assertEquals("Number of sales", 0l, saleDao.countAll());
		companyBusinessTestHelper.assertCurrentAccountingPeriod("0", "0", "0","0");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"0", "0","0","0");
		companyBusinessTestHelper.assertCustomer(CUST1,"0","0","0","0","0" );
		
    	super.businesses();
    	
    }
    
	@Override
	protected void _1(CreateSaleParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("1");
		//companyBusinessTestHelper.assertSale(S1,Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,"1","2000", "0", "2000","2000");
		companyBusinessTestHelper.assertCurrentAccountingPeriod("1", "2000", "0","2000");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"2", "2000","0","2000");
		companyBusinessTestHelper.assertCustomer(CUST1,"1","2000","0","0","2000" );
		//companyBusinessTestHelper.assertSaleByCriteria(null,null, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
		//	, new String[]{S1}, "2000", "0", "2000", "2000", "0");
		
		//companyBusinessTestHelper.assertStockableProduct(TP1, "98");
		
	}

	@Override
	protected void _2(DeleteSaleParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("0");
		assertEquals("Number of sales", 0l, saleDao.countAll());
		companyBusinessTestHelper.assertCurrentAccountingPeriod("0", "0", "0","0");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"0", "0","0","0");
		companyBusinessTestHelper.assertCustomer(CUST1,"0","0","0","0","0" );
	}

	@Override
	protected void _3(CreateSaleParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("1");
		//companyBusinessTestHelper.assertSale(S1,Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,"1","2000", "0", "2000","2000");
		companyBusinessTestHelper.assertCurrentAccountingPeriod("1", "2000", "0","2000");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"2", "2000","0","2000");
		companyBusinessTestHelper.assertCustomer(CUST1,"1","2000","0","0","2000" );
		//companyBusinessTestHelper.assertSaleByCriteria(null,null, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
		//	, new String[]{S1}, "2000", "0", "2000", "2000", "0");
	}
	
	@Override
	protected void _4(CreateSaleCashRegisterMovementParameters parameters) {
		
	}
	
	@Override
	protected void _5(DeleteSaleParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("0");
		assertEquals("Number of sales", 0l, saleDao.countAll());
		companyBusinessTestHelper.assertCurrentAccountingPeriod("0", "0", "0","0");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"0", "0","0","0");
		companyBusinessTestHelper.assertCustomer(CUST1,"0","0","0","0","0" );
	}
	
	@Override
	protected void _6(CreateSaleParameters parameters) {
		
	}
	
	@Override
	protected void _7(CreateSaleCashRegisterMovementParameters parameters) {
	
	}
	
	@Override
	protected void _8(CreateSaleCashRegisterMovementParameters parameters) {
		
	}
	
	@Override
	protected void _9(DeleteSaleParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("0");
		assertEquals("Number of sales", 0l, saleDao.countAll());
		companyBusinessTestHelper.assertCurrentAccountingPeriod("0", "0", "0","0");
		companyBusinessTestHelper.assertCurrentAccountingPeriodProduct(TP1,"0", "0","0","0");
		companyBusinessTestHelper.assertCustomer(CUST1,"0","0","0","0","0" );	
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
