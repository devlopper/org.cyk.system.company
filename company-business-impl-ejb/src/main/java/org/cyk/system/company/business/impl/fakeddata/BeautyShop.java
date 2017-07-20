package org.cyk.system.company.business.impl.fakeddata;

import java.io.Serializable;

import org.cyk.utility.common.cdi.AbstractBean;

public class BeautyShop extends AbstractBean implements Serializable {

	private static final long serialVersionUID = -4180196113740218243L;

	/*@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private ProductCategoryBusiness productCategoryBusiness;
	
	public void data(){
		
		ProductCategory productCategoryEntretienCoprs = productCategory(null, "EC", "Entretien du corps");
        ProductCategory productCategoryBeaute = productCategory(null, "BT", "Beaute");
        
        ProductCategory productCategorySoindDuCorps = productCategory(productCategoryEntretienCoprs, "SC", "Soin du coprs");
        ProductCategory productCategoryEpilation = productCategory(productCategoryEntretienCoprs, "EP", "Epilation");
        ProductCategory productCategoryMassage = productCategory(productCategoryEntretienCoprs, "MASS", "Massage");

        ProductCategory productCategoryOnglerie = productCategory(productCategoryBeaute, "OG", "Onglerie");
        ProductCategory productCategoryCoiffure = productCategory(productCategoryBeaute, "CF", "Coiffure");
        
        products(productCategorySoindDuCorps,new String[]{"Gommage","1000","Masque Vert","1000","Masque Argileux","1500"}
        	,new String[]{"Savon noir","1000","Miel","1000","Lait","1000"});
        
        products(productCategoryEpilation,new String[]{"Epilation Permanente","1000","Epilation a la cire","1000","Epilation electrique","1000"}
    	,new String[]{"Rasoir electrique","1000","Cire","1000","Crie chauffante","1000"});
        
        products(productCategoryMassage,new String[]{"Massage relaxant","1000","Massage amincicant","1000","Massage erotique","1000"}
    	,new String[]{"Huile d'arguant","1000","Pierre chaude","1000","Huile d'olive","1000"});
        
        products(productCategoryOnglerie,new String[]{"Manicure","1000","Pedicure","1000","Pose de faux ongle","1000"}
    	,new String[]{"Verni","1000","Coupe ongle","1000","Lime","1000"});
        
        products(productCategoryCoiffure,new String[]{"Tissage","1000","Tresse","1000","Champoing","1000"}
    	,new String[]{"Meche naturelle","1000","Meche synthetique","1000","RemiAir","1000"});
        
	}
	
	
	private TangibleProduct fakeTangibleProduct(String code,String name, ProductCategory productCategory, String cost){
		TangibleProduct t = new TangibleProduct(code,name,null,productCategory,cost==null?null:new BigDecimal(cost));
		t.setUseQuantity(new BigDecimal("100000"));
		t.setStockQuantity(new BigDecimal("100000"));
		tangibleProductBusiness.create(t);
		return t;
	}
	
	
	private IntangibleProduct fakeIntangibleProduct(String code,String name, ProductCategory productCategory, String cost){
		IntangibleProduct t = new IntangibleProduct(code,name,null,productCategory,StringUtils.isBlank(cost)?null:new BigDecimal(cost));
		productBusiness.create(t);
		return t;
	}
	
	
	private ProductCategory productCategory(ProductCategory parent,String code,String name){
		return productCategoryBusiness.create(new ProductCategory(parent, code, name));
	}
	
	
	private static int I = 0,J=0;
	
	private void products(ProductCategory productCategory,String[] prestations,String[] articles){
		if(prestations!=null)
			for(int i=0;i<prestations.length;i+=2)
				fakeIntangibleProduct("iprod"+(++I),prestations[i],productCategory,prestations[i+1]);
		
		if(articles!=null)
			for(int i=0;i<articles.length;i+=2)
				fakeTangibleProduct("tprod"+(++J),articles[i],productCategory,articles[i+1]);
	}
	
    
	public static Sale sell(SaleBusiness saleBusiness,ProductDao productDao,AccountingPeriod accountingPeriod,Cashier cashier,Integer day,Integer month,Integer hour,Integer minute,String[] products,String amountIn,String amountOut){
    	Sale sale = new Sale();
    	sale.setCashier(cashier);
    	sale.setAccountingPeriod(accountingPeriod);
    	if(day!=null)
    		sale.setDate(new DateTime(new DateTime(accountingPeriod.getPeriod().getFromDate()).getYear(), month, day, hour, minute).toDate());
    	saleProducts(saleBusiness,productDao,sale, products);
    	saleBusiness.create(sale, new SaleCashRegisterMovement(sale, new CashRegisterMovement(cashier.getCashRegister()),
    			amountIn==null?sale.getCost():new BigDecimal(amountIn), amountOut==null?BigDecimal.ZERO:new BigDecimal(amountOut)));
    	return sale;
    }
    
    
    public static Sale sell(SaleBusiness saleBusiness,ProductDao productDao,AccountingPeriod accountingPeriod,Cashier cashier,Integer day,Integer month,Integer hour,Integer minute,String[] products){
    	return sell(saleBusiness, productDao, accountingPeriod, cashier, day, month, hour, minute, products, null, null);
    }
    
    
    public static void saleProducts(SaleBusiness saleBusiness,ProductDao productDao,Sale sale,String[] products){
    	if(products==null)
    		return;
    	for(int i=0;i<products.length;i+=2){
    		SaleProduct saleProduct = saleBusiness.selectProduct(sale, productDao.read(products[i]));
    		saleProduct.setQuantity(new BigDecimal(products[i+1]));
    		saleBusiness.applyChange(sale, saleProduct);
    	}
    		
    }
	*/
}
