package org.cyk.system.company.business.impl.integration.sale;

import org.cyk.system.company.business.impl.integration.AbstractBusinessIT;
import org.cyk.system.company.model.sale.Customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractSaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
     
    @Override
    protected void populate() {
    	super.populate();
    	rootBusinessTestHelper.createActors(Customer.class, new String[]{"C1","C2","C3","C4","C5","C5np","C6","C7","C8","C9","C10","C11","C12","C13","C14","C15"});
    	createProducts(4, 4);
    	createSalableProducts(new String[][]{ {"TP1", "1000"},{"TP3", "500"},{"TP4", null},{"IP2", "700"} });
    }
    
    protected void createSale(CreateSaleParameters p){
    	companyBusinessTestHelper.createSale(p.getComputedIdentifier(), p.getDate(), p.getCashierCode(), p.getCustomerRegistrationCode(), p.getProducts(), p.getPaid(),p.getTaxable());
    }
    
    protected void updateSale(UpdateSaleParameters p){
    	companyBusinessTestHelper.updateSale(p.getComputedIdentifier(), p.getFiniteStateMachineAlphabetCode(),p.getTaxable());
    }
      
    /**/
    
    @Getter @Setter @AllArgsConstructor
    public static class CreateSaleParameters{
    	private String computedIdentifier,date,cashierCode;
    	private String customerRegistrationCode;
    	private String[][] products;
    	private String paid,taxable;
    }
    
    @Getter @Setter @AllArgsConstructor
    public static class UpdateSaleParameters{
    	private String computedIdentifier,finiteStateMachineAlphabetCode,taxable;
    }
}
