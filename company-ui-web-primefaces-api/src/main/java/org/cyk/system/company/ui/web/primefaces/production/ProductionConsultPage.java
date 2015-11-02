package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlanMetric;
import org.cyk.system.company.model.production.ProductionPlanResource;
import org.cyk.system.company.model.production.ProductionValue;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadsheet;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;

@Named @ViewScoped @Getter @Setter
public class ProductionConsultPage extends AbstractConsultPage<Production> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private ProductionSpreadsheet spreadsheet;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		spreadsheet = new ProductionSpreadsheet(identifiable, 
				new ArrayList<ProductionPlanResource>(identifiable.getRows()), 
				new ArrayList<ProductionPlanMetric>(identifiable.getColumns()), 
				new ArrayList<ProductionValue>(identifiable.getCells()));	
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().productionContextCommandables(getUserSession());
	}
	
}