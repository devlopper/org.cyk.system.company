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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductInventoryBusiness;
import org.cyk.system.company.business.api.product.TangibleProductStockMovementBusiness;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.payment.Cashier;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.company.model.product.TangibleProductStockMovement;
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
	
	@Inject private TangibleProductStockMovementBusiness tangibleProductStockMovementBusiness;
	@Inject private TangibleProductBusiness tangibleProductBusiness;
	@Inject private TangibleProductDao tangibleProductDao;
	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleStockInputBusiness saleStockInputBusiness;
	@Inject private AccountingPeriodDao accountingPeriodDao;
	@Inject private CashierDao cashierDao;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	@Inject private ProductBusiness productBusiness;
	@Inject private TangibleProductInventoryBusiness tangibleProductInventoryBusiness;
	@Inject private SaleStockOutputBusiness saleStockOutputBusiness;

	public void createTangibleProductStockMovement(List<TangibleProduct> tangibleProducts,Date date){
		TangibleProduct tangibleProduct = (TangibleProduct)randomDataProvider.randomFromList(tangibleProducts);
		if(tangibleProduct.getCode().equals(TangibleProduct.SALE_STOCK))
			return;
		Integer quantity;
		do{
			quantity = randomDataProvider.randomInt(-10, 10);
		}while(quantity==0);
		TangibleProductStockMovement tangibleProductStockMovement = new TangibleProductStockMovement(tangibleProduct, date, new BigDecimal(quantity), null);
		tangibleProductStockMovementBusiness.create(tangibleProductStockMovement);
		if(randomDataProvider.randomInt(1, 10)!=5){
			TangibleProduct lTangibleProduct = tangibleProductDao.read(tangibleProduct.getIdentifier());
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
			Sale sale = saleBusiness.newInstance(cashier.getEmployee().getPerson());
			//sale.setAccountingPeriod(accountingPeriod);
			sale.setCustomer(rootRandomDataProvider.oneFromDatabase(Customer.class));
			//sale.setCashier(cashier);
			sale.setDate(date(accountingPeriod));
			lastSaleDate = sale.getDate();
			for(int pc=0;pc<RandomDataProvider.getInstance().randomInt(1, 10);pc++){
				Product product = (Product)randomDataProvider.randomFromList(products);
				if(product.getCode().equals(TangibleProduct.SALE_STOCK))
					continue;
				if(product.getCode().equals(IntangibleProduct.SALE_STOCK))
					;//product.setPrice(new BigDecimal(randomDataProvider.randomInt(100000, 500000)));
				saleBusiness.selectProduct(sale, product, new BigDecimal(randomDataProvider.randomInt(1, 10)));
			}
				
			SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,new CashRegisterMovement(sale.getCashier().getCashRegister()));
			saleCashRegisterMovement.setAmountIn(new BigDecimal(randomDataProvider.randomPositiveInt(sale.getCost().intValue())*1.3));
			saleCashRegisterMovement.setAmountIn(saleCashRegisterMovement.getAmountIn().round(new MathContext(0, RoundingMode.DOWN)));
			saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
			saleBusiness.create(sale, saleCashRegisterMovement);
			
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
			SaleStockInput saleStockInput = saleStockInputBusiness.newInstance(cashier.getEmployee().getPerson());
			saleStockInput.setExternalIdentifier(RandomStringUtils.randomNumeric(3)+RandomStringUtils.randomAlphabetic(1).toUpperCase());
			Sale sale = saleStockInput.getSale();
			sale.setCustomer(rootRandomDataProvider.oneFromDatabase(Customer.class));
			sale.setDate(date(accountingPeriod));
			
			//sale.setExternalCustomerIdentifier(randomDataProvider.randomWord(5, 5));
			sale.setAutoComputeValueAddedTax(Boolean.TRUE);
			sale.setComments(randomDataProvider.randomText(5, 5, 5, 5));

			saleStockInput.getTangibleProductStockMovement().setQuantity(new BigDecimal(randomDataProvider.randomInt(1, 10)));
			SaleProduct saleProduct = sale.getSaleProducts().iterator().next();
			saleProduct.setPrice(new BigDecimal(randomDataProvider.randomInt(10000, 1000000)));
			saleProduct.setCommission(new BigDecimal(randomDataProvider.randomInt(0, saleProduct.getPrice().intValue())));
			saleBusiness.applyChange(sale, saleProduct);
			
			SaleCashRegisterMovement saleCashRegisterMovement = new SaleCashRegisterMovement(sale,new CashRegisterMovement(sale.getCashier().getCashRegister()));
			saleCashRegisterMovement(saleCashRegisterMovement,sale.getCost(),1.3f);
		
			saleStockInputBusiness.create(saleStockInput, saleCashRegisterMovement);
			
			for(int j=0;j<randomDataProvider.randomInt(1,2);j++){
				SaleStockOutput saleStockOutput = saleStockOutputBusiness.newInstance(cashier.getEmployee().getPerson(), saleStockInput);
				saleCashRegisterMovement(saleStockOutput.getSaleCashRegisterMovement(),sale.getBalance().getValue(),1.3f);
				saleStockOutput.getTangibleProductStockMovement().setQuantity(new BigDecimal(randomDataProvider.randomInt(1, saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods().intValue())));
				saleStockOutputBusiness.create(saleStockOutput);
				if(sale.getBalance().getValue().signum()<=0)
					break;
			}
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
