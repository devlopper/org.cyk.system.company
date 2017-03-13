package org.cyk.system.company.business.impl.iesa;

import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementCollectionBusiness;

public class IesaPopulate extends AbstractIesaBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void _execute_() {
    	super._execute_();
    	companyBusinessTestHelper.create(inject(SaleBusiness.class).instanciateOne("SCHOOLFEES001",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{{"TP05",2},{"TP06",1},{"IP04",3},{"IP05",3},{"IP06",3},{"IP07",10}}));    	
    	companyBusinessTestHelper.create(inject(SaleBusiness.class).instanciateOne("SCHOOLFEES002",IesaFakedDataProducer.CUSTOMER_001, new Object[][]{{"IP01",1},{"IP02",1,100000},{"TP01",1},{"TP02",1},{"TP03",1},{"TP04",1},{"IP03",1}}));
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("p001",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new String[][]{{"SCHOOLFEES001","1000"},{"SCHOOLFEES002","500"}}));
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("p002",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new String[][]{{"SCHOOLFEES001","1500"},{"SCHOOLFEES002","800"}}));
    	
    	companyBusinessTestHelper.create(inject(SaleCashRegisterMovementCollectionBusiness.class).instanciateOne("p003",null, IesaFakedDataProducer.CASH_REGISTER_001
    			, new String[][]{{"SCHOOLFEES001","2100"},{"SCHOOLFEES002","900"}}));
    	
		System.exit(0);
    }
    
    
         
}
