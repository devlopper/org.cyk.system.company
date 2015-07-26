package org.cyk.system.company.business.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.business.api.product.SaleCashRegisterMovementBusiness;
import org.cyk.system.company.model.product.Customer;
import org.cyk.system.company.model.product.SaleCashRegisterMovement;
import org.cyk.system.company.model.product.SaleProduct;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockOutput;
import org.cyk.utility.common.cdi.AbstractBean;

@Singleton
public class CompanyBusinessTestHelper extends AbstractBean implements Serializable {

	private static final long serialVersionUID = -6893154890151909538L;
	
	private static CompanyBusinessTestHelper INSTANCE;
	
	@Inject private SaleBusiness saleBusiness;
	@Inject private SaleCashRegisterMovementBusiness saleCashRegisterMovementBusiness;
	
	@Override
	protected void initialisation() {
		INSTANCE = this;
		super.initialisation();
	}
	
	public void set(SaleStockInput saleStockInput,Customer customer,String cost,String commission,String quantity,Date date){
    	saleStockInput.getSale().setCompleted(Boolean.TRUE);
    	saleStockInput.getSale().setDate(date);
    	saleStockInput.setExternalIdentifier(RandomStringUtils.randomNumeric(3)+RandomStringUtils.randomAlphabetic(1).toUpperCase());
    	saleStockInput.getTangibleProductStockMovement().setQuantity(new BigDecimal(quantity));
    	SaleProduct saleProduct = saleStockInput.getSale().getSaleProducts().iterator().next();
    	
    	saleProduct.setQuantity(new BigDecimal("1"));
    	saleProduct.setPrice(new BigDecimal(cost));
    	saleProduct.setCommission(new BigDecimal(commission));
    	
    	saleBusiness.applyChange(saleStockInput.getSale(), saleProduct);
    	saleStockInput.getSale().setCustomer(customer);
    }
	public void set(SaleStockInput saleStockInput,Customer customer,String cost,String commission,String quantity){
		set(saleStockInput, customer, cost, commission, quantity, null);
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
	
	/**/
	
	public static CompanyBusinessTestHelper getInstance() {
		return INSTANCE;
	}
	
}
