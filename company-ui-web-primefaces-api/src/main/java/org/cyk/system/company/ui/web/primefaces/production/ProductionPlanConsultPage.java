package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.api.model.AbstractOutputDetails;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.data.collector.form.FormOneData;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class ProductionPlanConsultPage extends AbstractConsultPage<ProductionPlan> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private FormOneData<Details> details;
	private Table<ProductionDetails> productionTable;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(Details.class, identifiable, new DetailsFormOneDataConfigurationAdapter<ProductionPlan,Details>(ProductionPlan.class, Details.class){
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
		
		productionTable = (Table<ProductionDetails>) createDetailsTable(ProductionDetails.class, new DetailsTableConfigurationAdapter<Production,ProductionDetails>(Production.class, ProductionDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<Production> getIdentifiables() {
				return CompanyBusinessLayer.getInstance().getProductionBusiness().findByProductionPlan(identifiable);
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
	
	public static class Details extends AbstractOutputDetails<ProductionPlan> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String productionUnit,product,energy,previousReportDate,nextReportDate,reportIntervalTimeDivisionType,comments,
			resources,metrics;		
		public Details(ProductionPlan productionPlan) {
			super(productionPlan);
			productionUnit = productionPlan.getProductionUnit().getCompany().getName();
			product = productionPlan.getProduct().getName();
			energy = productionPlan.getEnergy().getName();
			previousReportDate = timeBusiness.formatDate(productionPlan.getPreviousReportDate());
			nextReportDate = timeBusiness.formatDate(productionPlan.getNextReportDate());
			reportIntervalTimeDivisionType = productionPlan.getReportIntervalTimeDivisionType().getName();
			comments = productionPlan.getComments();
			resources = productionPlan.getRows().toString();
			metrics = productionPlan.getColumns().toString();
		}
	}
	
	public static class ProductionDetails extends AbstractOutputDetails<Production> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String date;
		public ProductionDetails(Production production) {
			super(production);
			date = timeBusiness.formatDate(production.getPeriod().getFromDate());
		}
	}
	
}