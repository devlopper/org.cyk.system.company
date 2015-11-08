package org.cyk.system.company.ui.web.primefaces.production;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.product.Product;
import org.cyk.system.company.model.production.Reseller;
import org.cyk.system.company.model.production.ResellerProduct;
import org.cyk.system.root.business.api.BusinessEntityInfos;
import org.cyk.ui.api.data.collector.form.AbstractFormModel;
import org.cyk.ui.web.primefaces.page.crud.AbstractCrudOnePage;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ResellerProductEditPage extends AbstractCrudOnePage<ResellerProduct> implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
				
	@Override
	protected BusinessEntityInfos fetchBusinessEntityInfos() {
		return uiManager.businessEntityInfos(ResellerProduct.class);
	}
	
	@Override
	protected Class<? extends AbstractFormModel<?>> __formModelClass__() {
		return Form.class;
	}
			
	/**/
	@Getter @Setter @AllArgsConstructor @NoArgsConstructor
	public static class Form extends AbstractFormModel<ResellerProduct> implements Serializable {
		private static final long serialVersionUID = -731657715703646576L;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Reseller reseller;
		@Input @InputChoice @InputOneChoice @InputOneCombo @NotNull private Product product;
		@Input @InputNumber @NotNull private BigDecimal takingUnitPrice;
		@Input @InputNumber @NotNull private BigDecimal saleUnitPrice;
		@Input @InputNumber @NotNull private BigDecimal commissionRate;
		/*
		@Override
		public void write() {
			super.write();
			identifiable.setTakingUnitPrice(takingUnitPrice);
			identifiable.setSaleUnitPrice(saleUnitPrice);
			identifiable.setCommissionRate(commissionRate);
			identifiable.setp.setToDate(identifiable.getPeriod().getFromDate());
		}*/
	}
	
}