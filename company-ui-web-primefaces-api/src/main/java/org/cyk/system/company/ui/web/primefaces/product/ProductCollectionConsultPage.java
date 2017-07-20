package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.product.ProductCollection;

@Named @ViewScoped @Getter @Setter
public class ProductCollectionConsultPage extends AbstractProductConsultPage<ProductCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;


}
