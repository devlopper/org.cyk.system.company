package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import org.cyk.system.company.model.product.Product;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractProductListPage<PRODUCT extends Product> extends AbstractCrudManyPage<PRODUCT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	/**/
	
}
