package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionItemListPage extends AbstractCrudManyPage<SalableProductCollectionItem> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;

	
}
