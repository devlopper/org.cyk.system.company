package org.cyk.system.company.business.impl.integration;

import java.io.Serializable;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.party.StoreBusiness;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.model.security.Installation;
import org.cyk.utility.common.helper.ClassHelper;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(ApplicationBusinessImpl.Listener.class, ApplicationBusinessAdapter.class);
    }
    
    @Override
    protected void businesses() {
    	for(Object[] array : new Object[][]{{"ENTREPOT","Entrepot"},{"BCOC","Boutique Cocody"},{"BYOP","Boutique yopougon"}}){
    		Store store = inject(StoreBusiness.class).instanciateOne();
    		store.setCode((String)array[0]);
    		store.setName((String)array[1]);
    		store.setHasPartyAsCompany(Boolean.TRUE);
    		inject(GenericBusiness.class).create(store);
    	}
    	
		for(Object[] array : new Object[][]{{"WONM001","BATIK DE WOODIN",1845},{"WONM002","REVOLUTION DE WOODIN",1845},{"WOM001","TRESOR DE WOODIN",3810}}){
			Product product = inject(ProductBusiness.class).instanciateOne();
			product.setCode((String)array[0]);
			product.setName((String)array[1]);
			product.setStockable(Boolean.TRUE).setSalable(Boolean.TRUE).setStorable(Boolean.TRUE).addStoresFromCode("ENTREPOT","BCOC","BYOP")
				.setSalableProductPropertiesPriceFromObject(array[2]);
			inject(GenericBusiness.class).create(product);
		}
    	
    	System.exit(0);
    }  

    /**/
    
    public static class ApplicationBusinessAdapter extends AbstractBusinessIT.ApplicationBusinessAdapter implements Serializable {
		private static final long serialVersionUID = 1L;
    	
		@Override
		public void installationStarted(Installation installation) {
			super.installationStarted(installation);
			installation.setIsCreateAccounts(Boolean.TRUE);
			installation.getApplication().setUniformResourceLocatorFiltered(Boolean.FALSE);
		}
		
    }
}
