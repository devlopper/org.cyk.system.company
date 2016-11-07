package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.Sale;

@Getter @Setter @Named @ViewScoped
public class SaleListPage extends AbstractSalableProductCollectionListPage<Sale,SalableProductCollectionItem> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
				
}