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
import org.cyk.system.company.business.api.payment.CashRegisterBusiness;
import org.cyk.system.company.business.api.payment.CashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
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
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleCashRegisterMovement;
import org.cyk.system.company.model.sale.SaleCashRegisterMovementCollection;
import org.cyk.system.company.model.sale.SaleResults;
import org.cyk.system.company.model.stock.StockTangibleProductMovement;
import org.cyk.system.company.model.stock.StockableTangibleProduct;
import org.cyk.system.company.persistence.api.accounting.AccountingPeriodProductDao;
import org.cyk.system.company.persistence.api.payment.CashRegisterMovementDao;
import org.cyk.system.company.persistence.api.product.ProductDao;
import org.cyk.system.company.persistence.api.sale.CustomerDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.company.persistence.api.sale.SalableProductDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementCollectionDao;
import org.cyk.system.company.persistence.api.sale.SaleCashRegisterMovementDao;
import org.cyk.system.company.persistence.api.sale.SaleDao;
import org.cyk.system.company.persistence.api.stock.StockableTangibleProductDao;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.mathematics.machine.FiniteStateMachineState;
import org.cyk.system.root.persistence.api.mathematics.machine.FiniteStateMachineStateDao;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.ObjectFieldValues;
import org.cyk.utility.common.test.TestEnvironmentListener.Try;

import lombok.Getter;
import lombok.Setter;

@Singleton
public class CompanyBusinessTestHelper extends AbstractBusinessTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
    @Inject private ProductDao productDao;
    @Inject private SalableProductDao salableProductDao;
    @Inject private CustomerDao customerDao;
    @Inject private AccountingPeriodProductDao accountingPeriodProductDao;
    @Inject private FiniteStateMachineStateDao finiteStateMachineStateDao;
    @Deprecated @Inject private StockableTangibleProductDao stockableTangibleProductDao;
    
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
	
	@Deprecated
	public void set(StockableTangibleProduct stockableTangibleProduct,String tangibleProductCode,String minimum,String maximum,String value){
		stockableTangibleProduct.setTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode));
		stockableTangibleProduct.setMovementCollection(new MovementCollection()); 
		set(stockableTangibleProduct.getMovementCollection(), tangibleProductCode,"Le stock",value==null?"0":value, minimum, maximum,"Input","Output");
	}
	
	@Deprecated
	public void set(CashRegister cashRegister,String code){
		setEnumeration(cashRegister, code);
		cashRegister.setOwnedCompany(inject(OwnedCompanyBusiness.class).findDefaulted());
		cashRegister.setMovementCollection(new MovementCollection());
	}
		
	@Deprecated
	public CashRegisterMovement createCashRegisterMovement(String cashRegisterCode,String value,String expectedValue,String expectedThrowableMessage){
		CashRegister cashRegister = inject(CashRegisterBusiness.class).find(cashRegisterCode);
    	final CashRegisterMovement cashRegisterMovement = inject(CashRegisterMovementBusiness.class).instanciateOne(null,cashRegister);
    	cashRegisterMovement.getMovement().setValue(commonUtils.getBigDecimal(value));
    	cashRegisterMovement.getMovement().setAction(StringUtils.startsWith(value, Constant.CHARACTER_MINUS.toString()) ? cashRegister.getMovementCollection().getType().getDecrementAction()
    			:cashRegister.getMovementCollection().getType().getIncrementAction());
    	if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {create(cashRegisterMovement);}
    		}.execute();
    	}else{
    		create(cashRegisterMovement);
    		assertMovementCollection(cashRegisterMovement.getCashRegister().getMovementCollection(), expectedValue);
    	}
    	return cashRegisterMovement;
    }
	@Deprecated
	public CashRegisterMovement createCashRegisterMovement(String cashRegisterCode,String value,String expectedValue){
		return createCashRegisterMovement(cashRegisterCode,value, expectedValue,null);
	}
	@Deprecated
	public CashRegisterMovement updateCashRegisterMovement(final CashRegisterMovement cashRegisterMovement,String value,String expectedValue,String expectedThrowableMessage){
		cashRegisterMovement.getMovement().setValue(commonUtils.getBigDecimal(value));
    	if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {update(cashRegisterMovement);}
    		}.execute();
    	}else{
    		update(cashRegisterMovement);
    		assertMovementCollection(cashRegisterMovement.getCashRegister().getMovementCollection(), expectedValue);
    	}
    	return cashRegisterMovement;
    }
	@Deprecated
	public CashRegisterMovement updateCashRegisterMovement(CashRegisterMovement cashRegisterMovement,String value,String expectedValue){
		return updateCashRegisterMovement(cashRegisterMovement,value, expectedValue,null);
	}
	@Deprecated
	public CashRegisterMovement deleteCashRegisterMovement(final CashRegisterMovement cashRegisterMovement,String expectedValue,String expectedThrowableMessage){
    	if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {delete(cashRegisterMovement);}
    		}.execute();
    	}else{
    		delete(cashRegisterMovement);
    		assertMovementCollection(cashRegisterMovement.getCashRegister().getMovementCollection(), expectedValue);
    	}
    	return cashRegisterMovement;
    }
	@Deprecated
	public CashRegisterMovement deleteCashRegisterMovement(final CashRegisterMovement cashRegisterMovement,String expectedValue){
		return deleteCashRegisterMovement(cashRegisterMovement, expectedValue,null);
	}
	@Deprecated
	public SalableProductCollectionItem updateSalableProductCollectionItem(SalableProductCollectionItem pSalableProductCollectionItem,String quantity,String expectedSalableProductCollectionCost,String expectedThrowableMessage){
		final SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).find(pSalableProductCollectionItem.getIdentifier());
		salableProductCollectionItem.setCascadeOperationToMaster(Boolean.TRUE);
		salableProductCollectionItem.setQuantity(commonUtils.getBigDecimal(quantity));
    	if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {update(salableProductCollectionItem);}
    		}.execute();
    	}else{
    		update(salableProductCollectionItem);
    		assertSalableProductCollectionItem(salableProductCollectionItem,expectedSalableProductCollectionCost);
    	}
    	return salableProductCollectionItem;
    }
	@Deprecated
	public SalableProductCollectionItem updateSalableProductCollectionItem(SalableProductCollectionItem salableProductCollectionItem,String quantity,String expectedSalableProductCollectionCost){
		return updateSalableProductCollectionItem(salableProductCollectionItem,quantity,expectedSalableProductCollectionCost,null);
	}
	@Deprecated
	public SalableProductCollectionItem deleteSalableProductCollectionItem(SalableProductCollectionItem pSalableProductCollectionItem,String expectedThrowableMessage){
		final SalableProductCollectionItem salableProductCollectionItem = inject(SalableProductCollectionItemBusiness.class).find(pSalableProductCollectionItem.getIdentifier());
		salableProductCollectionItem.setCascadeOperationToMaster(Boolean.TRUE);
		if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {delete(salableProductCollectionItem);}
    		}.execute();
    	}else{
    		delete(salableProductCollectionItem);
    	}
    	return salableProductCollectionItem;
    }
	@Deprecated
	public SalableProductCollectionItem deleteSalableProductCollectionItem(final SalableProductCollectionItem salableProductCollectionItem){
		return deleteSalableProductCollectionItem(salableProductCollectionItem,null);
	}
	@Deprecated
	public SalableProductCollectionItem deleteSalableProductCollectionItem(String salableProductCollectionItemCode){
		return deleteSalableProductCollectionItem(inject(SalableProductCollectionItemDao.class).read(salableProductCollectionItemCode),null);
	}
	@Deprecated
	public Sale updateSale(Sale pSale,String expectedCostValue,String expectedBalanceValue,String expectedThrowableMessage){
		final Sale sale = inject(SaleBusiness.class).find(pSale.getIdentifier());
		if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {update(sale);}
    		}.execute();
    	}else{
    		update(sale);
    		assertSale(sale,expectedCostValue,expectedBalanceValue);
    	}
    	return sale;
    }
	@Deprecated
	public Sale updateSale(Sale sale,String expectedCostValue,String expectedBalanceValue){
		return updateSale(sale,expectedCostValue, expectedBalanceValue,null);
	}
	@Deprecated
	public Sale deleteSale(Sale pSale,String expectedThrowableMessage){
		final Sale sale = inject(SaleBusiness.class).find(pSale.getIdentifier());
		if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {delete(sale);}
    		}.execute();
    	}else{
    		delete(sale);
    		
    	}
    	return sale;
    }
	public Sale deleteSale(final Sale sale){
		return deleteSale(sale,null);
	}
	@Deprecated
	public SaleCashRegisterMovement updateSaleCashRegisterMovement(SaleCashRegisterMovement pSaleCashRegisterMovement,String value,String expectedSaleBalanceValue,String expectedCashRegisterValue,String expectedThrowableMessage){
		final SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).find(pSaleCashRegisterMovement.getIdentifier());
		saleCashRegisterMovement.getCollection().getCashRegisterMovement().getMovement().setValue(commonUtils.getBigDecimal(value));
    	if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {update(saleCashRegisterMovement);}
    		}.execute();
    	}else{
    		update(saleCashRegisterMovement);
    		assertSaleCashRegisterMovement(saleCashRegisterMovement,null, saleCashRegisterMovement.getSale().getSalableProductCollection().getCost().getValue().toString(), expectedSaleBalanceValue,expectedCashRegisterValue);
    	}
    	return saleCashRegisterMovement;
    }
	@Deprecated
	public SaleCashRegisterMovement updateSaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement,String value,String expectedSaleBalanceValue,String expectedCashRegisterValue){
		return updateSaleCashRegisterMovement(saleCashRegisterMovement,value,expectedSaleBalanceValue, expectedCashRegisterValue,null);
	}
	@Deprecated
	public SaleCashRegisterMovement deleteSaleCashRegisterMovement(SaleCashRegisterMovement pSaleCashRegisterMovement,String expectedSaleBalanceValue,String expectedCashRegisterValue,String expectedThrowableMessage){
		final SaleCashRegisterMovement saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).find(pSaleCashRegisterMovement.getIdentifier());
		if(expectedThrowableMessage!=null){
    		new Try(expectedThrowableMessage){ 
    			private static final long serialVersionUID = -8176804174113453706L;
    			@Override protected void code() {delete(saleCashRegisterMovement);}
    		}.execute();
    	}else{
    		Sale sale = saleCashRegisterMovement.getSale();
    		CashRegister cashRegister = saleCashRegisterMovement.getCollection().getCashRegisterMovement().getCashRegister();
    		delete(saleCashRegisterMovement);
    		assertSale(sale, sale.getSalableProductCollection().getCost().getValue().toString(), expectedSaleBalanceValue);
    		assertCashRegister(cashRegister, expectedCashRegisterValue);
    	}
    	return saleCashRegisterMovement;
    }
	@Deprecated
	public SaleCashRegisterMovement deleteSaleCashRegisterMovement(final SaleCashRegisterMovement saleCashRegisterMovement,String expectedSaleBalanceValue,String expectedCashRegisterValue){
		return deleteSaleCashRegisterMovement(saleCashRegisterMovement, expectedSaleBalanceValue,expectedCashRegisterValue,null);
	}
	
	@Deprecated
	public void set(StockTangibleProductMovement stockTangibleProductMovement,String tangibleProductCode,String quantity){
		if(tangibleProductCode!=null)
			stockTangibleProductMovement.setStockableTangibleProduct(stockableTangibleProductDao.readByTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode)));
		stockTangibleProductMovement.setMovement(new Movement());
		//set(stockTangibleProductMovement.getMovement(), stockTangibleProductMovement.getStockableTangibleProduct().getMovementCollection().getCode(), quantity);
	}
	@Deprecated
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
	@Deprecated
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,String amountOut,Date date){
		saleCashRegisterMovement.getCollection().setAmountIn(new BigDecimal(amountIn));
		saleCashRegisterMovement.getCollection().setAmountOut(new BigDecimal(amountOut));
		//saleCashRegisterMovement.getCashRegisterMovement().setDate(date);
		//inject(SaleCashRegisterMovementBusiness.class).in(saleCashRegisterMovement);
	}
	@Deprecated
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,Date date){
		set(saleCashRegisterMovement,amountIn,"0",date);
	}
	@Deprecated
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn){
		set(saleCashRegisterMovement,amountIn,"0",null);
	}
	
	/* Creators */
	@Deprecated
    public void createStockTangibleProductMovement(String tangibleProductCode,String quantity){
    	StockTangibleProductMovement stockTangibleProductMovement = new StockTangibleProductMovement();
    	set(stockTangibleProductMovement, tangibleProductCode, quantity);
    	inject(StockTangibleProductMovementBusiness.class).create(stockTangibleProductMovement);
    }
    
	/* Sale */
	@Deprecated
    public Sale createSale(String identifier,String date,String cashierCode,String customerCode,String[][] products,String paid,String taxable,String finalState,String expectedThrowableMessage){
    	final Sale sale =  inject(SaleBusiness.class).instanciateOne();
    	set(sale,identifier,date, cashierCode, customerCode, products, taxable);
    	if(paid==null || !Boolean.TRUE.equals(Boolean.parseBoolean(StringUtils.defaultString(finalState,"true")))){
    		inject(SaleBusiness.class).create(sale);
    	}else{
    		final SaleCashRegisterMovement saleCashRegisterMovement =  inject(SaleCashRegisterMovementBusiness.class).instanciateOne(sale, null/*sale.getCashier().getPerson()*/,Boolean.TRUE);
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
    	//writeReport(inject(SaleBusiness.class).findReport(saleDao.readByComputedIdentifier(identifier)));
    }
    @Deprecated
    public void updateSale(String identifier,String finiteStateMachineAlphabetCode,String taxable){
    	/*Sale sale = null;//saleDao.readByComputedIdentifier(identifier);
    	FiniteStateMachineAlphabet finiteStateMachineAlphabet = finiteStateMachineAlphabetDao.read(finiteStateMachineAlphabetCode);
    	sale.getSalableProductCollection().setAutoComputeValueAddedTax(Boolean.parseBoolean(taxable));
    	*/
    }
    @Deprecated
    public void deleteSale(String identifier){
    	Sale sale = null;//saleDao.readByComputedIdentifier(identifier);
    	inject(SaleBusiness.class).delete(sale);
    }
    @Deprecated 
    public void writeSaleCashRegisterMovementReport(String identifier){
    	//writeReport(inject(SaleCashRegisterMovementBusiness.class).findReport(saleCashRegisterMovementDao.readByCashRegisterMovementCode(identifier)));
    }
    				    
    /*Assertions*/
    @Deprecated
    public void assertCashRegister(CashRegister cashRegister,String expectedValue){
    	cashRegister = inject(CashRegisterBusiness.class).find(cashRegister.getIdentifier());
    	assertMovementCollection(cashRegister.getMovementCollection().getCode(), expectedValue);
    }
    @Deprecated
    public void assertCashRegisterMovement(String code,String expectedCashRegisterValue,String expectedMovementValue,Boolean expectedIncrement
    		,String expectedSupportingDocumentProvider,String expectedSupportingDocumentIdentifier,String expectedMovementCollectionValue){
    	CashRegisterMovement cashRegisterMovement = inject(CashRegisterMovementDao.class).read(code);
    	assertCashRegister(cashRegisterMovement.getCashRegister(), expectedCashRegisterValue);
    	assertMovement(cashRegisterMovement.getMovement().getCode(),"MOV_EXPECT_CUMUL", expectedMovementValue,expectedMovementCollectionValue, expectedIncrement, expectedSupportingDocumentProvider, expectedSupportingDocumentIdentifier);
    }
    @Deprecated
    public void assertCashRegisterMovement(CashRegisterMovement cashRegisterMovement,String expectedCashRegisterValue,String expectedValue,Boolean expectedIncrement
    		,String expectedSupportingDocumentProvider,String expectedSupportingDocumentIdentifier){
    	
    }
    
    /**/
    
    public void assertSalableProductCollection(SalableProductCollection salableProductCollection,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertCost(salableProductCollection.getCost(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSalableProductCollection(String code,String expectedCostNumberOfElements,String expectedCostValue
    		,String expectedCostTax,String expectedCostTurnover){
    	assertSalableProductCollection(inject(SalableProductCollectionDao.class).read(code), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    }
    
    public void assertSalableProductCollectionItem(SalableProductCollectionItem salableProductCollectionItem,String salableProductCollectionCost){
    	//assertSalableProductCollection(salableProductCollectionItem.getCollection(), salableProductCollectionCost);
    }
    
    public void assertSale(String saleCode,String costValue,String balanceValue){
    	assertSale(inject(SaleDao.class).read(saleCode), costValue, balanceValue);
    }
    
    public void assertSale(Sale sale,String costValue,String balanceValue){
    	//String name = "sale "+sale.getCode();
    	//assertSalableProductCollection(sale.getSalableProductCollection(), costValue);
    	//assertSalableProductCollection(sale.getSalableProductCollection(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
    	//assertBigDecimalEquals(name+" balance value", balanceValue, sale.getBalance().getValue());
    	assertCost(sale.getSalableProductCollection().getCost(), new ObjectFieldValues(Balance.class).set(Cost.FIELD_VALUE,costValue));
    	//assertBalance(sale.getBalance(), new ObjectFieldValues(Balance.class).set(Balance.FIELD_VALUE,balanceValue));
    }
    
    public void assertSaleCashRegisterMovementCollection(String code,String expectedCashRegisterValue,String expectedMovementValue,Boolean expectedIncrement
    		,String expectedSupportingDocumentProvider,String expectedSupportingDocumentIdentifier,String expectedMovementCollectionValue){
    	SaleCashRegisterMovementCollection saleCashRegisterMovementCollection = inject(SaleCashRegisterMovementCollectionDao.class).read(code);
    	//assertSale(saleCashRegisterMovement.getSale(), expectedSaleCostValue, expectedSaleBalanceValue);
    	assertCashRegisterMovement(saleCashRegisterMovementCollection.getCashRegisterMovement().getCode(), expectedCashRegisterValue, expectedMovementValue
    			, expectedIncrement, expectedSupportingDocumentProvider, expectedSupportingDocumentIdentifier,expectedMovementCollectionValue);
    	//assertCashRegisterMovementCollection(saleCashRegisterMovementCollection.getCollection().getCashRegisterMovement(), expectedCashRegisterValue);
    }
    
    public void assertSaleCashRegisterMovement(SaleCashRegisterMovement saleCashRegisterMovement,String expectedBalance,String expectedSaleCostValue,String expectedSaleBalanceValue,String expectedCashRegisterValue){
    	saleCashRegisterMovement = inject(SaleCashRegisterMovementBusiness.class).find(saleCashRegisterMovement.getIdentifier());
    	assertSale(saleCashRegisterMovement.getSale(), expectedSaleCostValue, expectedSaleBalanceValue);
    	assertBigDecimalEquals("Sale cash register movement balance", expectedBalance, saleCashRegisterMovement.getBalance().getValue());
    	//assertCashRegisterMovement(saleCashRegisterMovement.getCollection().getCashRegisterMovement(), expectedCashRegisterValue);
    }
    
    public void assertSaleCashRegisterMovement(String code,String expectedBalance,String expectedSaleCostValue,String expectedSaleBalanceValue,String expectedCashRegisterValue){
    	assertSaleCashRegisterMovement(inject(SaleCashRegisterMovementDao.class).read(code),expectedBalance, expectedSaleCostValue, expectedSaleBalanceValue, expectedCashRegisterValue);
    }
    
    @Deprecated   
    public void assertSale(String computedIdentifier,ObjectFieldValues expectedValues){
    	Sale sale = null;//saleDao.readByComputedIdentifier(computedIdentifier);
    	doAssertions(sale, expectedValues);
    }
    @Deprecated
    public void assertSale(String computedIdentifier,String finiteStateMachineStateCode,String numberOfProceedElements,String cost,String tax,String turnover,String balance){
    	/*assertSale(computedIdentifier, new ObjectFieldValues(Sale.class)
		//.setBaseName(Sale.FIELD_FINITE_STATE_MACHINE_STATE).set(FiniteStateMachineState.FIELD_CODE,finiteStateMachineStateCode)
		.setBaseName(Sale.FIELD_COST).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS,numberOfProceedElements,Cost.FIELD_VALUE, cost,Cost.FIELD_TAX, tax,Cost.FIELD_TURNOVER, turnover)
		.setBaseName(Sale.FIELD_BALANCE).set(Balance.FIELD_VALUE,balance)
		);*/
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
    
    public void assertCost(Cost cost,String expectedNumberOfElements,String expectedValue,String expectedTax,String expectedTurnover){
    	doAssertions(cost, new ObjectFieldValues(Cost.class).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, expectedNumberOfElements)
    			.set(Cost.FIELD_VALUE, expectedValue).set(Cost.FIELD_TAX, expectedTax).set(Cost.FIELD_TURNOVER, expectedTurnover));
    }
    
    public void assertCost(Cost cost,ObjectFieldValues expectedValues){
    	doAssertions(cost, expectedValues);
    }
    
    public void assertBalance(Balance balance,ObjectFieldValues expectedValues){
    	doAssertions(balance, expectedValues);
    }
    
    public void assertBalance(Balance balance,String expectedValue,String expectedCumul){
    	doAssertions(balance, new ObjectFieldValues(Balance.class).set(Balance.FIELD_VALUE, expectedValue).set(Balance.FIELD_CUMUL, expectedCumul));
    }
    
    public void assertSaleFiniteStateMachineStateCount(Object[][] datas){
    	for(Object[] data : datas){
    		assertSaleFiniteStateMachineStateCount((String[])data[0],(String)data[1]);
    	}
    }
    public void assertSaleFiniteStateMachineStateCount(String[] finiteStateMachineStateCodes,String expectedCount){
    	//Sale.SearchCriteria saleSearchCriteria = new Sale.SearchCriteria();
    	/*for(String finiteStateMachineStateCode : finiteStateMachineStateCodes){
    		saleSearchCriteria.getFiniteStateMachineStates().add(finiteStateMachineStateDao.read(finiteStateMachineStateCode));
    	}*/
    	//assertEquals("Sale search criteria "+StringUtils.join(finiteStateMachineStateCodes)+" count", expectedCount
    	//		, inject(SaleBusiness.class).countByCriteria(saleSearchCriteria).toString());
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
    	Sale.SearchCriteria criteria = new Sale.SearchCriteria(getDate(fromDate, Boolean.FALSE), getDate(toDate, Boolean.FALSE));
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
    @Deprecated
    public void assertStockableTangibleProduct(String tangibleProductCode,ObjectFieldValues expectedValues){
    	StockableTangibleProduct stockableTangibleProduct = stockableTangibleProductDao.readByTangibleProduct((TangibleProduct) productDao.read(tangibleProductCode));
    	doAssertions(stockableTangibleProduct.getMovementCollection(), expectedValues);
    }
    @Deprecated
    public void assertStockableTangibleProduct(String tangibleProductCode,String value){
    	assertStockableTangibleProduct(tangibleProductCode, new ObjectFieldValues(StockableTangibleProduct.class)
    			.setBaseName(StockableTangibleProduct.FIELD_MOVEMENT_COLLECTION).set(MovementCollection.FIELD_VALUE,value));
    }
    
    
    
    /**/
    
    public Sale.SearchCriteria getSaleSearchCriteria(String fromDate,String toDate){
    	Sale.SearchCriteria saleSearchCriteria = new Sale.SearchCriteria(getDate(fromDate,Boolean.FALSE),getDate(toDate,Boolean.FALSE));
    	
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
		
		//createSaleCashRegisterMovement("sale999_0", "pay999", null, salableProduct.getPrice().multiply(new BigDecimal("2")).negate().toString(),
		//		"La vente n'est pas encore soldée");
	}
	
	public void balanceMustBeLowerThanCost() {
		SalableProduct salableProduct = null;
		do{
		 salableProduct = salableProductDao.readOneRandomly();
		}while(salableProduct.getPrice()==null);
		createSale("sale999_1", "1/1/2000", null, null, new String[][]{ new String[]{salableProduct.getProduct().getCode(),"1"} }, null, "false", null, null);
		
		//createSaleCashRegisterMovement("sale999_1", "pay999_1_0", null, salableProduct.getPrice().toString(),null);
		
		//createSaleCashRegisterMovement("sale999_1", "pay999_1_1", null, "-100","Balance doit être inférieur ou égal à");
	}
	
	@Override
	public TestCase instanciateTestCase(AbstractBusinessTestHelper helper) {
		return (TestCase) super.instanciateTestCase(helper);
	}
	
	@Override
	public TestCase instanciateTestCase() {
		return (TestCase) super.instanciateTestCase();
	}
	
	/**/
	
	public static class TestCase extends AbstractBusinessTestHelper.TestCase implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public void assertTangibleProduct(TangibleProduct tangibleProduct,String expectedQuantity){
			assertBigDecimalEquals("tangible product quantity is not equal", expectedQuantity, tangibleProduct.getQuantityMovementCollection().getValue());
	    }
		
		public void assertTangibleProduct(String code,String expectedQuantity){
			TangibleProduct tangibleProduct = read(TangibleProduct.class, code);
			inject(TangibleProductBusiness.class).setQuantityMovementCollection(tangibleProduct);
			assertTangibleProduct(tangibleProduct, expectedQuantity);
		}
		
		public void assertCost(Cost cost,String expectedNumberOfElements,String expectedValue,String expectedTax,String expectedTurnover){
	    	helper.doAssertions(cost, new ObjectFieldValues(Cost.class).set(Cost.FIELD_NUMBER_OF_PROCEED_ELEMENTS, expectedNumberOfElements)
	    			.set(Cost.FIELD_VALUE, expectedValue).set(Cost.FIELD_TAX, expectedTax).set(Cost.FIELD_TURNOVER, expectedTurnover));
	    }
		
		public void assertSalableProductCollectionCost(SalableProductCollection salableProductCollection,String expectedCostNumberOfElements,String expectedCostValue
	    		,String expectedCostTax,String expectedCostTurnover){
	    	assertCost(salableProductCollection.getCost(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
	    }
	    
	    public void assertSalableProductCollectionCost(String code,String expectedCostNumberOfElements,String expectedCostValue
	    		,String expectedCostTax,String expectedCostTurnover){
	    	assertSalableProductCollectionCost(inject(SalableProductCollectionDao.class).read(code), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
	    }
		
		public void assertSalableProductCollection(SalableProductCollection salableProductCollection,String expectedCostNumberOfElements,String expectedCostValue
	    		,String expectedCostTax,String expectedCostTurnover){
	    	assertCost(salableProductCollection.getCost(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
	    }
	    
	    public void assertSalableProductCollection(String code,String expectedCostNumberOfElements,String expectedCostValue
	    		,String expectedCostTax,String expectedCostTurnover){
	    	assertSalableProductCollection(inject(SalableProductCollectionDao.class).read(code), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
	    }
	    
	    public void assertSaleCost(String code,String expectedCostNumberOfElements,String expectedCostValue
	    		,String expectedCostTax,String expectedCostTurnover){
	    	assertSalableProductCollectionCost(inject(SaleDao.class).read(code).getSalableProductCollection(), expectedCostNumberOfElements, expectedCostValue, expectedCostTax, expectedCostTurnover);
	    }
		
	}
}
