package org.cyk.system.company.ui.web.primefaces.product;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.system.company.model.product.TangibleProductInventory;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class TangibleProductInventoryConsultPage extends AbstractConsultPage<TangibleProductInventory> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	/*
	private Table<TangibleProductInventoryReportTableDetails> detailsTable;
	
	@Override
	protected void afterInitialisation() {
		super.afterInitialisation();
		Collection<TangibleProductInventoryReportTableDetails> details = new ArrayList<>();
		for(TangibleProductInventoryDetail tangibleProductInventoryDetail : identifiable.getDetails())
			details.add(new TangibleProductInventoryReportTableDetails(tangibleProductInventoryDetail));
		//detailsTable = createDetailsTable(TangibleProductInventoryReportTableDetails.class, details, "model.entity.tangibleProductInventory");	
		detailsTable.setTitle(null);
		detailsTable.setShowHeader(Boolean.FALSE);
		detailsTable.setShowFooter(Boolean.FALSE);
		detailsTable.setShowToolBar(Boolean.TRUE);
		detailsTable.setIdentifiableClass(TangibleProductInventory.class);
		//BusinessEntityInfos tangibleProductInventoryEntityInfos = RootBusinessLayer.getInstance().getApplicationBusiness().findBusinessEntityInfos(TangibleProductInventory.class);
		//detailsTable.getPrintCommandable().setParameter(tangibleProductInventoryEntityInfos.getIdentifier(),identifiable.getIdentifier());
	}*/
	
}