package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.model.product.TangibleProduct;

public class ProductBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void populate() {
    	super.populate();
    	TangibleProduct tangibleProduct = new TangibleProduct();
    	companyBusinessTestHelper.set(tangibleProduct, "TP1");
    	create(tangibleProduct);
    }
        
    @Override
    protected void businesses() {
    	
    }
    
}
