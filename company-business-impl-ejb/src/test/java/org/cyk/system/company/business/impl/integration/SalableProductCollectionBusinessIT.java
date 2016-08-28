package org.cyk.system.company.business.impl.integration;

import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.utility.common.ObjectFieldValues;

public class SalableProductCollectionBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
   
    @Override
    protected void populate() {
    	super.populate();
    	inject(TangibleProductBusiness.class).create( inject(TangibleProductBusiness.class).instanciateMany(new String[][]{ {"TP1"},{"TP2"},{"TP3"} }) );
    	inject(IntangibleProductBusiness.class).create( inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{ {"IP1"} }) );
    	inject(SalableProductBusiness.class).create( inject(SalableProductBusiness.class)
    			.instanciateManyByProductCodes(new String[][]{ {"TP1","100"},{"TP2","50"},{"TP3","25"},{"IP1",null} }) );
    }
    
    @Override
    protected void businesses() {
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne("SPC01");
    	SalableProductCollectionItem item = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP3", "1", "0", "0");
    	create(salableProductCollection);
    	companyBusinessTestHelper.assertCost(item.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	
    	
    }
    
    /* Exceptions */
    

}
