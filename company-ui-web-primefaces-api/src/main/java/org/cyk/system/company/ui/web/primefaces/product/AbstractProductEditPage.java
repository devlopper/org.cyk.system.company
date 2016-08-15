package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.ui.api.model.AbstractBusinessIdentifiedEditFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractProductEditPage<PRODUCT extends Product> extends AbstractCrudOnePage<PRODUCT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Getter @Setter
	public static abstract class AbstractForm<PRODUCT extends Product> extends AbstractBusinessIdentifiedEditFormModel<PRODUCT> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
	}
	
}
