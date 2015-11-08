package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.cyk.system.company.business.impl.CompanyBusinessLayer;
import org.cyk.system.company.model.production.Production;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduction;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class ResellerProductionEditPage extends AbstractCrudOnePage<ResellerProduction> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
	}
	
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ResellerProduction.class);
	}
	
	@Override
	protected ResellerProduction instanciateIdentifiable() {
		ResellerProduction instance = super.instanciateIdentifiable();
		instance.setProduction(CompanyBusinessLayer.getInstance().getProductionBusiness().find(requestParameterLong(Production.class)));
		return instance;
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<ResellerProduction> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		@Input @InputChoice @InputOneChoice @InputOneCombo private Reseller reseller;
		@Input @InputChoice @InputOneChoice @InputOneCombo private Production production;
		@Input @InputNumber private BigDecimal takenQuantity,soldQuantity,returnedQuantity,paidAmount;
		@Override
		public void write() {
			super.write();
			reseller = identifiable.getReseller();
			production = identifiable.getProduction();
			identifiable.setTakenQuantity(takenQuantity);
			identifiable.setSoldQuantity(soldQuantity);
			identifiable.setReturnedQuantity(returnedQuantity);
			identifiable.getAmount().setUser(paidAmount);
		}
	}
	
}