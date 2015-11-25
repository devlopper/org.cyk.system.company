package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.company.ui.web.primefaces.model.ProductionSpreadsheet;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class ProductionConsultPage extends AbstractConsultPage<Production> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private ProductionSpreadsheet spreadsheet;
	private FormOneData<Details> details;
	private Table<ResellerProductionDetails> resellerTable;
	private String tabId = "infos";
	
	@Override
	protected void initialisation() {
		super.initialisation();
		spreadsheet = new ProductionSpreadsheet(identifiable);	
		details = createDetailsForm(Details.class, identifiable, new DetailsConfigurationListener.Form.Adapter<Production,Details>(Production.class, Details.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean getEnabledInDefaultTab() {
				return Boolean.TRUE;
			}
			@Override
			public String getTabId() {
				return "tabId";
			}
		});
		
		resellerTable = (Table<ResellerProductionDetails>) createDetailsTable(ResellerProductionDetails.class, new DetailsConfigurationListener.Table.Adapter<ResellerProduction,ResellerProductionDetails>(ResellerProduction.class, ResellerProductionDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<ResellerProduction> getIdentifiables() {
				return CompanyBusinessLayer.getInstance().getResellerProductionBusiness().findByProduction(identifiable);
			}
			@Override
			public ResellerProductionDetails createData(ResellerProduction identifiable) {
				return super.createData(identifiable);
			}
			@Override
			public Crud[] getCruds() {
				return new Crud[]{Crud.CREATE,Crud.READ,Crud.UPDATE,Crud.DELETE};
			}
			@Override
			public String getTabId() {
				return tabId;
			}
		});
	}
	
	@Override
	protected Collection<UICommandable> contextualCommandables() {
		return CompanyWebManager.getInstance().productionContextCommandables(getUserSession());
	}
	
	/**/
	
	public static class Details extends AbstractOutputDetails<Production> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String date;
		public Details(Production production) {
			super(production);
			date = timeBusiness.formatDate(production.getPeriod().getFromDate());
		}
	}
	
	
	
}