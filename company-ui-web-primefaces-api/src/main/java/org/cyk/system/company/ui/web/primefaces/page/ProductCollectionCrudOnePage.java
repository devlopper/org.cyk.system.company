package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;
import org.cyk.ui.api.command.UICommand;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named @ViewScoped @Getter @Setter
public class ProductCollectionCrudOnePage extends AbstractCrudOnePage<ProductCollection> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private ProductBusiness productBusiness;
	
	private DualListModel<Product> dualListModel;
	private List<Product> products = new ArrayList<>();
	private List<Product> selectedProducts,list=new ArrayList<>();
	private BigDecimal totalPrice = BigDecimal.ZERO;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		selectedProducts = new ArrayList<Product>(identifiable.getCollection());
		for(Product product : productBusiness.findAllNot(ProductCollection.class))
			if(!selectedProducts.contains(product))
				products.add(product);
		
		list = new ArrayList<Product>(identifiable.getCollection());//TODO a trick because dual list model target is empty when submitting
		
		dualListModel = new DualListModel<Product>(products, selectedProducts);
		for(Product product : selectedProducts)
			totalPrice = totalPrice.add(product.getPrice());
	}
	
	/**/
	
	@Override
	public void transfer(UICommand command, Object parameter) throws Exception {}
	
	@Override
	public Boolean validate(UICommand command, Object parameter) {return Boolean.TRUE;}
	
	@Override
	public void serve(UICommand command, Object parameter) {
		identifiable.setCollection(list);
		super.serve(command, parameter);
	}
	
	@SuppressWarnings("unchecked")
	public void onTransfer(TransferEvent event) {
	    if(Boolean.TRUE.equals(event.isAdd())){
	    	for(Product product : (List<Product>)event.getItems())
	        	identifiable.getCollection().add(product);
	    }else{
	    	for(Product product : (List<Product>)event.getItems())
	        	identifiable.getCollection().remove(product);
	    }
        
        totalPrice = identifiable.getTotalPrice();
        list = new ArrayList<Product>(identifiable.getCollection());
        //for(Product product : (List<Product>)event.getItems())
        //	totalPrice = totalPrice.add( new BigDecimal(event.isAdd()?1:-1).multiply(product.getPrice()));
    } 
	
	
	
}