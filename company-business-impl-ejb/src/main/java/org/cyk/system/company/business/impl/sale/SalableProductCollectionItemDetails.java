package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.impl.AbstractCollectionItemDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductCollectionItemDetails extends AbstractCollectionItemDetails.AbstractDefault<SalableProductCollectionItem,SalableProductCollection> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String salableProduct,quantity,reduction,commission/*,instances*/;
	
	@IncludeInputs(layout=Layout.VERTICAL) private CostDetails cost;
	
	public SalableProductCollectionItemDetails(SalableProductCollectionItem salableProductCollectionItem) {
		super(salableProductCollectionItem);
		this.salableProduct = formatUsingBusiness(salableProductCollectionItem.getSalableProduct());
		this.quantity = formatNumber(salableProductCollectionItem.getQuantity());
		//this.reduction = formatNumber(salableProductCollectionItem.getReduction());
		//this.commission = formatNumber(salableProductCollectionItem.getCommission());
		this.cost = new CostDetails(salableProductCollectionItem.getCost());
	}
	
	/**/
	
	public static final String FIELD_SALABLE_PRODUCT = "salableProduct";
	public static final String FIELD_QUANTITY = "quantity";
	public static final String FIELD_REDUCTION = "reduction";
	public static final String FIELD_COMMISSION = "commission";
	public static final String FIELD_COST = "cost";
}