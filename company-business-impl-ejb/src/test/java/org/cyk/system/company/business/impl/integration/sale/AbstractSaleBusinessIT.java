package org.cyk.system.company.business.impl.integration.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.model.product.IntangibleProduct;
import org.cyk.system.company.model.product.TangibleProduct;
import org.cyk.system.company.model.sale.Customer;

public abstract class AbstractSaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
     
    @Override
    protected void populate() {
    	super.populate();
    	rootBusinessTestHelper.createActors(Customer.class, new String[]{"C1","C2","C3","C4","C5","C5np","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15"});
    	createProducts(4, 4);
    	create(new IntangibleProduct(IntangibleProduct.STOCKING, IntangibleProduct.STOCKING, null, null));
    	create(new TangibleProduct(TangibleProduct.STOCKING, TangibleProduct.STOCKING, null, null));
    	createSalableProducts(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"TP4", null},{"IP2", "700"},{IntangibleProduct.STOCKING, null} });
    	createStockableTangibleProducts(new String[][]{ {"TP1", "0","100","100"},{"TP3", "5","95","90"},{"TP4", null,null,null},{TangibleProduct.STOCKING, null,null,null} });
    }
    
    protected void createSale(CreateSaleParameters p){
    	companyBusinessTestHelper.createSale(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getCustomerRegistrationCode(), p.getProducts(), p.getPaid(),p.getTaxable());
    }
    
    protected void updateSale(UpdateSaleParameters p){
    	companyBusinessTestHelper.updateSale(p.getComputedIdentifier(), p.getFiniteStateMachineAlphabetCode(),p.getTaxable());
    }
    
    protected void createSaleStock(CreateSaleStockParameters p){
    	companyBusinessTestHelper.createSaleStockTangibleProductMovementInput(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getCustomerRegistrationCode(), p.getPaid(),p.getTaxable(),p.getQuantity());
    }
      
    /**/
    
    @Getter @Setter @AllArgsConstructor
    public static class AbstractSaleParameters{
    	/* inputs */
    	protected String computedIdentifier;
  
    	
    }
    
    @Getter @Setter
    public static class CreateSaleParameters extends AbstractSaleParameters{
    	private String date,cashierCode;
    	private String customerRegistrationCode;
    	private String[][] products;
    	private String paid,taxable;
		public CreateSaleParameters(String computedIdentifier, String date,
				String cashierCode, String customerRegistrationCode,
				String[][] products, String paid, String taxable) {
			super(computedIdentifier);
			this.date = date;
			this.cashierCode = cashierCode;
			this.customerRegistrationCode = customerRegistrationCode;
			this.products = products;
			this.paid = paid;
			this.taxable = taxable;
		}
    	
    }
    
    @Getter @Setter
    public static class UpdateSaleParameters extends AbstractSaleParameters{
    	private String finiteStateMachineAlphabetCode,taxable;
		public UpdateSaleParameters(String computedIdentifier,
				String finiteStateMachineAlphabetCode, String taxable) {
			super(computedIdentifier);
			this.finiteStateMachineAlphabetCode = finiteStateMachineAlphabetCode;
			this.taxable = taxable;
		}
    }
    
    @Getter @Setter
    public static class CreateSaleStockParameters extends CreateSaleParameters{
    	
    	private String[][] stocks;
    	private String quantity;
    	
    	public CreateSaleStockParameters(String computedIdentifier,String date, String cashierCode,String customerRegistrationCode,
				String paid, String taxable,String quantity) {
			super(computedIdentifier, date, cashierCode, customerRegistrationCode,null, paid, taxable);
			this.quantity = quantity;
		}

		
    }
}
