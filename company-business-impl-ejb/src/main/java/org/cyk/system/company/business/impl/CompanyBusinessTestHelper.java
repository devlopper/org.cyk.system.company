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
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementInput;
import org.cyk.system.company.model.sale.SaleStockTangibleProductMovementOutput;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodDao;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.sale.SaleStockTangibleProductMovementInputDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineAlphabetDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineFinalStateDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
import org.cyk.system.root.persistence.api.party.person.PersonDao;
import org.cyk.utility.common.test.ExpectedValues;

@Singleton
public class CompanyBusinessTestHelper extends AbstractBusinessTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
	@Inject private RootDataProducerHelper rootDataProducerHelper;
    
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
    @Inject private SaleStockTangibleProductMovementInputDao saleStockTangibleProductMovementInputDao;
    
    @Getter @Setter private Boolean saleAutoCompleted = Boolean.TRUE;
	
	@Override
	protected void initialisation() {
		INSTANCE = this; 
		super.initialisation();
	}
	
	/* Setters */
	public void set(Product product,String code){
		setEnumeration(product, code);
	}
	public void set(SalableProduct salableProduct,String code,String price){
		salableProduct.setProduct(productDao.read(code));
		salableProduct.setPrice(commonUtils.getBigDecimal(price));
	}
	
	public void set(StockableTangibleProduct stockableTangibleProduct,String tangibleProductCode,String minimum,String maximum,String value){
		stockableTangibleProduct.setTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode));
		stockableTangibleProduct.setMovementCollection(new MovementCollection()); 
		set(stockableTangibleProduct.getMovementCollection(), tangibleProductCode,"Le stock",value==null?"0":value, minimum, maximum,"Input","Output");
	}
	
	public void set(CashRegister cashRegister,String code){
		setEnumeration(cashRegister, code);
		cashRegister.setOwnedCompany(CompanyBusinessLayer.getInstance().getOwnedCompanyBusiness().findDefaultOwnedCompany());
		cashRegister.setMovementCollection(new MovementCollection());
	}
	
	public void set(CashRegisterMovement cashRegisterMovement,String cashRegisterCode,String amount){
		cashRegisterMovement.setCashRegister(rootDataProducerHelper.getEnumeration(CashRegister.class, cashRegisterCode));
		cashRegisterMovement.setMovement(new Movement());
		set(cashRegisterMovement.getMovement(), cashRegisterMovement.getCashRegister().getMovementCollection().getCode(), amount);
		cashRegisterMovement.getMovement().setValue(new BigDecimal(amount));
	}
	
	public void set(StockTangibleProductMovement stockTangibleProductMovement,String tangibleProductCode,String quantity){
		if(tangibleProductCode!=null)
			stockTangibleProductMovement.setStockableTangibleProduct(stockableTangibleProductDao.readByTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode)));
		stockTangibleProductMovement.setMovement(new Movement());
		set(stockTangibleProductMovement.getMovement(), stockTangibleProductMovement.getStockableTangibleProduct().getMovementCollection().getCode(), quantity);
	}
	
	public void set(Sale sale,String identifier,String date,String cashierCode,String customerCode,String[][] products,String taxable){
		if(sale.getComputedIdentifier()==null)
			sale.setComputedIdentifier(identifier);
		if(sale.getAccountingPeriod()==null)
			sale.setAccountingPeriod(accountingPeriodDao.select().one());
		if(sale.getCashier()==null)
			if(cashierCode==null)
				sale.setCashier(cashierDao.select().one());
			else
				sale.setCashier(cashierDao.readByPerson(personDao.readByCode(cashierCode)));
    	sale.setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
    	sale.setDate(getDate(date,Boolean.FALSE));
    	if(products!=null)
	    	for(String[] infos : products){
	    		SalableProduct salableProduct = salableProductDao.readByProduct(productDao.read(infos[0]));
	    		SaleProduct saleProduct = getCompanyBusinessLayer().getSaleBusiness().selectProduct(sale, salableProduct,commonUtils.getBigDecimal(infos[1]));
	    		if(salableProduct.getPrice()==null){
	    			saleProduct.getCost().setValue(commonUtils.getBigDecimal(infos[2]));
	    			 getCompanyBusinessLayer().getSaleBusiness().applyChange(sale, saleProduct);
	    		}else{
	    				
	    		}
	    	}
    	if(sale.getComments()==null)
    		sale.setComments(RandomStringUtils.randomAlphabetic(10));
    	if(customerCode!=null)
    		sale.setCustomer(customerDao.readByRegistrationCode(customerCode));
    }
	
	public void set(SaleStockTangibleProductMovementInput saleStockTangibleProductMovementInput,String quantity){
		//set(saleStockTangibleProductMovementInput.getSale(), identifier, date, cashierCode, customerCode, null, taxable);
		set(saleStockTangibleProductMovementInput.getStockTangibleProductMovement(), null, quantity);
	}
		
	public void set(SaleStockTangibleProductMovementOutput saleStockOutput,String quantity,Date date){
		//saleStockOutput.getStockTangibleProductStockMovement().setQuantity(new BigDecimal(quantity).negate());
	}
	
	public void set(SaleStockTangibleProductMovementOutput saleStockOutput,String quantity){
		set(saleStockOutput, quantity, null);
	}
	
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,String amountOut,Date date){
		saleCashRegisterMovement.setAmountIn(new BigDecimal(amountIn));
		saleCashRegisterMovement.setAmountOut(new BigDecimal(amountOut));
		//saleCashRegisterMovement.getCashRegisterMovement().setDate(date);
		 getCompanyBusinessLayer().getSaleCashRegisterMovementBusiness().in(saleCashRegisterMovement);
	}
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,Date date){
		set(saleCashRegisterMovement,amountIn,"0",date);
	}
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn){
		set(saleCashRegisterMovement,amountIn,"0",null);
	}
	
	/* Creators */
	
	public void createCashRegisterMovement(String cashRegisterCode,String amount,String expectedBalance,String expectedThrowableMessage){
    	final CashRegisterMovement cashRegisterMovement = new CashRegisterMovement();
    	set(cashRegisterMovement, cashRegisterCode, amount);
    	CompanyBusinessLayer.getInstance().getCashRegisterMovementBusiness().create(cashRegisterMovement);
    }
	public void createCashRegisterMovement(String cashRegisterCode,String amount,String expectedBalance){
		createCashRegisterMovement(cashRegisterCode, amount, expectedBalance,null);
	}
	
    public void createStockTangibleProductMovement(String tangibleProductCode,String quantity){
    	StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement();
    	set(stockTangibleProductMovement, tangibleProductCode, quantity);
    	CompanyBusinessLayer.getInstance().getStockTangibleProductMovementBusiness().create(stockTangibleProductMovement);
    }
    
	/* Sale */
	
    public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable,Boolean finalState){
    	Sale sale =  getCompanyBusinessLayer().getSaleBusiness().instanciate(cashierDao.select().one().getPerson());
    	set(sale,identifier,date, cashierCode, customerCode, products, taxable);
    	if(paid==null || !Boolean.TRUE.equals(finalState)){
    		 getCompanyBusinessLayer().getSaleBusiness().create(sale);
    	}else{
    		SaleCashRegisterMovement saleCashRegisterMovement =  getCompanyBusinessLayer().getSaleCashRegisterMovementBusiness().instanciate(sale, sale.getCashier().getPerson(),Boolean.TRUE);
        	set(saleCashRegisterMovement, paid);
        	getCompanyBusinessLayer().getSaleBusiness().create(sale,saleCashRegisterMovement);
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
    
    public SaleStockTangibleProductMovementInput createSaleStockTangibleProductMovementInput(String identifier,String date,String cashierCode,String customerCode,String price,String taxable,String quantity){
    	Sale sale = CompanyBusinessLayer.getInstance().getSaleBusiness().instanciate(cashierDao.readAll().iterator().next().getPerson());
    	set(sale, identifier, date, cashierCode, customerCode, null, taxable);
    	SaleStockTangibleProductMovementInput input = CompanyBusinessLayer.getInstance().getSaleStockInputBusiness().instanciate(sale);
    	set(input, quantity);
    	if(price!=null){
    		SaleProduct saleProduct = input.getSale().getSaleProducts().iterator().next();
    		saleProduct.getCost().setValue(commonUtils.getBigDecimal(price));
    		saleProduct.setQuantity(commonUtils.getBigDecimal(quantity));
    		CompanyBusinessLayer.getInstance().getSaleBusiness().applyChange(input.getSale(), saleProduct);
    	}
    	
    	CompanyBusinessLayer.getInstance().getSaleStockInputBusiness().create(input);
    	return input;
    }
    
    public SaleStockTangibleProductMovementOutput createSaleStockTangibleProductMovementOutput(String identifier,String date,String cashierCode,String paid,String quantity){
    	Sale sale = saleDao.readByComputedIdentifier(identifier);
    	SaleStockTangibleProductMovementOutput output = CompanyBusinessLayer.getInstance().getSaleStockOutputBusiness().instanciate(cashierDao.readAll().iterator().next().getPerson()
    			,saleStockTangibleProductMovementInputDao.readBySale(sale));
    	output.getSaleCashRegisterMovement().getCashRegisterMovement().getMovement().setValue(commonUtils.getBigDecimal(paid));
    	output.getStockTangibleProductMovement().getMovement().setValue(commonUtils.getBigDecimal(quantity));
    	return CompanyBusinessLayer.getInstance().getSaleStockOutputBusiness().create(output);
    }
    				    
    /*Assertions*/
       
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
	
    private CompanyBusinessLayer getCompanyBusinessLayer(){
    	return CompanyBusinessLayer.getInstance();
    }
    
	public static CompanyBusinessTestHelper getInstance() {
		return INSTANCE;
	}
	
}
