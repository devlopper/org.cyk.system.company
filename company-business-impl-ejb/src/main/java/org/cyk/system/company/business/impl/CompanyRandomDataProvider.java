package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.TangibleProductDao;
import org.cyk.system.root.business.impl.AbstractRandomDataProvider;
import org.cyk.system.root.business.impl.RootRandomDataProvider;
import org.cyk.utility.common.generator.RandomDataProvider;

@Singleton
public class CompanyRandomDataProvider extends AbstractRandomDataProvider implements Serializable {

	private static final long serialVersionUID = -4495079111239824662L;

	//private RootBusinessLayer rootBusinessLayer = RootBusinessLayer.getInstance();
	@Inject private RootRandomDataProvider rootRandomDataProvider;
	
	@Inject private StockTangibleProductMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private TangibleProductDao tangibleProductDao;
	@Inject private SaleBusiness saleBusiness;
	@Inject private AccountingPeriodDao accountingPeriodDao;
	@Inject private CashierDao cashierDao;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	
	public void createTangibleProductStockMovement(List<TangibleProduct> tangibleProducts,Date date){
		TangibleProduct tangibleProduct = (TangibleProduct)randomDataProvider.randomFromList(tangibleProducts);
		if(tangibleProduct.getCode().equals(TangibleProduct.STOCKING))
			return;
		Integer quantity;
		do{
			quantity = randomDataProvider.randomInt(-10, 10);
		}while(quantity==0);
		StockTangibleProductMovement tangibleProductStockMovement = null;// new TangibleProductStockMovement(tangibleProduct, date, new BigDecimal(quantity), null);
		tangibleProductStockMovementBusiness.create(tangibleProductStockMovement);
		if(randomDataProvider.randomInt(1, 10)!=5){
			//TangibleProduct lTangibleProduct = tangibleProductDao.read(tangibleProduct.getIdentifier());
			//lTangibleProduct.setUseQuantity(lTangibleProduct.getUseQuantity().add(tangibleProductStockMovement.getQuantity()));
			tangibleProductBusiness.update(tangibleProduct);
			//tangibleProduct.setUseQuantity(lTangibleProduct.getUseQuantity());
		}
	}
	
	public void createTangibleProductStockMovement(Integer count){
		Date fromDate=DateUtils.addYears(new Date(), -1),toDate=DateUtils.addYears(new Date(), 1);
		List<TangibleProduct> tangibleProducts = new ArrayList<>(tangibleProductDao.readAll());
		for(int i=0;i<count;i++){
			createTangibleProductStockMovement(tangibleProducts,randomDataProvider.randomDate(fromDate, toDate));
		} 
	}
	
	public void createSale(Integer count){
		List<TangibleProduct> tangibleProducts = new ArrayList<>(tangibleProductBusiness.findAll());
		List<Product> products = new ArrayList<>(productBusiness.findAll());
		AccountingPeriod accountingPeriod = accountingPeriodDao.select().one();
		Cashier cashier = cashierDao.select().one();
		Date lastSaleDate = null;
		int stock = randomDataProvider.randomInt(0, count*2);
		for(int i=0;i<count;i++){
			Sale sale = saleBusiness.instanciateOne(cashier.getPerson());
			//sale.setAccountingPeriod(accountingPeriod);
			sale.setCustomer(rootRandomDataProvider.oneFromDatabase(Customer.class));
			//sale.setCashier(cashier);
			sale.setDate(date(accountingPeriod));
			lastSaleDate = sale.getDate();
			for(int pc=0;pc<RandomDataProvider.getInstance().randomInt(1, 10);pc++){
				Product product = (Product)randomDataProvider.randomFromList(products);
				if(product.getCode().equals(TangibleProduct.STOCKING))
					continue;
				if(product.getCode().equals(IntangibleProduct.STOCKING))
					;//product.setPrice(new BigDecimal(randomDataProvider.randomInt(100000, 500000)));
				//saleBusiness.selectProduct(sale, product, new BigDecimal(randomDataProvider.randomInt(1, 10)));
			}
				
			SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement();//new SaleCashRegisterMovement(sale,new CashRegisterMovement(sale.getCashier().getCashRegister()));
			saleCashRegisterMovement.setAmountIn(new BigDecimal(randomDataProvider.randomPositiveInt(sale.getCost().getValue().intValue())*1.3));
			saleCashRegisterMovement.setAmountIn(saleCashRegisterMovement.getAmountIn().round(new MathContext(0, RoundingMode.DOWN)));
			saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
			sale.getSaleCashRegisterMovements().add(saleCashRegisterMovement);
			saleBusiness.create(sale);
			
			if(stock>0 && randomDataProvider.randomInt(1, 5)==3){
				createTangibleProductStockMovement(tangibleProducts,sale.getDate());
				stock--;
			}
		}
		
		for(int i=0;i<stock;i++){
			createTangibleProductStockMovement(tangibleProducts,randomDataProvider.randomDate(lastSaleDate, accountingPeriod.getPeriod().getToDate()));
		}
	}
	
	public void createSaleStockInput(Integer count){
		AccountingPeriod accountingPeriod = accountingPeriodDao.select().one();
		Cashier cashier = cashierDao.select().one();
		for(int i=0;i<count;i++){
			//SaleStockTangibleProductMovementInput saleStockInput = new SaleStockTangibleProductMovementInput(); //null;//saleStockInputBusiness.instanciate(cashier.getPerson());
			//saleStockInput.setExternalIdentifier(RandomStringUtils.randomNumeric(3)+RandomStringUtils.randomAlphabetic(1).toUpperCase());
			Sale sale = null;//saleStockInput.getSale();
			sale.setCustomer(rootRandomDataProvider.oneFromDatabase(Customer.class));
			sale.setDate(date(accountingPeriod));
			
			//sale.setExternalCustomerIdentifier(randomDataProvider.randomWord(5, 5));
			sale.setAutoComputeValueAddedTax(Boolean.TRUE);
			sale.setComments(randomDataProvider.randomText(5, 5, 5, 5));

			//saleStockInput.getStockTangibleProductStockMovement().setQuantity(new BigDecimal(randomDataProvider.randomInt(1, 10)));
			SaleProduct saleProduct = sale.getSaleProducts().iterator().next();
			//saleProduct.setPrice(new BigDecimal(randomDataProvider.randomInt(10000, 1000000)));
			//saleProduct.setCommission(new BigDecimal(randomDataProvider.randomInt(0, saleProduct.getPrice().intValue())));
			saleBusiness.applyChange(sale, saleProduct);
			
			SaleCashRegisterMovement saleCashRegisterMovement = null;//new SaleCashRegisterMovement(sale,new CashRegisterMovement(sale.getCashier().getCashRegister()));
			saleCashRegisterMovement(saleCashRegisterMovement,sale.getCost().getValue(),1.3f);
		
			//saleStockInputBusiness.create(saleStockInput, saleCashRegisterMovement);
			
			
		}
	}
	
	private void saleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement,BigDecimal maxValue,Float ratio){
		try {
			saleCashRegisterMovement.setAmountIn(new BigDecimal(randomDataProvider.randomInt(1,maxValue.intValue())*ratio));
		} catch (Exception e) {
			System.out.println("ERROR : "+maxValue.intValue());
			throw new RuntimeException(e);
		}
		saleCashRegisterMovement.setAmountIn(saleCashRegisterMovement.getAmountIn().round(new MathContext(0, RoundingMode.DOWN)));
		saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
	}
	
	public void createTangibleProductInventory(Integer count){
		List<TangibleProduct> tangibleProducts = new ArrayList<>(tangibleProductBusiness.findAll());
		AccountingPeriod accountingPeriod = accountingPeriodDao.select().one();
		for(int i=0;i<count;i++){
			TangibleProductInventory tangibleProductInventory = new TangibleProductInventory();
			tangibleProductInventory.setDate(date(accountingPeriod));
			for(TangibleProduct tangibleProduct : tangibleProducts)
				tangibleProductInventory.getDetails().add(new TangibleProductInventoryDetail(tangibleProduct, new BigDecimal(randomDataProvider.randomInt(0, 1000)), null));
			tangibleProductInventoryBusiness.create(tangibleProductInventory);
		}
	}
	
	private Date date(AccountingPeriod accountingPeriod){
		return randomDataProvider.randomDate(accountingPeriod.getPeriod().getFromDate(), accountingPeriod.getPeriod().getToDate());
	}
	
	
}
