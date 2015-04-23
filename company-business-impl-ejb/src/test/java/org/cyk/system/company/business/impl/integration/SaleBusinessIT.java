package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductBusiness;
import org.cyk.system.company.business.api.accounting.AccountingPeriodProductCategoryBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.accounting.AccountingPeriodProductCategory;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.root.model.pattern.tree.AbstractDataTreeNode;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.joda.time.DateTime;
import org.junit.Assert;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Deployment
    public static Archive<?> createDeployment() {
    	return createRootDeployment();
    } 
    
    @Inject private SaleBusiness saleBusiness;
    @Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject private CashRegisterBusiness cashRegisterBusiness;
    @Inject private ProductBusiness productBusiness;
    @Inject private ProductCategoryBusiness productCategoryBusiness;
    @Inject private AccountingPeriodBusiness accountingPeriodBusiness;
    @Inject private AccountingPeriodProductBusiness accountingPeriodProductBusiness;
    @Inject private AccountingPeriodProductCategoryBusiness accountingPeriodProductCategoryBusiness;
    
    @Inject private OwnedCompanyBusiness ownedCompanyBusiness;
    @Inject private CashierDao cashierDao;
     
    //private IntangibleProduct product;
    private CashRegister cashRegister1,cashRegisterLimited;
    private AccountingPeriod accountingPeriod;
    
    private Cashier cashier;
            
    public void oneFullPayment(){
    	Sale sale = nextSale(1,1,8,0,new String[]{"iprod3","6","tprod1","2"});
    	payment(sale, "10000","1000","10000.00");
    }
    
    public void manyPayments(){
    	Sale sale = nextSale(1,1,8,0,new String[]{"iprod3","4","tprod1","2"});
    	payment(sale, "5000","3000","15000.00");
    	payment(sale, "2000","1000","17000.00");
    	payment(sale, "2000","-1000","19000.00");
    }
    
    @Override
    protected void businesses() {
    	fakeInstallation();
    	
    	cashier = cashierDao.readAll().iterator().next();
    	accountingPeriod = accountingPeriodBusiness.findCurrent();
    	
    	cashRegister1 = new CashRegister();
    	cashRegister1.setCode("CASHIER001");
    	cashRegister1.setOwnedCompany(ownedCompanyBusiness.findDefaultOwnedCompany());
    	create(cashRegister1);
    	
    	cashRegisterLimited = new CashRegister();
    	cashRegisterLimited.setCode("CASHIER002");
    	cashRegisterLimited.setMinimumBalance(new BigDecimal("0"));
    	cashRegisterLimited.setMaximumBalance(new BigDecimal("1000000"));
    	cashRegisterLimited.setOwnedCompany(ownedCompanyBusiness.findDefaultOwnedCompany());
    	create(cashRegisterLimited);
    	
    	Sale sale = nextSale(1,1,8,0,new String[]{"iprod3","6","tprod1","2"});
    	payment(sale, "10000","1000","10000.00");
    	assertProductResults(new String[]{"iprod3","6","9000","tprod1","2","2000"});
    	assertProductCategoryResults(new String[]{"SC","8","11000","EP","0","0","MASS","0","0","EC","8","11000"});
    	assertProductCategoryResults(new String[]{"OG","0","0","CF","0","0","BT","0","0"});
    	assertHighestAndLowestProductCategory(new String[]{"SC","EP","MASS"}, "8",new String[]{"SC"},"0", new String[]{"EP","MASS"});
    	
    	sale = nextSale(1,1,8,30,new String[]{"iprod3","4","tprod1","2"});
    	payment(sale, "5000","3000","15000.00");
    	payment(sale, "2000","1000","17000.00");
    	payment(sale, "2000","-1000","19000.00");
    	assertProductResults(new String[]{"iprod3","10","15000","tprod1","4","4000"});
    	assertProductCategoryResults(new String[]{"SC","14","19000","EP","0","0","MASS","0","0","EC","14","19000"});
    	assertProductCategoryResults(new String[]{"OG","0","0","CF","0","0","BT","0","0"});
    	
    	sale = nextSale(1,1,8,30,new String[]{"iprod1","2","iprod2","1","tprod2","5","tprod3","3"});
    	payment(sale, "11000","0","30000.00");
    	
    	
    	sale = nextSale(1,1,9,30,new String[]{"iprod4","2","iprod5","1","iprod6","3","tprod4","1","tprod5","2","tprod6","1"});
    	payment(sale, "10000","0","40000.00");
    	
    	sale = nextSale(1,2,9,30,new String[]{"iprod7","1","iprod8","1","iprod9","1","tprod7","1","tprod8","3","tprod9","1"});
    	payment(sale, "8000","0","48000.00");
    	
    	sale = nextSale(13,2,9,30,new String[]{"iprod10","1","iprod11","3","iprod12","2","tprod10","4","tprod11","1","tprod12","2"});
    	payment(sale, "13000","0","61000.00");
    	
    	Collection<ProductCategory> productCategories = productCategoryBusiness.findHierarchies();
    	
    	System.out.println("I  - Highest : "+accountingPeriodProductCategoryBusiness.findHighestNumberOfSales(accountingPeriod, productCategories));
    	System.out.println("I  - Lowest : "+accountingPeriodProductCategoryBusiness.findLowestNumberOfSales(accountingPeriod, productCategories));
    	for(ProductCategory productCategory : productCategories){
    		System.out.println("I - "+accountingPeriodProductCategoryBusiness.findByAccountingPeriodByProduct(accountingPeriod, productCategory));
    		Collection<ProductCategory> productCategories2 = new ArrayList<>();
    		for(AbstractDataTreeNode child : productCategory.getChildren()){
    			productCategories2.add((ProductCategory) child);
    			System.out.println("\tII - "+accountingPeriodProductCategoryBusiness.findByAccountingPeriodByProduct(accountingPeriod, (ProductCategory) child));
    			Collection<Product> products = productBusiness.findByCategory(((ProductCategory) child));
    			for(Product product : products){
    				System.out.println("\t\tIII - "+accountingPeriodProductBusiness.findByAccountingPeriodByProduct(accountingPeriod, product));
    			}
    			System.out.println("\tIII  - Highest : "+accountingPeriodProductBusiness.findHighestNumberOfSales(accountingPeriod, products));
            	System.out.println("\tIII  - Lowest : "+accountingPeriodProductBusiness.findLowestNumberOfSales(accountingPeriod, products));
    		}
    		System.out.println("\tII  - Highest : "+accountingPeriodProductCategoryBusiness.findHighestNumberOfSales(accountingPeriod, productCategories2));
        	System.out.println("\tII  - Lowest : "+accountingPeriodProductCategoryBusiness.findLowestNumberOfSales(accountingPeriod, productCategories2));
    		
    	}
    }

    private void payment(Sale sale,String in,String out,String amount,String balance,String totalBalance){
    	SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();
    	saleCashRegisterMovement.setSale(sale); 
    	saleCashRegisterMovement.setAmountIn(new BigDecimal(in==null?amount:in));
    	saleCashRegisterMovement.setAmountOut(new BigDecimal(out==null?balance:out));
    	saleCashRegisterMovement.setCashRegisterMovement(new CashRegisterMovement(null, cashier.getCashRegister(), new BigDecimal(amount), null));
    	
    	if(sale.getIdentifier()==null){
        	saleBusiness.create(sale,saleCashRegisterMovement);	
        	//writeReport(saleBusiness.findReport(Arrays.asList(sale)));
    	}else{
    		saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);	
    	}
    	
    	Assert.assertEquals("Balance",new BigDecimal(balance), sale.getBalance());
    	Assert.assertEquals("Total Balance",new BigDecimal(totalBalance), cashRegisterBusiness.sumBalance());
    		
    }
    
    private void payment(Sale sale,String amount,String balance,String totalBalance){
    	payment(sale, null, null, amount, balance, totalBalance);
    }
    
    private void assertProductResults(String values[]){
    	for(int i=0;i<values.length;i+=3){
	    	String productCode=values[i],numberOfSales=values[i+1],turnover=values[i+2];
	    	AccountingPeriodProduct app = accountingPeriodProductBusiness.findByAccountingPeriodByProduct(accountingPeriod, productBusiness.find(productCode));
	    	assertEquals(productCode+" : Number of Sales",new BigDecimal(numberOfSales), app.getSalesResults().getCount());
	    	assertEquals(productCode+" : Turnover",new BigDecimal(turnover), app.getSalesResults().getTurnover());
    	}
    }
    
    private void assertProductCategoryResults(String values[]){
    	for(int i=0;i<values.length;i+=3){
	    	String productCategoryCode=values[i],numberOfSales=values[i+1],turnover=values[i+2];
	    	AccountingPeriodProductCategory appc = accountingPeriodProductCategoryBusiness.findByAccountingPeriodByProduct(accountingPeriod, productCategoryBusiness.find(productCategoryCode));
	    	assertEquals(productCategoryCode+" : Number of Sales",new BigDecimal(numberOfSales), appc.getSalesResults().getCount());
	    	assertEquals(productCategoryCode+" : Turnover",new BigDecimal(turnover), appc.getSalesResults().getTurnover());
    	}
    }
    
    private void assertHighestAndLowestProductCategory(String[] categoryCodes,String highestValue,String[] highest,String lowestValue,String[] lowest){
    	BigDecimal hv = new BigDecimal(highestValue),lv = new BigDecimal(lowestValue);
    	Collection<ProductCategory> productCategories = new ArrayList<ProductCategory>();
    	for(String code : categoryCodes)
    		productCategories.add(productCategoryBusiness.find(code));
    	
    	Collection<AccountingPeriodProductCategory> results = //new ArrayList<>();
    			accountingPeriodProductCategoryBusiness.findHighestNumberOfSales(accountingPeriod, productCategories);
    	for(AccountingPeriodProductCategory accp : results){
    		for(Integer index=0;index<highest.length;index++)
    			if(accp.getEntity().getCode().equals(highest[index]))
    				assertEquals("Highest", hv, accp.getSalesResults().getCount());
    		
    		for(Integer index=0;index<lowest.length;index++)
    			if(accp.getEntity().getCode().equals(lowest[index]))
    				assertEquals("Lowest", lv, accp.getSalesResults().getCount());
    	}
    	
    	
    }
    
    private Sale nextSale(Integer day,Integer month,Integer hour,Integer minute,String[] products){
    	Sale sale = new Sale();
    	sale.setCashier(cashier);
    	sale.setAccountingPeriod(accountingPeriod);
    	if(day!=null)
    		sale.setDate(new DateTime(new DateTime(accountingPeriod.getPeriod().getFromDate()).getYear(), month, day, hour, minute).toDate());
    	saleProducts(sale, products);
    	return sale;
    }
    
    /*
    private Sale nextSale(String[] products){
    	return nextSale(null, null, null, null,products);
    }*/
    
    private void saleProducts(Sale sale,String[] products){
    	if(products==null)
    		return;
    	for(int i=0;i<products.length;i+=2){
    		SaleProduct saleProduct = saleBusiness.selectProduct(sale, productDao.read(products[i]));
    		saleProduct.setQuantity(new BigDecimal(products[i+1]));
    		saleBusiness.quantifyProduct(sale, saleProduct);
    	}
    		
    }
    
    @Override protected void finds() {}
    @Override protected void create() {}
    @Override protected void delete() {}
    @Override protected void read() {}
    @Override protected void update() {}
    

}
