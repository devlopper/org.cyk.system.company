package org.cyk.system.company.business.impl.integration.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.model.sale.Customer;

public abstract class AbstractSaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    protected static final String CUST1="C1",CUST2="C2",CUST3="C3",CUST4="C4",CUST5="C5",CUST6="C6",CUST7="C7",CUST8="C8",CUST9="C9",CUST10="C10",CUST11="C11",CUST12="C12";
    
    @Override
    protected void populate() {
    	super.populate();
    	rootBusinessTestHelper.createActors(Customer.class, new String[]{CUST1,CUST2,CUST3,CUST4,CUST5,"C5np",CUST6,CUST7,CUST8,CUST9,CUST10,CUST11,CUST12,"C13","C14","C15"});
    	createProducts(4, 4);
    	createSalableProducts(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"TP4", null},{"IP2", "700"} });
    	createStockableTangibleProducts(new String[][]{ {"TP1", "0","100","100"},{"TP3", "5","95","90"},{"TP4", null,null,null} });
    }
    
    protected void createSale(CreateSaleParameters p){
    	companyBusinessTestHelper.createSale(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getCustomerRegistrationCode(), p.getProducts(), p.getPaid(),p.getTaxable());
    	writeReport(companyBusinessLayer.getSaleBusiness().findReport(saleDao.readByComputedIdentifier(p.getComputedIdentifier())));
    }
    
    protected void updateSale(UpdateSaleParameters p){
    	companyBusinessTestHelper.updateSale(p.getComputedIdentifier(), p.getFiniteStateMachineAlphabetCode(),p.getTaxable());
    }
    
    protected void createSaleStock(CreateSaleStockInputParameters p){
    	companyBusinessTestHelper.createSaleStockTangibleProductMovementInput(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getCustomerRegistrationCode(), p.getPrice(),p.getTaxable(),p.getQuantity());
    }
    
    protected void createSaleStock(CreateSaleStockOutputParameters p){
    	companyBusinessTestHelper.createSaleStockTangibleProductMovementOutput(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getPaid(),p.getQuantity());
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
    public static class CreateSaleStockInputParameters extends CreateSaleParameters{
    	
    	private String[][] stocks;
    	private String quantity,price;
    	
    	public CreateSaleStockInputParameters(String computedIdentifier,String date, String cashierCode,String customerRegistrationCode,
				String price, String taxable,String quantity) {
			super(computedIdentifier, date, cashierCode, customerRegistrationCode,null, null, taxable);
			this.quantity = quantity;
			this.price = price;
		}	
    }
    
    @Getter @Setter
    public static class CreateSaleStockOutputParameters extends AbstractSaleParameters{
    	private String date,cashierCode,paid,quantity;
		public CreateSaleStockOutputParameters(String computedIdentifier,String date,String cashierCode,String paid,String quantity) {
			super(computedIdentifier);
			this.date=date;
			this.cashierCode = cashierCode;
			this.paid = paid;
			this.quantity = quantity;
		}
    }
}
