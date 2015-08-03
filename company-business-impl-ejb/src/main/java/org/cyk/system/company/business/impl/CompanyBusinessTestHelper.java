package org.cyk.system.company.business.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cyk.system.company.business.api.payment.CashierBusiness;
import org.cyk.system.company.business.api.product.CustomerBusiness;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.business.api.product.SaleStockBusiness;
import org.cyk.system.company.business.api.product.SaleStockInputBusiness;
import org.cyk.system.company.business.api.product.SaleStockOutputBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.system.root.business.impl.AbstractTestHelper;
import org.cyk.system.root.model.party.person.Person;

@Singleton
public class CompanyBusinessTestHelper extends AbstractTestHelper implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
	@Inject private CashierBusiness cashierBusiness;
	@Inject private SaleBusiness saleBusiness;
	
	@Inject protected SaleStockInputBusiness saleStockInputBusiness;
    @Inject protected SaleStockOutputBusiness saleStockOutputBusiness;
    @Inject protected SaleStockBusiness saleStockBusiness;
    @Inject protected SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
    @Inject protected CustomerBusiness customerBusiness;
	
	@Override
	protected void initialisation() {
		INSTANCE = this; 
		super.initialisation();
	}
	
	public Person cashierPerson(){
		return cashierBusiness.findOneRandomly().getEmployee().getPerson();
    }
	
	public void set(SaleStockInput saleStockInput,Customer customer,String externalIdentifier,String cost,String commission,String quantity,Date date){
    	saleStockInput.getSale().setCompleted(Boolean.TRUE);
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
		saleCashRegisterMovement.getCashRegisterMovement().setDate(date);
		saleCashRegisterMovementBusiness.in(saleCashRegisterMovement);
	}
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn,Date date){
		set(saleCashRegisterMovement,amountIn,"0",date);
	}
	public void set(SaleCashRegisterMovement saleCashRegisterMovement,String amountIn){
		set(saleCashRegisterMovement,amountIn,"0",null);
	}
	
	/* Sale Stock */
	
	public SaleStockInput drop(Date date,Person person,Customer customer,String externalIdentifier,String cost,String commission,String quantity,String expectedCost,String expectedVat,String expectedBalance,String expectedCumulBalance){
    	SaleStockInput saleStockInput = saleStockInputBusiness.newInstance(person);
    	SaleCashRegisterMovement saleCashRegisterMovement = saleCashRegisterMovementBusiness.newInstance(saleStockInput.getSale(), person);
    	set(saleStockInput, customerBusiness.load(customer.getIdentifier()),externalIdentifier, cost, commission, quantity,date);
    	set(saleCashRegisterMovement, "0");
    	saleStockInputBusiness.create(saleStockInput,saleCashRegisterMovement);
    	
    	saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
    	assertBigDecimalEquals("Cost", expectedCost, saleStockInput.getSale().getCost());
    	assertBigDecimalEquals("VAT", expectedVat, saleStockInput.getSale().getValueAddedTax());
    	assertBigDecimalEquals("Balance", expectedBalance, saleStockInput.getSale().getBalance().getValue());
    	assertBigDecimalEquals("Cumul Balance", expectedCumulBalance, saleStockInput.getSale().getBalance().getCumul());
    	return saleStockInput;
    }
    
    public void taking(Date date,Person person,SaleStockInput saleStockInput,String quantity,String paid,String expectedRemainingGoods,String expectedBalance,String expectedCumulBalance){
    	saleStockInput = saleStockInputBusiness.load(saleStockInput.getIdentifier());
    	SaleStockOutput saleStockOutput = saleStockOutputBusiness.newInstance(person, saleStockInput);
    	set(saleStockOutput, quantity);
    	set(saleStockOutput.getSaleCashRegisterMovement(), paid,date);
    	saleStockOutputBusiness.create(saleStockOutput);
    	
    	saleStockOutput = saleStockOutputBusiness.load(saleStockOutput.getIdentifier());
    	//Matchers
    	assertBigDecimalEquals("Remaining number of goods", expectedRemainingGoods, saleStockOutput.getSaleStockInput().getRemainingNumberOfGoods());
    	assertBigDecimalEquals("Balance", expectedBalance, saleStockOutput.getSaleStockInput().getSale().getBalance().getValue());
    	assertBigDecimalEquals("Cumul Balance", expectedCumulBalance, saleStockOutput.getSaleCashRegisterMovement().getBalance().getCumul());
    }
	
	/**/
	
	public static CompanyBusinessTestHelper getInstance() {
		return INSTANCE;
	}
	
}
