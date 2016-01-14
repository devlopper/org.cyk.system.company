package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness.SaleBusinessAdapter;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleProductBusiness;
import org.cyk.system.company.business.api.sale.SaleStockBusiness;
import org.cyk.system.company.business.api.sale.SaleStockInputBusiness;
import org.cyk.system.company.business.api.sale.SaleStockOutputBusiness;
import org.cyk.system.company.model.Balance;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.accounting.AccountingPeriodProduct;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleReport;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.sale.SalesDetails;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.test.ExpectedValues;
import org.cyk.utility.common.test.TestEnvironmentListener.Try;

@Singleton
public class CompanyBusinessTestHelper extends AbstractBusinessTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
	@Inject private SaleBusiness saleBusiness;
	@Inject private RootDataProducerHelper rootDataProducerHelper;
	
	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
    @Inject protected SaleStockOutputBusiness saleStockOutputBusiness;
    @Inject protected SaleStockBusiness saleStockBusiness;
    @Inject protected SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject protected SaleProductBusiness saleProductBusiness;
    
    @Inject private ProductDao productDao;
    @Inject private SalableProductDao salableProductDao;
    @Inject private CustomerDao customerDao;
    @Inject private CashierDao cashierDao;
    @Inject private PersonDao personDao;
    @Inject private AccountingPeriodDao accountingPeriodDao;
    @Inject private AccountingPeriodProductDao accountingPeriodProductDao;
    
    @Getter @Setter private Boolean saleAutoCompleted = Boolean.TRUE;
	
    static{
    	SaleBusiness.LISTENERS.add(new SaleBusinessAdapter(){
			private static final long serialVersionUID = -6866817416681636762L;
    		@Override
    		public void reportCreated(SaleBusiness saleBusiness, SaleReport saleReport, Boolean invoice) {
    			//assertSaleReport(saleReport, invoice);
    		}
    	});
    }
    
	@Override
	protected void initialisation() {
		INSTANCE = this; 
		super.initialisation();
	}
	
	public void set(Product product,String code){
		product.setCode(code);
		product.setName(code);
	}
	
	public void set(SalableProduct salableProduct,String code,String price){
		salableProduct.setProduct(productDao.read(code));
		salableProduct.setPrice(commonUtils.getBigDecimal(price));
	}
	
	public void set(CashRegister cashRegister,String code){
		cashRegister.setCode(code);
		cashRegister.setName(code);
		cashRegister.setOwnedCompany(CompanyBusinessLayer.getInstance().getOwnedCompanyBusiness().findDefaultOwnedCompany());
		cashRegister.setMovementCollection(new MovementCollection());
	}
	
	public void set(CashRegisterMovement cashRegisterMovement,String cashRegisterCode,String amount){
		cashRegisterMovement.setCashRegister(rootDataProducerHelper.getEnumeration(CashRegister.class, cashRegisterCode));
		cashRegisterMovement.setMovement(new Movement());
		set(cashRegisterMovement.getMovement(), cashRegisterMovement.getCashRegister().getMovementCollection().getCode(), amount);
		cashRegisterMovement.getMovement().setValue(new BigDecimal(amount));
	}
	
	public void set(Sale sale,String identifier,String date,String cashierCode,String customerCode,String[][] products,String taxable){
		sale.setComputedIdentifier(identifier);
		sale.setAccountingPeriod(accountingPeriodDao.select().one());
		if(cashierCode==null)
			sale.setCashier(cashierDao.select().one());
		else
			sale.setCashier(cashierDao.readByPerson(personDao.readByCode(cashierCode)));
    	sale.setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
    	sale.setDate(getDate(date));
    	if(products!=null)
	    	for(String[] infos : products){
	    		SalableProduct salableProduct = salableProductDao.readByProduct(productDao.read(infos[0]));
	    		saleBusiness.selectProduct(sale, salableProduct,commonUtils.getBigDecimal(infos[1]));
	    	}
    	sale.setComments(RandomStringUtils.randomAlphabetic(10));
    	if(customerCode!=null)
    		sale.setCustomer(customerDao.readByRegistrationCode(customerCode));
    }
	
	public void set(SaleStockInput saleStockInput,Customer customer,String externalIdentifier,String cost,String commission,String quantity,Date date){
    	saleStockInput.getSale().setDate(date);
    	saleStockInput.setExternalIdentifier(externalIdentifier);
    	saleStockInput.getTangibleProductStockMovement().setQuantity(new BigDecimal(quantity));
    	
    	SaleProduct saleProduct = saleStockInput.getSale().getSaleProducts().iterator().next();
    	
    	saleProduct.setQuantity(new BigDecimal("1"));
    	//saleProduct.setPrice(new BigDecimal(cost));
    	saleProduct.setCommission(new BigDecimal(commission));
    	
    	saleBusiness.applyChange(saleStockInput.getSale(), saleProduct);
    	saleStockInput.getSale().setCustomer(customer);
    }
	public void set(SaleStockInput saleStockInput,Customer customer,String externalIdentifier,String cost,String commission,String quantity){
		set(saleStockInput, customer, externalIdentifier,cost, commission, quantity, null);
	}
	
	public void set(SaleStockOutput saleStockOutput,String quantity,Date date){
		saleStockOutput.getTangibleProductStockMovement().setQuantity(new BigDecimal(quantity).negate());
	}
	
	public void set(SaleStockOutput saleStockOutput,String quantity){
		set(saleStockOutput, quantity, null);
	}
	
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,String amountOut,Date date){
		saleCashRegisterMovement.setAmountIn(new BigDecimal(amountIn));
		saleCashRegisterMovement.setAmountOut(new BigDecimal(amountOut));
		//saleCashRegisterMovement.getCashRegisterMovement().setDate(date);
		saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
	}
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,Date date){
		set(saleCashRegisterMovement,amountIn,"0",date);
	}
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn){
		set(saleCashRegisterMovement,amountIn,"0",null);
	}
	
	/* Payment */
	
	public void createCashRegisterMovement(String cashRegisterCode,String amount,String expectedBalance,String expectedThrowableMessage){
    	final CashRegisterMovement cashRegisterMovement = new CashRegisterMovement();
    	set(cashRegisterMovement, cashRegisterCode, amount);
    	
    	if(StringUtils.isBlank(expectedThrowableMessage)){
    		CompanyBusinessLayer.getInstance().getCashRegisterMovementBusiness().create(cashRegisterMovement);
    		assertCashRegister(cashRegisterMovement.getCashRegister(), expectedBalance);
    	}else{
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {CompanyBusinessLayer.getInstance().getCashRegisterMovementBusiness().create(cashRegisterMovement);}
    		}.execute();
    	}
    }
	public void createCashRegisterMovement(String cashRegisterCode,String amount,String expectedBalance){
		createCashRegisterMovement(cashRegisterCode, amount, expectedBalance,null);
	}
	
    
    private void assertCashRegister(CashRegister cashRegister,String expectedBalance){
    	cashRegister = (CashRegister) genericBusiness.use(CashRegister.class).find(cashRegister.getIdentifier());
    	assertEquals("Cash register balance",new BigDecimal(expectedBalance), cashRegister.getMovementCollection().getValue());
    }    
	
	/* Sale */
	
    public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable
    		,String expectedCost,String expectedTax,String expectedTurnover,String expectedBalance,String expectedCumulBalance){
    	Sale sale = saleBusiness.newInstance(cashierDao.select().one().getPerson());
    	set(sale,identifier,date, cashierCode, customerCode, products, taxable);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(sale, sale.getCashier().getPerson(),Boolean.TRUE);
    	set(saleCashRegisterMovement, paid);
    	saleBusiness.create(sale,saleCashRegisterMovement);
    	
    	sale = saleBusiness.load(sale.getIdentifier());
    	
    	assertCost(sale.getCost(), expectedCost, expectedTax, expectedTurnover);
    	assertBalance(sale.getBalance(), expectedBalance, expectedCumulBalance);
    	
    	   	
    	/*if(Boolean.TRUE.equals(printPos)){
    		writeReport(saleBusiness.findReport(sale));
    		pause(1000);
    		if(saleCashRegisterMovement.getIdentifier()!=null)
    			writeReport(saleCashRegisterMovementBusiness.findReport(saleCashRegisterMovement));
    	}*/
    	return sale;
    }
    
	public Sale sell(Date date,Person person,Customer customer,String[] products,String paid,Boolean printPos,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
    	Sale sale = saleBusiness.newInstance(person);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(sale, person,Boolean.TRUE);
    	//set(sale, customerBusiness.load(customer.getIdentifier()), products,date);
    	set(saleCashRegisterMovement, paid);
    	saleBusiness.create(sale,saleCashRegisterMovement);
    	
    	sale = saleBusiness.load(sale.getIdentifier());
    	
    	if(expectedCost!=null)
    		assertBigDecimalEquals("Cost", expectedCost, sale.getCost().getValue());
    	if(expectedVat!=null)
    		assertBigDecimalEquals("Tax", expectedVat, sale.getCost().getTax());
    	if(expectedBalance!=null)
    		assertBigDecimalEquals("Balance", expectedBalance, sale.getBalance().getValue());
    	if(expectedCumulBalance!=null)
    		assertBigDecimalEquals("Cumul Balance", expectedCumulBalance, sale.getBalance().getCumul());
    	   	
    	if(Boolean.TRUE.equals(printPos)){
    		writeReport(saleBusiness.findReport(sale));
    		pause(1000);
    		if(saleCashRegisterMovement.getIdentifier()!=null)
    			writeReport(saleCashRegisterMovementBusiness.findReport(saleCashRegisterMovement));
    	}
    	return sale;
    }
	
	public void saleComputeByCriteria(SaleSearchCriteria criteria,String expectedCost,String expectedVat,String expectedTurnover,String expectedBalance){
		SalesDetails actual = saleBusiness.computeByCriteria(criteria);
		saleComputeByCriteria(actual, expectedCost, expectedVat, expectedTurnover, expectedBalance);
	}
	
	private void saleComputeByCriteria(SalesDetails actual,String expectedCost,String expectedVat,String expectedTurnover,String expectedBalance){
		assertBigDecimalEquals("Cost", expectedCost, actual.getCost());
		assertBigDecimalEquals("VAT", expectedVat, actual.getValueAddedTax());
		assertBigDecimalEquals("Turnover", expectedTurnover, actual.getTurnover());
    	assertBigDecimalEquals("Balance", expectedBalance, actual.getBalance());
	}
	
	/*
	private static void assertSaleReport(SaleReport saleReport,String[] payments,Boolean invoice){
		contains(LabelValue.class, saleReport.getPaymentInfos().getCollection(), new Object[]{LabelValue.FIELD_LABEL,LabelValue.FIELD_VALUE}, new Object[][]{
			{DefaultSaleReportProducer.LABEL_AMOUNT_TO_PAY,payments[0]}
		});
	}
	*/
	
	public SaleCashRegisterMovement pay(Date date,Person person,Sale sale,String paid,Boolean printPos,String expectedBalance,String expectedCumulBalance){
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(sale, person,Boolean.TRUE);
    	set(saleCashRegisterMovement, paid);
    	saleCashRegisterMovementBusiness.create(saleCashRegisterMovement);
    	
    	assertBigDecimalEquals("Balance", expectedBalance, sale.getBalance().getValue());
    	assertBigDecimalEquals("Cumul Balance", expectedCumulBalance, sale.getBalance().getCumul());
    	
    	if(Boolean.TRUE.equals(printPos)){
    		writeReport(saleCashRegisterMovementBusiness.findReport(saleCashRegisterMovement));
    	}
    	
    	return saleCashRegisterMovement;
    }
	
	/* Sale Stock */
	
	public SaleStockInput drop(Date date,Person person,Customer customer,String externalIdentifier,String cost,String commission,String quantity,Boolean printPos,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
    	SaleStockInput saleStockInput = saleStockInputBusiness.newInstance(person);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person,Boolean.TRUE);
    	//set(saleStockInput, customerBusiness.load(customer.getIdentifier()),externalIdentifier, cost, commission, quantity,date);
    	set(saleCashRegisterMovement, "0");
    	saleStockInputBusiness.create(saleStockInput,saleCashRegisterMovement);
    	
    	if(Boolean.TRUE.equals(printPos)){
    		writeReport(saleBusiness.findReport(saleStockInput.getSale()));
    		//writeReport(saleCashRegisterMovementBusiness.findReport(saleCashRegisterMovement));
    	}
    	
    	assertSaleStockInput(saleStockInput, expectedCost, expectedVat, expectedBalance, expectedCumulBalance);
    	
    	return saleStockInput;
    }
	
	private void assertSaleStockInput(SaleStockInput saleStockInput,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
		saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
		if(expectedCost!=null)
    		assertBigDecimalEquals("Cost", expectedCost, saleStockInput.getSale().getCost().getValue());
    	if(expectedVat!=null)
    		assertBigDecimalEquals("VAT", expectedVat, saleStockInput.getSale().getCost().getTax());
    	if(expectedBalance!=null)
    		assertBigDecimalEquals("Balance", expectedBalance, saleStockInput.getSale().getBalance().getValue());
    	if(expectedCumulBalance!=null)
    		assertBigDecimalEquals("Cumul Balance", expectedCumulBalance, saleStockInput.getSale().getBalance().getCumul());
	}
	
	public SaleStockInput drop(Date date,Person person,Customer customer,String externalIdentifier,String cost,String commission,String quantity,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
		return drop(date, person, customer, externalIdentifier, cost, commission, quantity,Boolean.FALSE ,expectedCost, expectedVat, expectedBalance, expectedCumulBalance);
	}
	
	public SaleStockInput complete(Date date,Person person,SaleStockInput saleStockInput,String commission,Boolean printPos,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
		saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
		saleStockInput.getSale().setAutoComputeValueAddedTax(Boolean.TRUE);
		saleStockInput.getSale().getSaleProducts().iterator().next().setCommission(new BigDecimal(commission));
		saleBusiness.applyChange(saleStockInput.getSale(), saleStockInput.getSale().getSaleProducts().iterator().next());
		SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person,Boolean.TRUE);
		set(saleCashRegisterMovement, "0");
		saleStockInputBusiness.complete(saleStockInput, saleCashRegisterMovement);
    	
    	if(Boolean.TRUE.equals(printPos))
    		writeReport(saleBusiness.findReport(saleStockInput.getSale()));
    	
    	assertSaleStockInput(saleStockInput, expectedCost, expectedVat, expectedBalance, expectedCumulBalance);
    	
    	return saleStockInput;
	}
    
    public void taking(Date date,Person person,SaleStockInput saleStockInput,String quantity,String paid,Boolean printPos,String expectedRemainingGoods,String expectedBalance,String expectedCumulBalance){
    	saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
    	SaleStockOutput saleStockOutput = saleStockOutputBusiness.newInstance(person, saleStockInput);
    	set(saleStockOutput, quantity);
    	set(saleStockOutput.getSaleCashRegisterMovement(), paid,date);
    	saleStockOutputBusiness.create(saleStockOutput);
    	
    	if(Boolean.TRUE.equals(printPos))
    		writeReport(saleCashRegisterMovementBusiness.findReport(saleStockOutput.getSaleCashRegisterMovement()));
    	
    	saleStockOutput = saleStockOutputBusiness.load(saleStockOutput.getIdentifier());
    	//Matchers
    	if(expectedRemainingGoods!=null)
    		assertBigDecimalEquals("Remaining number of goods", expectedRemainingGoods, saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods());
    	if(expectedBalance!=null)
    		assertBigDecimalEquals("Balance", expectedBalance, saleStockOutput.getSaleStockInput().getSale().getBalance().getValue());
    	if(expectedCumulBalance!=null)
    		assertBigDecimalEquals("Cumul Balance", expectedCumulBalance, saleStockOutput.getSaleCashRegisterMovement().getBalance().getCumul());
    }
    
    public void taking(Date date,Person person,SaleStockInput saleStockInput,String quantity,String paid,String expectedRemainingGoods,String expectedBalance,String expectedCumulBalance){
    	taking(date, person, saleStockInput, quantity, paid, Boolean.FALSE, expectedRemainingGoods, expectedBalance, expectedCumulBalance);
    }
    
    public void saleStockInputComputeByCriteria(SaleStockInputSearchCriteria criteria,String expectedIn,String expectedRemaining,String expectedCost,String expectedVat,String expectedTurnover,String expectedBalance){
		//System.out.println(saleBusiness.findByCriteria(criteria));
    	SaleStocksDetails actual = saleStockInputBusiness.computeByCriteria(criteria);
    	saleComputeByCriteria(actual.getSalesDetails(), expectedCost, expectedVat, expectedTurnover, expectedBalance);
		assertBigDecimalEquals("In", expectedIn, actual.getIn());
		assertBigDecimalEquals("Remaining", expectedRemaining, actual.getRemaining());
	}
    
    /*Assertions*/
    
    public void assertCost(Cost cost,String expectedValue,String expectedTax,String expectedTurnover){
    	assertBigDecimalEquals("Cost", expectedValue, cost.getValue());
    	assertBigDecimalEquals("Tax", expectedTax, cost.getTax());
    	assertBigDecimalEquals("Turnover", expectedTurnover, cost.getTurnover());
    }
    public void assertBalance(Balance balance,String expectedValue,String expectedCumul){
    	assertBigDecimalEquals("Balance", expectedValue, balance.getValue());
    	assertBigDecimalEquals("Balance Cumul", expectedCumul, balance.getCumul());
    }
    
    public void assertCostComputation(AccountingPeriod accountingPeriod,String[][] values){
    	for(String[] infos : values){
    		BigDecimal vat = CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeValueAddedTax(accountingPeriod, commonUtils.getBigDecimal(infos[0]));
    		assertBigDecimalEquals("Value Added Tax of "+infos[0], infos[1], vat);
    		assertBigDecimalEquals("Turnover of "+infos[0]+" with VAT = "+vat, infos[2]
    				, CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeTurnover(accountingPeriod, commonUtils.getBigDecimal(infos[0]), vat));
    	}
    }
    
    public void assertSaleResults(SaleResults saleResults,String expectedNumberOfProceedElements,String expectedCost,String expectedTax,String expectedTurnover){
    	assertBigDecimalEquals("Number of proceed elements", expectedNumberOfProceedElements, saleResults.getCost().getNumberOfProceedElements());
    	assertCost(saleResults.getCost(), expectedCost, expectedTax, expectedTurnover);
    }
    public void assertCurrentAccountingPeriodSaleResults(String expectedNumberOfProceedElements,String expectedCost,String expectedTax,String expectedTurnover){
    	assertSaleResults(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleResults(), expectedNumberOfProceedElements, expectedCost, expectedTax, expectedTurnover);
    }
    public void assertCurrentAccountingPeriodProductSaleResults(String productCode,String expectedNumberOfProceedElements,String expectedCost,String expectedTax,String expectedTurnover){
    	AccountingPeriodProduct accountingPeriodProduct = accountingPeriodProductDao.readByAccountingPeriodByProduct(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent(), productDao.read(productCode));
    	SaleResults saleResults = accountingPeriodProduct.getSaleResults();
    	assertSaleResults(saleResults, expectedNumberOfProceedElements, expectedCost, expectedTax, expectedTurnover);
    }
    
    public void assertCustomer(String registrationCode,ExpectedValues expectedValues){
    	Customer customer = customerDao.readByRegistrationCode(registrationCode);
    	//assertBigDecimalEquals("Sale count", expectedValues.get(Customer.FIELD_SALE_COUNT), customer.getSaleCount());
    	doAssertions(customer, expectedValues);
    }
	
	/**/
	
	public static CompanyBusinessTestHelper getInstance() {
		return INSTANCE;
	}
	
}
