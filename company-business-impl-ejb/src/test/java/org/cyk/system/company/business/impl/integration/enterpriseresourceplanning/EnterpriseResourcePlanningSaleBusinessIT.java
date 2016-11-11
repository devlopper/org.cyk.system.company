package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.math.BigDecimal;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.security.UserAccountDao;
import org.junit.Test;

public class EnterpriseResourcePlanningSaleBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private String CASH_REGISTER_001 = "CR001",CASH_REGISTER_002 = "CR002";
    private String SALE_001 = "S001",SALE_002 = "S002";
    private Sale sale;
    private SaleCashRegisterMovement saleCashRegisterMovement;
    
    @Override
    protected void populate() {
    	super.populate();
    	inject(SalableProductBusiness.class).create(TangibleProduct.class, "TP01", "Omo", new BigDecimal("100"));
    	inject(SalableProductBusiness.class).create(TangibleProduct.class, "TP02", "Javel", new BigDecimal("150"));
    	inject(SalableProductBusiness.class).create(TangibleProduct.class, "TP03", "Riz", new BigDecimal("225"));
    }
    
    @Override
    protected void businesses() {
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	
    	create(inject(SaleBusiness.class).instanciateOneRandomly(SALE_001));
    	assertThat("Sale "+SALE_001+" exists", inject(SaleBusiness.class).find(SALE_001)!=null);
    	assertThat("Salable product collection "+SALE_001+" exists", inject(SalableProductCollectionBusiness.class).find(SALE_001)!=null);
    	
    	SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(userAccount, inject(SaleBusiness.class).find(SALE_001), inject(CashRegisterBusiness.class).find(CASH_REGISTER_001));
    	saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(new BigDecimal("5"));
    	create(saleCashRegisterMovement);
    	assertEquals(saleCashRegisterMovement.getCode(),saleCashRegisterMovement.getCashRegisterMovement().getCode());
    }
    
    //@Test
    public void salableProductCollection(){
    	SalableProductCollection salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC001", new Object[][]{
    		new Object[]{"TP01",1},new Object[]{"TP03",2}	
    	}, "550");
    	
    }
    
    @Test
    public void cashRegister002(){
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_002);
    	sale.getBalance().setValue(new BigDecimal("1000"));		
    	create(sale);
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "150", "150", "850");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "200", "350", "650");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "100", "450", "550");
    	saleCashRegisterMovement = companyBusinessTestHelper.updateSaleCashRegisterMovement(saleCashRegisterMovement, "200", "550", "450");
    	companyBusinessTestHelper.deleteSaleCashRegisterMovement(saleCashRegisterMovement, "350","650");
    }
         
}
