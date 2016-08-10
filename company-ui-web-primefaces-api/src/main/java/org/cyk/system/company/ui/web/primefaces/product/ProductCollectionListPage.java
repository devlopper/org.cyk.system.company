package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.ProductCollection;

@Named @ViewScoped @Getter @Setter
public class ProductCollectionListPage extends AbstractProductListPage<ProductCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/**/
	
	public static class Adapter extends AbstractAdapter<ProductCollection> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public Adapter() {
			super(ProductCollection.class);
		}
		
	}
}
