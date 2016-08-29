package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.root.business.impl.AbstractOutputDetails;
import org.cyk.utility.common.Constant;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductCollectionItemDetails extends AbstractOutputDetails<SalableProductCollectionItem> implements Serializable{
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText
	private String unitPrice,quantity,instances;
	
	@IncludeInputs(layout=Layout.VERTICAL) private CostDetails cost;
	
	public SalableProductCollectionItemDetails(SalableProductCollectionItem salableProductCollectionItem) {
		super(salableProductCollectionItem);
		//this.code = salableProductCollectionItem.getSalableProduct().getProduct().getCode();
		//this.name = salableProductCollectionItem.getSalableProduct().getProduct().getName();
		this.unitPrice = salableProductCollectionItem.getSalableProduct().getPrice()==null?Constant.EMPTY_STRING:formatNumber(salableProductCollectionItem.getSalableProduct().getPrice());
		this.quantity = formatNumber(salableProductCollectionItem.getQuantity());
		this.cost = new CostDetails(salableProductCollectionItem.getCost());
	}
	
	/**/
	
	public static final String FIELD_UNIT_PRICE = "unitPrice";
	public static final String FIELD_QUANTITY = "quantity";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_COST = "cost";
}