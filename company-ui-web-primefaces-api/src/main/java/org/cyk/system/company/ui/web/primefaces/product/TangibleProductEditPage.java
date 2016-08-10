package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.TangibleProduct;

@Named @ViewScoped @Getter @Setter
public class TangibleProductEditPage extends AbstractProductEditPage<TangibleProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static class Form extends AbstractForm<TangibleProduct> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}
	
	public static class Adapter extends AbstractAdapter<TangibleProduct> implements Serializable {

		private static final long serialVersionUID = 4370361826462886031L;

		public Adapter() {
			super(TangibleProduct.class);
			
		}
		
	}
	
}
