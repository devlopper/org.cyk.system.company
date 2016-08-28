package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.accounting.AccountingPeriodBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.business.api.sale.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.stock.StockTangibleProductMovementBusiness;
import org.cyk.system.company.business.api.structure.OwnedCompanyBusiness;
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
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.sale.SaleSearchCriteria;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.payment.CashierDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineAlphabet;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineAlphabetDao;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
import org.cyk.utility.common.ObjectFieldValues;
import org.cyk.utility.common.test.TestEnvironmentListener.Try;

import lombok.Getter;
import lombok.Setter;

@Singleton
public class CompanyBusinessTestHelper extends AbstractBusinessTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
	@Inject private RootDataProducerHelper rootDataProducerHelper;
    
    @Inject private ProductDao productDao;
    @Inject private SalableProductDao salableProductDao;
    @Inject private CustomerDao customerDao;
    @Inject private CashierDao cashierDao;
    @Inject private AccountingPeriodProductDao accountingPeriodProductDao;
    @Inject private FiniteStateMachineStateDao finiteStateMachineStateDao;
    @Inject private FiniteStateMachineAlphabetDao finiteStateMachineAlphabetDao;
    @Inject private SaleDao saleDao;
    @Inject private SaleCashRegisterMovementDao saleCashRegisterMovementDao;
    @Inject private StockableTangibleProductDao stockableTangibleProductDao;
    
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
		cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaultOwnedCompany());
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
		/*if(sale.getComputedIdentifier()==null)
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
	    		SaleProduct saleProduct = inject(SaleBusiness.class).selectProduct(sale, salableProduct,commonUtils.getBigDecimal(infos[1]));
	    		if(salableProduct.getPrice()==null){
	    			saleProduct.getCost().setValue(commonUtils.getBigDecimal(infos[2]));
	    			 inject(SaleBusiness.class).applyChange(sale, saleProduct);
	    		}else{
	    				
	    		}
	    	}
    	if(sale.getComments()==null)
    		sale.setComments(RandomStringUtils.randomAlphabetic(10));
    	if(customerCode!=null)
    		sale.setCustomer(customerDao.read(customerCode));
    	*/
    }
	
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,String amountOut,Date date){
		saleCashRegisterMovement.setAmountIn(new BigDecimal(amountIn));
		saleCashRegisterMovement.setAmountOut(new BigDecimal(amountOut));
		//saleCashRegisterMovement.getCashRegisterMovement().setDate(date);
		 inject(SaleCashRegisterMovementBusiness.class).in(saleCashRegisterMovement);
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
    	inject(CashRegisterMovementBusiness.class).create(cashRegisterMovement);
    }
	public void createCashRegisterMovement(String cashRegisterCode,String amount,String expectedBalance){
		createCashRegisterMovement(cashRegisterCode, amount, expectedBalance,null);
	}
	
    public void createStockTangibleProductMovement(String tangibleProductCode,String quantity){
    	StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement();
    	set(stockTangibleProductMovement, tangibleProductCode, quantity);
    	inject(StockTangibleProductMovementBusiness.class).create(stockTangibleProductMovement);
    }
    
	/* Sale */
	
    public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable,String finalState,String expectedThrowableMessage){
    	final Sale sale =  inject(SaleBusiness.class).instanciateOne(cashierDao.select().one().getPerson());
    	set(sale,identifier,date, cashierCode, customerCode, products, taxable);
    	if(paid==null || !Boolean.TRUE.equals(Boolean.parseBoolean(StringUtils.defaultString(finalState,"true")))){
    		inject(SaleBusiness.class).create(sale);
    	}else{
    		final SaleCashRegisterMovement saleCashRegisterMovement =  inject(SaleCashRegisterMovementBusiness.class).instanciateOne(sale, sale.getCashier().getPerson(),Boolean.TRUE);
        	set(saleCashRegisterMovement, paid);
        	sale.getSaleCashRegisterMovements().add(saleCashRegisterMovement);
        	if(expectedThrowableMessage!=null){
        		new Try(expectedThrowableMessage){ 
        			private static final long serialVersionUID = -8176804174113453706L;
        			@Override protected void code() {inject(SaleBusiness.class).create(sale);}
        		}.execute();
        	}else{
        		inject(SaleBusiness.class).create(sale);
        	}	
    	}
    	return sale;
    }
    /*public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable,String expectedThrowableMessage){
    	return createSale(identifier, date, cashierCode, customerCode, products, paid, taxable,
    			finiteStateMachineFinalStateDao.readByState(inject(AccountingPeriodBusiness.class).findCurrent().getSaleConfiguration()
    					.getFiniteStateMachine().getInitialState())!=null,expectedThrowableMessage);
    }*/
    
    public void writeSaleReport(String identifier){
    	writeReport(inject(SaleBusiness.class).findReport(saleDao.readByComputedIdentifier(identifier)));
    }
    
    public void updateSale(String identifier,String finiteStateMachineAlphabetCode,String taxable){
    	Sale sale = saleDao.readByComputedIdentifier(identifier);
    	FiniteStateMachineAlphabet finiteStateMachineAlphabet = finiteStateMachineAlphabetDao.read(finiteStateMachineAlphabetCode);
    	sale.getSalableProductCollection().setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
    	inject(SaleBusiness.class).update(sale, finiteStateMachineAlphabet);
    }
    
    public void deleteSale(String identifier){
    	Sale sale = saleDao.readByComputedIdentifier(identifier);
    	inject(SaleBusiness.class).delete(sale);
    }
    
    public void createSaleCashRegisterMovement(String saleComputedIdentifier,String computedIdentifier,String cashierPersonCode,String amount,String expectedThrowableMessage){
    	final SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class)
    			.instanciateOne(saleComputedIdentifier,computedIdentifier, cashierPersonCode==null?cashierDao.readOneRandomly().getPerson().getCode():cashierPersonCode, amount);
    	if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {inject(SaleCashRegisterMovementBusiness.class).create(saleCashRegisterMovement);}
    		}.execute();
    	}else{
    		inject(SaleCashRegisterMovementBusiness.class).create(saleCashRegisterMovement);
    	}	
    }
    
    public void writeSaleCashRegisterMovementReport(String identifier){
    	writeReport(inject(SaleCashRegisterMovementBusiness.class).findReport(saleCashRegisterMovementDao.readByCashRegisterMovementComputedIdentifier(identifier)));
    }
    				    
    /*Assertions*/
       
    public void assertSale(String computedIdentifier,ObjectFieldValues expectedValues){
    	Sale sale = saleDao.readByComputedIdentifier(computedIdentifier);
    	doAssertions(sale, expectedValues);
    }
    public void assertSale(String computedIdentifier,String finiteStateMachineStateCode,String numberOfProceedElements,String cost,String tax,String turnover,String balance){
    	assertSale(computedIdentifier, new ObjectFieldValues(Sale.class)
		//.setBaseName(Sale.FIELD_FINITE_STATE_MACHINE_STATE).set(FiniteStateMachineState.FIELD_CODE,finiteStateMachineStateCode)
		.setBaseName(Sale.FIELD_COST).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover)
		.setBaseName(Sale.FIELD_BALANCE).set(Balance.FIELD_VALUE,balance)
		);
    }
    
    public void assertCustomer(String registrationCode,ObjectFieldValues expectedValues){
    	Customer customer = customerDao.read(registrationCode);
    	doAssertions(customer, expectedValues);
    }
    public void assertCustomer(String registrationCode,String saleCount,String turnover,String paymentCount,String paid,String balance){
    	assertCustomer(registrationCode, new ObjectFieldValues(Customer.class)
		.set(Customer.FIELD_SALE_COUNT,saleCount,Customer.FIELD_BALANCE,balance,Customer.FIELD_TURNOVER,turnover
				,Customer.FIELD_PAYMENT_COUNT,paymentCount,Customer.FIELD_PAID,paid));
    }
    
    public void assertCurrentAccountingPeriod(ObjectFieldValues expectedValues){
    	AccountingPeriod accountingPeriod = inject(AccountingPeriodBusiness.class).findCurrent();
    	doAssertions(accountingPeriod.getSaleResults().getCost(), expectedValues);
    }
    public void assertCurrentAccountingPeriod(String numberOfProceedElements,String cost,String tax,String turnover){
    	assertCurrentAccountingPeriod(new ObjectFieldValues(Cost.class)
			.set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover));
    }
    
    public void assertCurrentAccountingPeriodProduct(String productCode,ObjectFieldValues expectedValues){
    	AccountingPeriodProduct accountingPeriodProduct = accountingPeriodProductDao.readByAccountingPeriodByEntity(inject(AccountingPeriodBusiness.class).findCurrent(), productDao.read(productCode));
    	doAssertions(accountingPeriodProduct.getSaleResults().getCost(), expectedValues);
    }
    public void assertCurrentAccountingPeriodProduct(String productCode,String numberOfProceedElements,String cost,String tax,String turnover){
    	assertCurrentAccountingPeriodProduct(productCode,new ObjectFieldValues(Cost.class)
    		.set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover));
    }
    
    public void assertCostComputation(AccountingPeriod accountingPeriod,String[][] values){
    	for(String[] infos : values){
    		BigDecimal vat = inject(AccountingPeriodBusiness.class).computeValueAddedTax(accountingPeriod, commonUtils.getBigDecimal(infos[0]));
    		assertBigDecimalEquals("Value Added Tax of "+infos[0], infos[1], vat);
    		assertBigDecimalEquals("Turnover of "+infos[0]+" with VAT = "+vat, infos[2]
    				, inject(AccountingPeriodBusiness.class).computeTurnover(accountingPeriod, commonUtils.getBigDecimal(infos[0]), vat));
    	}
    }
    
    public void assertCost(Cost cost,ObjectFieldValues expectedValues){
    	doAssertions(cost, expectedValues);
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
    			, inject(SaleBusiness.class).countByCriteria(saleSearchCriteria).toString());
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
    	Collection<Sale> sales = inject(SaleBusiness.class).findByCriteria(criteria);
    	assertEquals("Find sale by criteria count using collection", expectedComputedIdentifiers.length, sales.size());
    	assertEquals("Find sale by criteria count using fonction", expectedComputedIdentifiers.length, inject(SaleBusiness.class).countByCriteria(criteria).intValue());
    	int i = 0;
    	for(Sale sale : sales)
    		assertEquals("Find sale by criteria computed identifier",expectedComputedIdentifiers[i++],sale.getCode());
    	SaleResults saleResults = inject(SaleBusiness.class).computeByCriteria(criteria);
    	doAssertions(saleResults, new ObjectFieldValues(SaleResults.class).set(SaleResults.FIELD_BALANCE,expectedBalance,SaleResults.FIELD_PAID,expectedPaid)
    		.setBaseName(SaleResults.FIELD_COST).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,expectedComputedIdentifiers.length+""
    			,Cost.FIELD_VALUE,expectedCost,Cost.FIELD_TAX,expectedTax,Cost.FIELD_TURNOVER,expectedTurnover));
    }
    
    public void assertAccountingPeriodProduct(String productCode,ObjectFieldValues expectedValues){
    	AccountingPeriodProduct accountingPeriodProduct = accountingPeriodProductDao.readByAccountingPeriodByEntity(inject(AccountingPeriodBusiness.class).findCurrent(), productDao.read(productCode));
    	doAssertions(accountingPeriodProduct.getSaleResults().getCost(), expectedValues);
    	doAssertions(accountingPeriodProduct.getProductResults().getNumberOfSalesSort(), expectedValues);
    }
    
    public void assertAccountingPeriod(AccountingPeriod accountingPeriod,ObjectFieldValues expectedValues){
    	doAssertions(accountingPeriod.getSaleResults().getCost(), expectedValues);
    }
    public void assertAccountingPeriod(ObjectFieldValues expectedValues){
    	assertAccountingPeriod(inject(AccountingPeriodBusiness.class).findCurrent(), expectedValues);
    }
    
    public void assertStockableTangibleProduct(String tangibleProductCode,ObjectFieldValues expectedValues){
    	StockableTangibleProduct stockableTangibleProduct = stockableTangibleProductDao.readByTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode));
    	doAssertions(stockableTangibleProduct.getMovementCollection(), expectedValues);
    }
    public void assertStockableTangibleProduct(String tangibleProductCode,String value){
    	assertStockableTangibleProduct(tangibleProductCode, new ObjectFieldValues(StockableTangibleProduct.class)
    			.setBaseName(StockableTangibleProduct.FIELD_MOVEMENT_COLLECTION).set(MovementCollection.FIELD_VALUE,value));
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

	public void balanceMustBeGreaterThanZero() {
		SalableProduct salableProduct = null;
		do{
		 salableProduct = salableProductDao.readOneRandomly();
		}while(salableProduct.getPrice()==null);
		createSale("sale999", "1/1/2000", null, null, new String[][]{ new String[]{salableProduct.getProduct().getCode(),"1"} }
			, salableProduct.getPrice().multiply(new BigDecimal("2")).toString(), "false", null, "Balance doit être supérieur ou égal à 0");
	}
	
	public void balanceCannotBeIncrementedBeforeSoldOut() {
		SalableProduct salableProduct = null;
		do{
		 salableProduct = salableProductDao.readOneRandomly();
		}while(salableProduct.getPrice()==null);
		createSale("sale999_0", "1/1/2000", null, null, new String[][]{ new String[]{salableProduct.getProduct().getCode(),"1"} }, null, "false", null, null);
		
		createSaleCashRegisterMovement("sale999_0", "pay999", null, salableProduct.getPrice().multiply(new BigDecimal("2")).negate().toString(),
				"La vente n'est pas encore soldée");
	}
	
	public void balanceMustBeLowerThanCost() {
		SalableProduct salableProduct = null;
		do{
		 salableProduct = salableProductDao.readOneRandomly();
		}while(salableProduct.getPrice()==null);
		createSale("sale999_1", "1/1/2000", null, null, new String[][]{ new String[]{salableProduct.getProduct().getCode(),"1"} }, null, "false", null, null);
		
		createSaleCashRegisterMovement("sale999_1", "pay999_1_0", null, salableProduct.getPrice().toString(),null);
		
		createSaleCashRegisterMovement("sale999_1", "pay999_1_1", null, "-100","Balance doit être inférieur ou égal à");
	}
}
