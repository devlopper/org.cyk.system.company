package org.cyk.system.company.business.impl.integration;

import java.math.BigDecimal;

import org.cyk.system.company.business.api.product.IntangibleProductBusiness;
import org.cyk.system.company.business.api.product.TangibleProductBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionBusiness;
import org.cyk.system.company.business.api.sale.SalableProductCollectionItemBusiness;
import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionDao;
import org.cyk.utility.common.ObjectFieldValues;

public class SalableProductCollectionBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
   
    @Override
    protected void populate() {
    	super.populate();
    	inject(TangibleProductBusiness.class).create( inject(TangibleProductBusiness.class).instanciateMany(new String[][]{ {"TP1"},{"TP2"},{"TP3"} }) );
    	inject(IntangibleProductBusiness.class).create( inject(IntangibleProductBusiness.class).instanciateMany(new String[][]{ {"IP1"} }) );
    	//inject(SalableProductBusiness.class).create( inject(SalableProductBusiness.class)
    	//		.instanciateManyByProductCodes(new String[][]{ {"TP1","100"},{"TP2","50"},{"TP3","25"},{"IP1",null} }) );
    }
    
    @Override
    protected void businesses() {
    	SalableProductCollection salableProductCollection = inject(SalableProductCollectionBusiness.class).instanciateOne("SPC01");
    	SalableProductCollectionItem item1 = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP3", "1", "0", "0");
    	companyBusinessTestHelper.assertCost(item1.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	SalableProductCollectionItem item2 = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP1", "2", "0", "0");
    	companyBusinessTestHelper.assertCost(item2.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "200"));
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "225"));
    	create(salableProductCollection);
    	companyBusinessTestHelper.assertCost((salableProductCollection = inject(SalableProductCollectionDao.class).read(salableProductCollection.getIdentifier()))
    			.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "225"));
    	
    	item1 = inject(SalableProductCollectionItemBusiness.class).find(item1.getCode());
    	item2 = inject(SalableProductCollectionItemBusiness.class).find(item2.getCode());
    	
    	item2.getCost().setValue(BigDecimal.ONE);
    	update(item2);
    	item2 = inject(SalableProductCollectionItemBusiness.class).find(item2.getIdentifier());
    	companyBusinessTestHelper.assertCost(item2.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "1"));
    	
    	inject(SalableProductCollectionItemBusiness.class).computeCost(item2);
    	update(item2);
    	item2 = inject(SalableProductCollectionItemBusiness.class).find(item2.getIdentifier());
    	companyBusinessTestHelper.assertCost(item2.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "200"));
    	
    	salableProductCollection = inject(SalableProductCollectionBusiness.class).find(salableProductCollection.getIdentifier());
    	inject(SalableProductCollectionBusiness.class).computeCost(salableProductCollection);
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "225"));
    	
    	inject(SalableProductCollectionBusiness.class).remove(salableProductCollection, item2);
    	update(salableProductCollection);
    	salableProductCollection = inject(SalableProductCollectionBusiness.class).find(salableProductCollection.getIdentifier());
    	inject(SalableProductCollectionBusiness.class).computeCost(salableProductCollection);
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "25"));
    	
    	SalableProductCollectionItem item3 = inject(SalableProductCollectionItemBusiness.class).instanciateOne(salableProductCollection, "TP2", "3", "0", "0");
    	item3.setCascadeOperationToMaster(Boolean.TRUE);
    	create(item3);
    	salableProductCollection = inject(SalableProductCollectionBusiness.class).find(salableProductCollection.getIdentifier());
    	companyBusinessTestHelper.assertCost(salableProductCollection.getCost(), new ObjectFieldValues(Cost.class).set(Cost.FIELD_VALUE, "175"));
    }
    
    /* Exceptions */
    

}
