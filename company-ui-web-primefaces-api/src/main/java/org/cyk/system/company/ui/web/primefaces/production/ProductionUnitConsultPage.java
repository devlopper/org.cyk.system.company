package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.business.api.production.ResellerBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.ProductionUnit;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.ui.web.primefaces.CompanyWebManager;
import org.cyk.system.root.business.api.Crud;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.ui.api.command.UICommandable;
import org.cyk.ui.web.primefaces.Table;
import org.cyk.ui.web.primefaces.page.crud.AbstractConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class ProductionUnitConsultPage extends AbstractConsultPage<ProductionUnit> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	private Table<ProductionPlanDetails> productionPlanTable;
	private Table<ResellerDetails> resellerProductTable;

	@Override
	protected void initialisation() {
		super.initialisation();
		
		productionPlanTable = (Table<ProductionPlanDetails>) createDetailsTable(ProductionPlanDetails.class, new DetailsConfigurationListener.Table.Adapter<ProductionPlan,ProductionPlanDetails>(ProductionPlan.class, ProductionPlanDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<ProductionPlan> getIdentifiables() {
				return inject(ProductionPlanBusiness.class).findByProductionUnit(identifiable);
			}
			@Override
			public Crud[] getCruds() {
				return new Crud[]{Crud.UPDATE,Crud.READ};
			}
			@Override
			public String getTabId() {
				return tabId;
			}
		});
		
		resellerProductTable = (Table<ResellerDetails>) createDetailsTable(ResellerDetails.class, new DetailsConfigurationListener.Table.Adapter<Reseller,ResellerDetails>(Reseller.class, ResellerDetails.class){
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<Reseller> getIdentifiables() {
				return inject(ResellerBusiness.class).findByProductionUnit(identifiable);
			}
			/*
			@Override
			public Crud[] getCruds() {
				return new Crud[]{Crud.READ};
			}*/
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
			product = productionPlan.getManufacturedProduct().getProduct().getName();
			energy = productionPlan.getEnergy().getName();
			nextReportDate = timeBusiness.formatDate(productionPlan.getNextReportDate());
		}
	}
	
	public static class ResellerDetails extends AbstractOutputDetails<Reseller> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String registrationCode,names,salary,amountGap,payable;
		public ResellerDetails(Reseller reseller) {
			super(reseller);
			registrationCode = reseller.getCode();
			names = reseller.getPerson().getNames();
			salary = numberBusiness.format(reseller.getSalary());
			amountGap = numberBusiness.format(reseller.getAmountGap());
			payable = numberBusiness.format(reseller.getPayable());
		}
	}
	
}