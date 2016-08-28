package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.sale.SaleBusiness;
import org.cyk.system.company.model.sale.Sale;

public class SaleBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
   
    @Override
    protected void businesses() {
    	Sale sale = inject(SaleBusiness.class).instanciateOne();
    	
    	create(sale);
    	
    }
    
    /* Exceptions */
    

}
