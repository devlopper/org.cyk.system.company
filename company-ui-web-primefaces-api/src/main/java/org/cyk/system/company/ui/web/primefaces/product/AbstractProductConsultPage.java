package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.Product;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Getter @Setter
public abstract class AbstractProductConsultPage<PRODUCT extends Product> extends AbstractConsultPage<PRODUCT> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	
}
