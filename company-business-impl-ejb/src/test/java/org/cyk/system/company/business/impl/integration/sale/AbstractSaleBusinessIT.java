package org.cyk.system.company.business.impl.integration.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.model.sale.Customer;

public abstract class AbstractSaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
     
    @Override
    protected void populate() {
    	super.populate();
    	rootBusinessTestHelper.createActors(Customer.class, new String[]{"C1","C2","C3","C4","C5","C5np","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15"});
    	createProducts(4, 4);
    	createSalableProducts(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"TP4", null},{"IP2", "700"} });
    	createStockableTangibleProducts(new String[][]{ {"TP1", "0","100","100"},{"TP3", "5","95","90"},{"TP4", null,null,null} });
    }
    
    protected void createSale(CreateSaleParameters p){
    	companyBusinessTestHelper.createSale(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getCustomerRegistrationCode(), p.getProducts(), p.getPaid(),p.getTaxable());
    }
    
    protected void updateSale(UpdateSaleParameters p){
    	companyBusinessTestHelper.updateSale(p.getComputedIdentifier(), p.getFiniteStateMachineAlphabetCode(),p.getTaxable());
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
}
