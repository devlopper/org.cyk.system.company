package org.cyk.system.company.business.impl.integration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.business.api.product.ProductCategoryBusiness;
import org.cyk.system.company.business.api.product.ProductFamilyBusiness;
import org.cyk.system.company.business.api.product.ProductSizeBusiness;
import org.cyk.system.company.business.api.product.ProductTypeBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCategory;
import org.cyk.system.company.model.product.ProductFamily;
import org.cyk.system.company.model.product.ProductSize;
import org.cyk.system.company.model.product.ProductType;
import org.cyk.system.root.business.api.GenericBusiness;
import org.cyk.system.root.business.api.party.StoreBusiness;
import org.cyk.system.root.business.impl.party.ApplicationBusinessImpl;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.root.model.party.Store;
import org.cyk.system.root.model.security.Installation;
import org.cyk.utility.common.helper.ClassHelper;
import org.cyk.utility.common.helper.RandomHelper;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {
    private static final long serialVersionUID = -6691092648665798471L;
    
    static {
    	ClassHelper.getInstance().map(ApplicationBusinessImpl.Listener.class, ApplicationBusinessAdapter.class);
    }
    
    @Override
    protected void businesses() {
    	List<String> types = getLines("uniwax/product/type.txt");
    	for(String string  : types){
    		ProductType type = inject(ProductTypeBusiness.class).instanciateOne();
    		String[] array = StringUtils.split(string,"\t");
    		type.setCode(array[0]);
    		type.setName(array.length > 1 ? array[1] : array[0]);
    		inject(GenericBusiness.class).create(type);
    	}
    	
    	List<String> families = getLines("uniwax/product/family.txt");
    	for(String string  : families){
			ProductFamily family = inject(ProductFamilyBusiness.class).instanciateOne();
			String[] array = StringUtils.split(string,"\t");
    		family.setCode(array[0]);
    		family.setName(array.length > 1 ? array[1] : array[0]);
			inject(GenericBusiness.class).create(family);
		}
    	
    	List<String> categories = getLines("uniwax/product/category.txt");
    	for(String string  : categories){
    		ProductCategory category = inject(ProductCategoryBusiness.class).instanciateOne();
    		String[] array = StringUtils.split(string,"\t");
    		category.setCode(array[0]);
    		category.setName(array.length > 1 ? array[1] : array[0]);
    		inject(GenericBusiness.class).create(category);
    	}
    	
    	List<String> sizes = getLines("uniwax/product/size.txt");
    	for(String string  : sizes){
    		ProductSize size = inject(ProductSizeBusiness.class).instanciateOne();
    		String[] array = StringUtils.split(string,"\t");
    		size.setCode(array[0]);
    		size.setName(array.length > 1 ? array[1] : array[0]);
    		inject(GenericBusiness.class).create(size);
    	}
    	
    	List<String> stores = getLines("uniwax/store.txt");
    	String storeCodes[] = getCodes(stores).toArray(new String[]{});
    	for(String string  : stores){
    		Store store = inject(StoreBusiness.class).instanciateOne();
    		String[] array = StringUtils.split(string,"\t");
    		store.setCode(array[0]);
    		store.setName(array.length > 1 ? array[1] : array[0]);
    		store.setHasPartyAsCompany(Boolean.TRUE);
    		inject(GenericBusiness.class).create(store);
    	}
    	
    	List<String> products = getLines("uniwax/product/pagne.txt");
    	Collection<AbstractIdentifiable> identifiables = new ArrayList<>();
		for(String string : products){
			Product product = inject(ProductBusiness.class).instanciateOne();
			String[] array = StringUtils.split(string,"\t");
			
			product.setCode(/*array[1]*/RandomHelper.getInstance().getString(5));
			product.setTypeFromCode(getCode(types, array[0]));
			product.setName((String)array[3]);
			product.setSizeFromCode(getCode(sizes, array[2]));
			
			product.setCategoryFromCode(getCode(categories, array[4]));
			product.setFamilyFromCode(getCode(families, array[5]));
			
			product.setStockable(Boolean.TRUE).setSalable(Boolean.TRUE).setStorable(Boolean.TRUE).addStoresFromCode(storeCodes)
				.setSalableProductPropertiesPriceFromObject(1000);
			
			identifiables.add(product);
			
		}
    	
		inject(GenericBusiness.class).create(identifiables);
		
    	System.exit(0);
    }  
    
    private List<String> getLines(String name){
    	try {
			return IOUtils.readLines(getClass().getResourceAsStream(name));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }

    private List<String> getCodes(List<String> strings){
    	List<String> codes = new ArrayList<>();
    	for(String string  : strings){
    		String[] array = StringUtils.split(string,"\t");
    		codes.add(array[0]);
    	}
    	return codes;
    }
    
    private String getCode(List<String> strings,String name){
    	for(String string  : strings){
    		String[] array = StringUtils.split(string,"\t");
    		if(array[1].equals(name))
    			return array[0];
    	}
    	return null;
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
