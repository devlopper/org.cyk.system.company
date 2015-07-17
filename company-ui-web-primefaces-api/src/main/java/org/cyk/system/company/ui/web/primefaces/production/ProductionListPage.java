package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.ProductionSpreadSheet;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.model.table.Row;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Named @ViewScoped @Getter @Setter
public class ProductionListPage extends AbstractCrudManyPage<ProductionSpreadSheet> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		
		table.setShowOpenCommand(Boolean.TRUE);
		table.getOpenRowCommandable().setRendered(Boolean.TRUE);
		
		table.getPrintCommandable().setRendered(Boolean.FALSE);
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ProductionSpreadSheet.class);
	}
	
	@Override
	protected void rowAdded(Row<Object> row) {
		super.rowAdded(row);
		row.setOpenable(Boolean.TRUE);
	}
	
}