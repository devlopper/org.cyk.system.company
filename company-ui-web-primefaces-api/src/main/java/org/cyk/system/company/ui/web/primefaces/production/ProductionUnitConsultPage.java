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
import org.cyk.system.company.model.production.ProductionUnit;
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
public class ProductionUnitConsultPage extends AbstractConsultPage<ProductionUnit> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private FormOneData<Details> details;
	private Table<ProductionPlanDetails> productionPlanTable;

	@Override
	protected void initialisation() {
		super.initialisation();
		details = createDetailsForm(Details.class, identifiable, new DetailsFormOneDataConfigurationAdapter<Production,Details>(Production.class, Details.class){
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
		
		productionPlanTable = (Table<ProductionPlanDetails>) createDetailsTable(ProductionPlanDetails.class, new DetailsTableConfigurationAdapter<ProductionPlan,ProductionPlanDetails>(ProductionPlan.class, ProductionPlanDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<ProductionPlan> getIdentifiables() {
				return CompanyBusinessLayer.getInstance().getProductionPlanBusiness().findByProductionUnit(identifiable);
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
	
	public static class Details extends AbstractOutputDetails<ProductionUnit> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String name;
		public Details(ProductionUnit productionUnit) {
			super(productionUnit);
			name = productionUnit.getCompany().getName();
		}
	}
	
	public static class ProductionPlanDetails extends AbstractOutputDetails<ProductionPlan> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String product,energy,nextReportDate;
		public ProductionPlanDetails(ProductionPlan productionPlan) {
			super(productionPlan);
			product = productionPlan.getProduct().getName();
			energy = productionPlan.getEnergy().getName();
			nextReportDate = timeBusiness.formatDate(productionPlan.getNextReportDate());
		}
	}
	
}