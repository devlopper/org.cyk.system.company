package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.api.production.ProductionPlanBusiness;
import org.cyk.system.company.model.production.ProductionPlan;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProductionPlan;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class ResellerProductionPlanEditPage extends AbstractCrudOnePage<ResellerProductionPlan> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
	
	@Override
	protected ResellerProductionPlan instanciateIdentifiable() {
		ResellerProductionPlan instance = super.instanciateIdentifiable();
		instance.setProductionPlan(inject(ProductionPlanBusiness.class).find(requestParameterLong(ProductionPlan.class)));
		return instance;
	}
				
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ResellerProductionPlan.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<ResellerProductionPlan> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Reseller reseller;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private ProductionPlan productionPlan;
		
		@Input @InputNumber @NotNull private BigDecimal takingUnitPrice;
		@Input @InputNumber @NotNull private BigDecimal saleUnitPrice;
		@Input @InputNumber @NotNull private BigDecimal commissionRate;
		
	}
	
}