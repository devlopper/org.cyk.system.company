package org.cyk.system.company.business.impl.integration.sale;

import java.math.BigDecimal;

public abstract class AbstractSaleStockWithOneFiniteStateMachineStateBusinessIT extends AbstractSaleBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    protected static final String SS1 = "sale_stock_1",SS2 = "sale_stock_2",SS3 = "sale_stock_3",SS4 = "sale_stock_4",SS5 = "sale_stock_5";
    
    @Override
    protected void businesses() {
    	updateAccountingPeriod(new BigDecimal("0.18"), Boolean.TRUE);
    	    	
    	CreateSaleStockInputParameters sp = new CreateSaleStockInputParameters(SS1, null, null, CUST6, "2000", "false","1");
    	createSaleStock(sp);
    	stock_1_noTax_input(sp);
    	
    	sp = new CreateSaleStockInputParameters(SS2, null, null, CUST7, "3500", "false","3");
    	createSaleStock(sp);
    	stock_2_noTax_input(sp);
    	
    	CreateSaleStockOutputParameters ssop = new CreateSaleStockOutputParameters(SS2, null,null, "250","-1");
    	createSaleStock(ssop);
    	stock_3_noTax_output(ssop);
    	
    }
    
    protected abstract void stock_1_noTax_input(CreateSaleStockInputParameters parameters);
    protected abstract void stock_2_noTax_input(CreateSaleStockInputParameters parameters);
    protected abstract void stock_3_noTax_output(CreateSaleStockOutputParameters parameters);
    /*protected abstract void stock_4_noTax_input(CreateSaleStockParameters parameters);
    protected abstract void stock_5_noTax_output(CreateSaleParameters parameters);
    protected abstract void stock_6_noTax_output(CreateSaleParameters parameters);*/
}
