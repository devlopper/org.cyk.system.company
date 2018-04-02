package org.cyk.system.company.ui.web.primefaces.stock;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.stock.StockableProduct;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class StockableTangibleProductEditPage extends AbstractCrudOnePage<StockableProduct> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	public static class Form extends AbstractFormModel<StockableProduct> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo private Product tangibleProduct;
		
	}

}
