package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class StockableTangibleProductConsultPage extends AbstractConsultPage<StockableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
}
