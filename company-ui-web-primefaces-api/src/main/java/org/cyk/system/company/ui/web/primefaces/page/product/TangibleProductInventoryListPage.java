package org.cyk.system.company.ui.web.primefaces.page.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.product.ProductBusiness;
import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.system.company.model.product.TangibleProductInventoryDetail;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudManyPage;

@Named @ViewScoped @Getter @Setter
public class TangibleProductInventoryListPage extends AbstractCrudManyPage<TangibleProductInventory> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Inject private ProductBusiness productBusiness;
	
	private List<TangibleProductInventoryDetail> details = new ArrayList<>();
	
	@Override
	protected void initialisation() {
		super.initialisation();
		rowAdapter.setOpenable(Boolean.TRUE);
	}
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		table.setShowEditColumn(Boolean.FALSE);
		table.setShowAddRemoveColumn(Boolean.FALSE);
		
		table.setShowOpenCommand(Boolean.TRUE);
		table.getOpenRowCommandable().setRendered(Boolean.TRUE);
		
		table.getPrintCommandable().setRendered(Boolean.FALSE);
		
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().stockContextCommandables(getUserSession());
	}
		
}