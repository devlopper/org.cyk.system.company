package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.ui.web.primefaces.page.party.AbstractActorConsultPage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Named @ViewScoped @Getter @Setter
public class ResellerConsultPage extends AbstractActorConsultPage<Reseller> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	//private FormOneData<ResellerProductDetails> details;
	private String tabId = "infos";
	
	@Override
	protected void initialisation() {
		super.initialisation();
		/*details = createDetailsForm(Details.class, identifiable, new DetailsConfigurationListener.Form.Adapter<Production,Details>(Production.class, Details.class){
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
				return inject(ResellerProductionBusiness.class).findByProduction(identifiable);
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
		});*/
	}
	
	/**/
	
	public static class ResellerProductDetails extends AbstractOutputDetails<ResellerProductionPlan> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		@Input @InputText private String date,product,takingUnitPrice,saleUnitPrice,commissionRate;
		
		public ResellerProductDetails(ResellerProductionPlan resellerProduct) {
			super(resellerProduct);
			
		}
	}
	
	
	
}