package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.accounting.AccountingPeriod;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputBooleanButton;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputOneCombo;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionEditPage extends AbstractSalableProductCollectionEditPage<SalableProductCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return identifiable;
	}
	
	@Getter @Setter
	public static class Form extends AbstractDefaultForm<SalableProductCollection> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
		
		@Input @InputChoice @InputOneChoice @InputOneCombo private AccountingPeriod accountingPeriod;
		
		@IncludeInputs(layout=IncludeInputs.Layout.VERTICAL) private CostFormModel cost = new CostFormModel();
		
		@Input @InputBooleanButton private Boolean autoComputeValueAddedTax = Boolean.TRUE;
				
		public static final String FIELD_ACCOUNTINGPERIOD = "accountingPeriod";
		public static final String FIELD_COST = "cost";
		public static final String FIELD_AUTO_COMPUTE_VALUE_ADDED_TAX = "autoComputeValueAddedTax";
		
	}

}
