package org.cyk.system.company.business.impl.integration.sale;

import org.cyk.system.company.model.product.TangibleProduct;

public class SaleStockWithOneFiniteStateMachineStateBusinessIT extends AbstractSaleStockWithOneFiniteStateMachineStateBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
	@Override
	protected void stock_1_noTax_input(CreateSaleStockInputParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("2");
		//companyBusinessTestHelper.assertSale(SS1,"","1","2000", "0", "2000","2000");
		companyBusinessTestHelper.assertCustomer(parameters.getCustomerRegistrationCode(),"1","2000","0","0","2000" );
		//companyBusinessTestHelper.assertSaleByCriteria(null,null, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
		//	, new String[]{SS1}, "2000", "0", "2000", "2000", "0");
		
		companyBusinessTestHelper.assertStockableTangibleProduct(TangibleProduct.STOCKING, "1");
	}
	
	@Override
	protected void stock_2_noTax_input(CreateSaleStockInputParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("3");
		//companyBusinessTestHelper.assertSale(SS2,"","1","3500", "0", "3500","3500");
		companyBusinessTestHelper.assertCustomer(parameters.getCustomerRegistrationCode(),"1","3500","0","0","3500" );
		//companyBusinessTestHelper.assertSaleByCriteria(null,null, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE,Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE
		//		,Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
		//	, new String[]{SS1,SS2}, "5500", "0", "5500", "5500", "0");
		
		companyBusinessTestHelper.assertStockableTangibleProduct(TangibleProduct.STOCKING, "4");
	}
	
	@Override
	protected void stock_3_noTax_output(CreateSaleStockOutputParameters parameters) {
		companyBusinessTestHelper.assertSaleFiniteStateMachineStateCount("2");
		//companyBusinessTestHelper.assertSale(SS2,"","1","3500", "0", "3500","3250");
		companyBusinessTestHelper.assertCustomer(CUST7,"1","3500","1","250","3250" );
		//companyBusinessTestHelper.assertSaleByCriteria(null,null, new String[]{Sale.FINITE_STATE_MACHINE_FINAL_STATE_CODE}
		//	, new String[]{SS1,SS2}, "5500", "0", "5500", "5250", "250");
		
		companyBusinessTestHelper.assertStockableTangibleProduct(TangibleProduct.STOCKING, "3");
	}
                
}
