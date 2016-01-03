package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness.SaleBusinessAdapter;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleProductBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.model.payment.CashRegister;
import org.cyk.system.company.model.payment.CashRegisterMovement;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleReport;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.company.model.product.SaleStocksDetails;
import org.cyk.system.company.model.product.SalesDetails;
import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.root.business.impl.AbstractBusinessTestHelper;
import org.cyk.system.root.business.impl.RootDataProducerHelper;
import org.cyk.system.root.model.mathematics.Movement;
import org.cyk.system.root.model.mathematics.MovementCollection;
import org.cyk.system.root.model.party.person.Person;
import org.cyk.utility.common.test.TestEnvironmentListener.Try;

import lombok.Getter;
import lombok.Setter;

@Singleton
public class CompanyBusinessTestHelper extends AbstractBusinessTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
	@Inject private CashierBusiness cashierBusiness;
	@Inject private SaleBusiness saleBusiness;
	@Inject private RootDataProducerHelper rootDataProducerHelper;
	
	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
    @Inject protected SaleStockOutputBusiness saleStockOutputBusiness;
    @Inject protected SaleStockBusiness saleStockBusiness;
    @Inject protected SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject protected CustomerBusiness customerBusiness;
    @Inject protected ProductBusiness productBusiness;
    @Inject protected SaleProductBusiness saleProductBusiness;
    
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
	
	public Person cashierPerson(){
		return cashierBusiness.findOneRandomly().getEmployee().getPerson();
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
	
	public void set(Sale sale,Customer customer,String[] products,Date date){
    	sale.setCompleted(Boolean.TRUE);
    	sale.setDate(date);
    	for(int i = 0;i<products.length;i=i+2){
    		Product product = productBusiness.find(products[i]);
    		SaleProduct saleProduct = saleBusiness.selectProduct(sale, product);
    		saleProduct.setQuantity(new BigDecimal(products[i+1]));
    		saleBusiness.applyChange(sale, saleProduct);
    	}
    	sale.setComments(RandomStringUtils.randomAlphabetic(10));
    	sale.setCustomer(customer);
    }
	
	public void set(SaleStockInput saleStockInput,Customer customer,String externalIdentifier,String cost,String commission,String quantity,Date date){
    	saleStockInput.getSale().setCompleted(saleAutoCompleted);
    	saleStockInput.getSale().setDate(date);
    	saleStockInput.setExternalIdentifier(externalIdentifier);
    	saleStockInput.getTangibleProductStockMovement().setQuantity(new BigDecimal(quantity));
    	
    	SaleProduct saleProduct = saleStockInput.getSale().getSaleProducts().iterator().next();
    	
    	saleProduct.setQuantity(new BigDecimal("1"));
    	saleProduct.setPrice(new BigDecimal(cost));
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
	
	public Sale sell(Date date,Person person,Customer customer,String[] products,String paid,Boolean printPos,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
    	Sale sale = saleBusiness.newInstance(person);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(sale, person);
    	set(sale, customerBusiness.load(customer.getIdentifier()), products,date);
    	set(saleCashRegisterMovement, paid);
    	saleBusiness.create(sale,saleCashRegisterMovement);
    	
    	sale = saleBusiness.load(sale.getIdentifier());
    	
    	if(expectedCost!=null)
    		assertBigDecimalEquals("Cost", expectedCost, sale.getCost());
    	if(expectedVat!=null)
    		assertBigDecimalEquals("VAT", expectedVat, sale.getValueAddedTax());
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
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(sale, person);
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
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person);
    	set(saleStockInput, customerBusiness.load(customer.getIdentifier()),externalIdentifier, cost, commission, quantity,date);
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
    		assertBigDecimalEquals("Cost", expectedCost, saleStockInput.getSale().getCost());
    	if(expectedVat!=null)
    		assertBigDecimalEquals("VAT", expectedVat, saleStockInput.getSale().getValueAddedTax());
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
		SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person);
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
	
	/**/
	
	public static CompanyBusinessTestHelper getInstance() {
		return INSTANCE;
	}
	
}
