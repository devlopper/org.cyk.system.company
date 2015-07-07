package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.ProductionSpreadSheetTemplate;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Named @ViewScoped @Getter @Setter
public class ProductionPlanModelListPage extends AbstractCrudManyPage<ProductionSpreadSheetTemplate> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ProductionSpreadSheetTemplate.class);
	}
	
}