package org.cyk.system.company.ui.web.primefaces.sale;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.ui.web.primefaces.CostFormModel;
import org.cyk.ui.web.primefaces.page.AbstractCollectionItemEditPage;
import org.cyk.utility.common.annotation.FieldOverride;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputChoice;
import org.cyk.utility.common.annotation.user.interfaces.InputChoiceAutoComplete;
import org.cyk.utility.common.annotation.user.interfaces.InputNumber;
import org.cyk.utility.common.annotation.user.interfaces.InputOneAutoComplete;
import org.cyk.utility.common.annotation.user.interfaces.InputOneChoice;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class SalableProductCollectionItemEditPage extends AbstractCollectionItemEditPage.Extends<SalableProductCollectionItem,SalableProductCollection> implements Serializable {

	private static final long serialVersionUID = 3274187086682750183L;
	
	@Override
	protected void initialisation() {
		super.initialisation();
		
	}
	
	@Getter @Setter @FieldOverride(name=Form.FIELD_COLLECTION,type=SalableProductCollection.class)
	public static class Form extends AbstractCollectionItemEditPage.AbstractForm.AbstractDefault<SalableProductCollectionItem,SalableProductCollection> implements Serializable{
		private static final long serialVersionUID = -4741435164709063863L;
	
		@Input @InputChoice @InputChoiceAutoComplete @InputOneChoice @InputOneAutoComplete @NotNull private SalableProduct salableProduct;
		@Input @InputNumber @NotNull private BigDecimal quantity;
		@Input @InputNumber @NotNull private BigDecimal reduction=BigDecimal.ZERO;
		@Input @InputNumber @NotNull private BigDecimal commission = BigDecimal.ZERO;
		
		@IncludeInputs(layout=IncludeInputs.Layout.VERTICAL) private CostFormModel cost = new CostFormModel();
	
		/**/
		
		public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
		public static final String FIELD_QUANTITY = "quantity";
		public static final String FIELD_REDUCTION = "reduction";
		public static final String FIELD_COMMISSION = "commission";
		
		public static final String FIELD_COST = "cost";
	}

}
