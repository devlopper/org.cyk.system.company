package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.product.ProductCollection;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ProductCollectionEditPage extends AbstractProductEditPage<ProductCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static class Form extends AbstractForm<Product> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}

	
}
