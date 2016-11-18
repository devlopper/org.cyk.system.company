package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.sale.Sale;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
   
    @Override
    protected void businesses() {
    	create(new CashRegister());
    	
    	SaleBusiness saleBusiness = inject(SaleBusiness.class);
    	Sale sale = saleBusiness.instanciateOne();
    	sale.getSalableProductCollection().getCost().setValue(new BigDecimal("10000"));
    	create(sale); 
    	/*
    	CreateReportFileArguments<Sale> arguments = new CreateReportFileArguments<Sale>(CompanyConstant.REPORT_INVOICE, sale);
    	saleBusiness.createReportFile(sale, arguments);
    	inject(RootBusinessTestHelper.class).write(arguments.getFile());
    	*/
    	/*
    	SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness = inject(SaleCashRegisterMovementBusiness.class);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness
    			.instanciateOne(sale,inject(CashRegisterDao.class).readOneRandomly(),Boolean.TRUE);
    	saleCashRegisterMovement.setAmountIn(new BigDecimal("1025"));
    	saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(new BigDecimal("1025"));
    	create(saleCashRegisterMovement); 
    	
    	file = new File();
    	file.setRepresentationType(inject(FileRepresentationTypeDao.class).read(CompanyConstant.REPORT_PAYMENT_RECEIPT));
    	saleCashRegisterMovementBusiness.createFile(saleCashRegisterMovement, file);
    	inject(RootBusinessTestHelper.class).write(file);
    	*/
    	
    }
    
    /* Exceptions */
    

}
