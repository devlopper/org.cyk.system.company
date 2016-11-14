package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.root.model.security.UserAccount;
import org.cyk.system.root.persistence.api.security.UserAccountDao;
import org.junit.Test;

public class EnterpriseResourcePlanningSaleBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private String CASH_REGISTER_001 = "CR001",CASH_REGISTER_002 = "CR002",CASH_REGISTER_003 = "CR003";
    private String SALE_001 = "S001",SALE_002 = "S002";
    private Sale sale;
    private SaleCashRegisterMovement saleCashRegisterMovement;
    private SalableProductCollection salableProductCollection;
    private SalableProductCollectionItem salableProductCollectionItem;
    
    @Override
    protected void populate() {
    	super.populate();
    	inject(SalableProductBusiness.class).create(TangibleProduct.class, "TP01", "Omo", new BigDecimal("100"));
    	inject(SalableProductBusiness.class).create(TangibleProduct.class, "TP02", "Javel", new BigDecimal("150"));
    	inject(SalableProductBusiness.class).create(TangibleProduct.class, "TP03", "Riz", new BigDecimal("225"));
    	
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_003));
    }
    
    //@Test
    public void crudSalableProductCollection(){
    	salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC001", new Object[][]{
    		{"TP01",1},{"TP03",2}	
    	}, "550");
    	
    	companyBusinessTestHelper.deleteSalableProductCollection(salableProductCollection);
    }
    
    //@Test
    public void crudSalableProductCollectionItem(){
    	salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC002", new Object[][]{}, "0");
    	SalableProductCollectionItem salableProductCollectionItemTP01 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP01",1}, "100");
    	SalableProductCollectionItem salableProductCollectionItemTP02 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP02",2}, "400");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP01, "300");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",3}, "975");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP02, "675");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",1}, "900");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItem, "675");
    }
    
    @Test
    public void crudSale(){
    	sale = companyBusinessTestHelper.createSale(null, new Object[][]{{"TP01",1},{"TP03",2}}, "550", "550");
    	//sale = companyBusinessTestHelper.createSale(null, new Object[][]{}, "0", "0");
    	String saleCode = sale.getCode();
    	assertThat("Sale "+saleCode+" exists", inject(SaleBusiness.class).find(saleCode)!=null);
    	assertThat("Salable product collection "+saleCode+" exists", inject(SalableProductCollectionBusiness.class).find(saleCode)!=null);
    	assertThat("Sale code start with FACT", StringUtils.startsWith(saleCode, "FACT"));
    	
    	companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "150", "400","150");
    	companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "200", "200","350");
    	
    	companyBusinessTestHelper.deleteSale(sale);
    }
    
    //@Test
    public void crudSaleCashRegisterMovement(){
    	UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_001);
    	create(sale);
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(userAccount, inject(SaleBusiness.class).find(SALE_001), inject(CashRegisterBusiness.class).find(CASH_REGISTER_001));
    	saleCashRegisterMovement.getCashRegisterMovement().getMovement().setValue(new BigDecimal("5"));
    	create(saleCashRegisterMovement);
    	assertEquals(saleCashRegisterMovement.getCode(),saleCashRegisterMovement.getCashRegisterMovement().getCode());
    	assertThat("saleCashRegisterMovement code start with PAIE", StringUtils.startsWith(saleCashRegisterMovement.getCode(), "PAIE"));
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_002);	
    	sale.getSalableProductCollection().getCost().setValue(new BigDecimal("1000"));
    	create(sale);
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "150", "850","150");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "200", "650", "350");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "100", "550", "450");
    	saleCashRegisterMovement = companyBusinessTestHelper.updateSaleCashRegisterMovement(saleCashRegisterMovement, "200", "450", "550");
    	companyBusinessTestHelper.deleteSaleCashRegisterMovement(saleCashRegisterMovement,"650", "350");
    }
         
}
