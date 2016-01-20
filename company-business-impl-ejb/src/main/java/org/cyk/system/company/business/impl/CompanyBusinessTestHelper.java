package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.sale.SaleBusiness;
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
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleProduct;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockInput;
import org.cyk.system.company.model.sale.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.sale.SaleStockOutput;
import org.cyk.system.company.model.sale.SaleStocksDetails;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineAlphabetDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineFinalStateDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
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
    @Inject private FiniteStateMachineStateDao finiteStateMachineStateDao;
    @Inject private FiniteStateMachineAlphabetDao finiteStateMachineAlphabetDao;
    @Inject private FiniteStateMachineFinalStateDao finiteStateMachineFinalStateDao;
    @Inject private SaleDao saleDao;
    @Inject private StockableTangibleProductDao stockableTangibleProductDao;
    
    @Getter @Setter private Boolean saleAutoCompleted = Boolean.TRUE;
	
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
	
	public void set(StockableTangibleProduct stockableTangibleProduct,String tangibleProductCode,String minimum,String maximum,String value){
		stockableTangibleProduct.setTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode));
		stockableTangibleProduct.setMovementCollection(new MovementCollection()); 
		set(stockableTangibleProduct.getMovementCollection(), tangibleProductCode+"_movcol","Le stock",value==null?"0":value, minimum, maximum,"Input","Output");
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
	
	public void set(StockTangibleProductMovement stockTangibleProductMovement,String tangibleProductCode,String amount){
		stockTangibleProductMovement.setStockableTangibleProduct(stockableTangibleProductDao.readByTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode)));
		stockTangibleProductMovement.setMovement(new Movement());
		set(stockTangibleProductMovement.getMovement(), stockTangibleProductMovement.getStockableTangibleProduct().getMovementCollection().getCode(), amount);
	}
	
	public void set(Sale sale,String identifier,String date,String cashierCode,String customerCode,String[][] products,String taxable){
		sale.setComputedIdentifier(identifier);
		sale.setAccountingPeriod(accountingPeriodDao.select().one());
		if(cashierCode==null)
			sale.setCashier(cashierDao.select().one());
		else
			sale.setCashier(cashierDao.readByPerson(personDao.readByCode(cashierCode)));
    	sale.setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
    	sale.setDate(getDate(date,Boolean.FALSE));
    	if(products!=null)
	    	for(String[] infos : products){
	    		SalableProduct salableProduct = salableProductDao.readByProduct(productDao.read(infos[0]));
	    		SaleProduct saleProduct = saleBusiness.selectProduct(sale, salableProduct,commonUtils.getBigDecimal(infos[1]));
	    		if(salableProduct.getPrice()==null){
	    			saleProduct.getCost().setValue(commonUtils.getBigDecimal(infos[2]));
	    			saleBusiness.applyChange(sale, saleProduct);
	    		}else{
	    				
	    		}
	    	}
    	sale.setComments(RandomStringUtils.randomAlphabetic(10));
    	if(customerCode!=null)
    		sale.setCustomer(customerDao.readByRegistrationCode(customerCode));
    }
	
	public void set(SaleStockInput saleStockInput,Customer customer,String externalIdentifier,String cost,String commission,String quantity,Date date){
    	saleStockInput.getSale().setDate(date);
    	saleStockInput.setExternalIdentifier(externalIdentifier);
    	//saleStockInput.getTangibleProductStockMovement().setQuantity(new BigDecimal(quantity));
    	
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
		//saleStockOutput.getTangibleProductStockMovement().setQuantity(new BigDecimal(quantity).negate());
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
	
    public void createStockTangibleProductMovement(String tangibleProductCode,String quantity){
    	StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement();
    	set(stockTangibleProductMovement, tangibleProductCode, quantity);
    	CompanyBusinessLayer.getInstance().getStockTangibleProductMovementBusiness().create(stockTangibleProductMovement);
    }
    
	/* Sale */
	
    public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable,Boolean finalState){
    	Sale sale = saleBusiness.newInstance(cashierDao.select().one().getPerson());
    	set(sale,identifier,date, cashierCode, customerCode, products, taxable);
    	if(paid==null || !Boolean.TRUE.equals(finalState)){
    		saleBusiness.create(sale);
    	}else{
    		SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(sale, sale.getCashier().getPerson(),Boolean.TRUE);
        	set(saleCashRegisterMovement, paid);
        	saleBusiness.create(sale,saleCashRegisterMovement);
    	}
    	
    	   	
    	/*if(Boolean.TRUE.equals(printPos)){
    		writeReport(saleBusiness.findReport(sale));
    		pause(1000);
    		if(saleCashRegisterMovement.getIdentifier()!=null)
    			writeReport(saleCashRegisterMovementBusiness.findReport(saleCashRegisterMovement));
    	}*/
    	return sale;
    }
    public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable){
    	return createSale(identifier, date, cashierCode, customerCode, products, paid, taxable,
    			finiteStateMachineFinalStateDao.readByState(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent().getSaleConfiguration().getFiniteStateMachine().getInitialState())!=null);
    }
    
    public void updateSale(String identifier,String finiteStateMachineAlphabetCode,String taxable){
    	Sale sale = saleDao.readByComputedIdentifier(identifier);
    	FiniteStateMachineAlphabet finiteStateMachineAlphabet = finiteStateMachineAlphabetDao.read(finiteStateMachineAlphabetCode);
    	sale.setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
    	CompanyBusinessLayer.getInstance().getSaleBusiness().update(sale, finiteStateMachineAlphabet);
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
    	//saleComputeByCriteria(actual.getSalesDetails(), expectedCost, expectedVat, expectedTurnover, expectedBalance);
		assertBigDecimalEquals("In", expectedIn, actual.getIn());
		assertBigDecimalEquals("Remaining", expectedRemaining, actual.getRemaining());
	}
    
    /*Assertions*/
    
    /*public void assertCost(Cost cost,String expectedValue,String expectedTax,String expectedTurnover){
    	assertBigDecimalEquals("Cost", expectedValue, cost.getValue());
    	assertBigDecimalEquals("Tax", expectedTax, cost.getTax());
    	assertBigDecimalEquals("Turnover", expectedTurnover, cost.getTurnover());
    }
    public void assertBalance(Balance balance,String expectedValue,String expectedCumul){
    	assertBigDecimalEquals("Balance", expectedValue, balance.getValue());
    	assertBigDecimalEquals("Balance Cumul", expectedCumul, balance.getCumul());
    }*/
    
    public void assertSale(String computedIdentifier,ExpectedValues expectedValues){
    	Sale sale = saleDao.readByComputedIdentifier(computedIdentifier);
    	doAssertions(sale, expectedValues);
    	doAssertions(sale.getCost(), expectedValues);
    	doAssertions(sale.getBalance(), expectedValues);
    }
    public void assertSale(String computedIdentifier,String finiteStateMachineStateCode,String numberOfProceedElements,String cost,String tax,String turnover,String balance){
    	assertSale(computedIdentifier, new ExpectedValues()
    		.setClass(FiniteStateMachineState.class).setValues(FiniteStateMachineState.FIELD_CODE,finiteStateMachineStateCode)
    		.setClass(Cost.class).setValues(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover)
    		.setClass(Balance.class).setValues(Balance.FIELD_VALUE,balance)
    		);
    }
    
    public void assertCustomer(String registrationCode,ExpectedValues expectedValues){
    	Customer customer = customerDao.readByRegistrationCode(registrationCode);
    	doAssertions(customer, expectedValues);
    }
    public void assertCustomer(String registrationCode,String saleCount,String turnover,String paymentCount,String paid,String balance){
    	assertCustomer(registrationCode, new ExpectedValues()
    		.setClass(Customer.class).setValues(Customer.FIELD_SALE_COUNT,saleCount,Customer.FIELD_BALANCE,balance,Customer.FIELD_TURNOVER,turnover
    				,Customer.FIELD_PAYMENT_COUNT,paymentCount,Customer.FIELD_PAID,paid));
    }
    
    public void assertCurrentAccountingPeriod(ExpectedValues expectedValues){
    	AccountingPeriod accountingPeriod = CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent();
    	doAssertions(accountingPeriod.getSaleResults().getCost(), expectedValues);
    }
    public void assertCurrentAccountingPeriod(String numberOfProceedElements,String cost,String tax,String turnover){
    	assertCurrentAccountingPeriod(new ExpectedValues()
			.setClass(Cost.class).setValues(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover));
    }
    
    public void assertCurrentAccountingPeriodProduct(String productCode,ExpectedValues expectedValues){
    	AccountingPeriodProduct accountingPeriodProduct = accountingPeriodProductDao.readByAccountingPeriodByProduct(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent(), productDao.read(productCode));
    	doAssertions(accountingPeriodProduct.getSaleResults().getCost(), expectedValues);
    }
    public void assertCurrentAccountingPeriodProduct(String productCode,String numberOfProceedElements,String cost,String tax,String turnover){
    	assertCurrentAccountingPeriodProduct(productCode,new ExpectedValues()
    			.setClass(Cost.class).setValues(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover));
    }
    
    public void assertCostComputation(AccountingPeriod accountingPeriod,String[][] values){
    	for(String[] infos : values){
    		BigDecimal vat = CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeValueAddedTax(accountingPeriod, commonUtils.getBigDecimal(infos[0]));
    		assertBigDecimalEquals("Value Added Tax of "+infos[0], infos[1], vat);
    		assertBigDecimalEquals("Turnover of "+infos[0]+" with VAT = "+vat, infos[2]
    				, CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().computeTurnover(accountingPeriod, commonUtils.getBigDecimal(infos[0]), vat));
    	}
    }
    
    public void assertSaleFiniteStateMachineStateCount(Object[][] datas){
    	for(Object[] data : datas){
    		assertSaleFiniteStateMachineStateCount((String[])data[0],(String)data[1]);
    	}
    }
    public void assertSaleFiniteStateMachineStateCount(String[] finiteStateMachineStateCodes,String expectedCount){
    	SaleSearchCriteria saleSearchCriteria = new SaleSearchCriteria();
    	for(String finiteStateMachineStateCode : finiteStateMachineStateCodes){
    		saleSearchCriteria.getFiniteStateMachineStates().add(finiteStateMachineStateDao.read(finiteStateMachineStateCode));
    	}
    	assertEquals("Sale search criteria "+StringUtils.join(finiteStateMachineStateCodes)+" count", expectedCount
    			, CompanyBusinessLayer.getInstance().getSaleBusiness().countByCriteria(saleSearchCriteria).toString());
    }
    public void assertSaleFiniteStateMachineStateCount(String finiteStateMachineStateCode,String expectedCount){
    	assertSaleFiniteStateMachineStateCount(new String[]{finiteStateMachineStateCode},expectedCount);
    }
    public void assertSaleFiniteStateMachineStateCount(String expectedCount){
    	Set<String> codes = new HashSet<>();
    	for(FiniteStateMachineState finiteStateMachineState : finiteStateMachineStateDao.readAll())
    		codes.add(finiteStateMachineState.getCode());
    	assertSaleFiniteStateMachineStateCount(codes.toArray(new String[]{}),expectedCount);
    }
    
    public void assertSaleByCriteria(String fromDate,String toDate,String[] saleFiniteStateMachineStateCodes,String[] expectedComputedIdentifiers,String expectedCost,String expectedTax,String expectedTurnover
    		,String expectedBalance,String expectedPaid){
    	SaleSearchCriteria criteria = new SaleSearchCriteria(getDate(fromDate, Boolean.FALSE), getDate(toDate, Boolean.FALSE));
    	for(String saleFiniteStateMachineStateCode : saleFiniteStateMachineStateCodes)
    		criteria.getFiniteStateMachineStates().add(finiteStateMachineStateDao.read(saleFiniteStateMachineStateCode));
    	Collection<Sale> sales = CompanyBusinessLayer.getInstance().getSaleBusiness().findByCriteria(criteria);
    	assertEquals("Find sale by criteria count using collection", expectedComputedIdentifiers.length, sales.size());
    	assertEquals("Find sale by criteria count using fonction", expectedComputedIdentifiers.length, CompanyBusinessLayer.getInstance().getSaleBusiness().countByCriteria(criteria).intValue());
    	int i = 0;
    	for(Sale sale : sales)
    		assertEquals("Find sale by criteria computed identifier",expectedComputedIdentifiers[i++],sale.getComputedIdentifier());
    	SaleResults saleResults = CompanyBusinessLayer.getInstance().getSaleBusiness().computeByCriteria(criteria);
    	doAssertions(saleResults, new ExpectedValues().setClass(Cost.class).setValues(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,expectedComputedIdentifiers.length+""
    			,Cost.FIELD_VALUE,expectedCost,Cost.FIELD_TAX,expectedTax,Cost.FIELD_TURNOVER,expectedTurnover)
    			.setClass(SaleResults.class).setValues(SaleResults.FIELD_BALANCE,expectedBalance,SaleResults.FIELD_PAID,expectedPaid));
    }
    
    public void assertAccountingPeriodProduct(String productCode,ExpectedValues expectedValues){
    	AccountingPeriodProduct accountingPeriodProduct = accountingPeriodProductDao.readByAccountingPeriodByProduct(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent(), productDao.read(productCode));
    	doAssertions(accountingPeriodProduct.getSaleResults().getCost(), expectedValues);
    	doAssertions(accountingPeriodProduct.getProductResults().getNumberOfSalesSort(), expectedValues);
    }
    
    public void assertAccountingPeriod(AccountingPeriod accountingPeriod,ExpectedValues expectedValues){
    	doAssertions(accountingPeriod.getSaleResults().getCost(), expectedValues);
    }
    public void assertAccountingPeriod(ExpectedValues expectedValues){
    	assertAccountingPeriod(CompanyBusinessLayer.getInstance().getAccountingPeriodBusiness().findCurrent(), expectedValues);
    }
    
    public void assertStockableTangibleProduct(String tangibleProductCode,ExpectedValues expectedValues){
    	StockableTangibleProduct stockableTangibleProduct = stockableTangibleProductDao.readByTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode));
    	doAssertions(stockableTangibleProduct.getMovementCollection(), expectedValues);
    }
    public void assertStockableTangibleProduct(String tangibleProductCode,String value){
    	assertStockableTangibleProduct(tangibleProductCode, new ExpectedValues()
    			.setClass(MovementCollection.class).setValues(MovementCollection.FIELD_VALUE,value));
    }
    
    /**/
    
    public SaleSearchCriteria getSaleSearchCriteria(String fromDate,String toDate){
    	SaleSearchCriteria saleSearchCriteria = new SaleSearchCriteria(getDate(fromDate,Boolean.FALSE),getDate(toDate,Boolean.FALSE));
    	
    	return saleSearchCriteria;
    }
	
	/**/
	
	public static CompanyBusinessTestHelper getInstance() {
		return INSTANCE;
	}
	
}
