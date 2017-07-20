package org.cyk.system.company.business.impl.integration.enterpriseresourceplanning;

import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.junit.Test;

public class EnterpriseResourcePlanningSaleBusinessIT extends AbstractEnterpriseResourcePlanningBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    private String CASH_REGISTER_001 = "CR001",CASH_REGISTER_002 = "CR002",CASH_REGISTER_003 = "CR003";
    //private String SALE_001 = "S001",SALE_002 = "S002";
    /*private Sale sale;
    private SaleCashRegisterMovement saleCashRegisterMovement;
    private SalableProductCollection salableProductCollection;
    private SalableProductCollectionItem salableProductCollectionItem;
    */
    @Override
    protected void populate() {
    	super.populate();
    	create(inject(TangibleProductBusiness.class).instanciateMany(new String[][]{{"TP01","Omo"},{"TP02", "Javel"},{"TP03", "Riz"}}));
    	create(inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{{"IP01","Nettoyage"},{"IP02", "Surveillance"},{"IP03", "Conseil"}}));
    	create(inject(SalableProductBusiness.class).instanciateMany(new String[][]{{"","","","","","","","","","","TP01","100"}
    	,{"","","","","","","","","","","TP02", "150"},{"","","","","","","","","","","TP03", "225"},{"","","","","","","","","","","IP02", "500"}}));
    	
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_001));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_002));
    	create(inject(CashRegisterBusiness.class).instanciateOneRandomly(CASH_REGISTER_003));
    }
    
    @Test
    public void crudSalableProductCollection(){
    	/*salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC001", new Object[][]{
    		{"TP01",1},{"TP03",2}	
    	}, "550");*/
    	
    	//companyBusinessTestHelper.deleteSalableProductCollection(salableProductCollection);
    }
    
    @Test
    public void crudSalableProductCollectionItem(){
    	/*salableProductCollection = companyBusinessTestHelper.createSalableProductCollection("SPC002", new Object[][]{}, "0");
    	SalableProductCollectionItem salableProductCollectionItemTP01 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP01",1}, "100");
    	SalableProductCollectionItem salableProductCollectionItemTP02 = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP02",2}, "400");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP01, "300");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",3}, "975");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItemTP02, "675");
    	salableProductCollectionItem = companyBusinessTestHelper.createSalableProductCollectionItem("SPC002", new Object[]{"TP03",1}, "900");
    	salableProductCollectionItem = companyBusinessTestHelper.deleteSalableProductCollectionItem(salableProductCollectionItem, "675");*/
    }
    
    @Test
    public void crudSale(){
    	/*sale = companyBusinessTestHelper.createSale(null,null, new Object[][]{{"TP01",1},{"TP03",2}}, "550", "550");
    	//sale = companyBusinessTestHelper.createSale(null, new Object[][]{}, "0", "0");
    	String saleCode = sale.getCode();
    	assertThat("Sale "+saleCode+" exists", inject(SaleBusiness.class).find(saleCode)!=null);
    	assertThat("Salable product collection "+saleCode+" exists", inject(SalableProductCollectionBusiness.class).find(saleCode)!=null);
    	assertThat("Sale code start with FACT", StringUtils.startsWith(saleCode, "FACT"));
    	
    	companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "150",null, "400","150");
    	companyBusinessTestHelper.createSaleCashRegisterMovement(saleCode, CASH_REGISTER_003, "200",null, "200","350");
    	
    	companyBusinessTestHelper.deleteSale(sale);
    	*/
    }
    
    @Test
    public void crudSaleCashRegisterMovement(){
    	/*UserAccount userAccount = inject(UserAccountDao.class).readOneRandomly();
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_001);
    	create(sale);
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(userAccount, inject(SaleBusiness.class).find(SALE_001), inject(CashRegisterBusiness.class).find(CASH_REGISTER_001));
    	saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().setValue(new BigDecimal("5"));
    	create(saleCashRegisterMovement);
    	assertEquals(saleCashRegisterMovement.getCode(),saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCode());
    	assertThat("saleCashRegisterMovement code start with PAIE", StringUtils.startsWith(saleCashRegisterMovement.getCode(), "PAIE"));
    	
    	sale = inject(SaleBusiness.class).instanciateOneRandomly(SALE_002);	
    	sale.getSalableProductCollection().getCost().setValue(new BigDecimal("1000"));
    	create(sale);
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "150",null, "850","150");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "200",null, "650", "350");
    	saleCashRegisterMovement = companyBusinessTestHelper.createSaleCashRegisterMovement(SALE_002, CASH_REGISTER_002, "100",null, "550", "450");
    	saleCashRegisterMovement = companyBusinessTestHelper.updateSaleCashRegisterMovement(saleCashRegisterMovement, "200", "450", "550");
    	companyBusinessTestHelper.deleteSaleCashRegisterMovement(saleCashRegisterMovement,"650", "350");
    	*/
    }
         
}
