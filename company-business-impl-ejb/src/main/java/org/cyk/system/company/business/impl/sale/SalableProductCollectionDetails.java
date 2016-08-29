package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.root.business.impl.AbstractCollectionDetails;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalableProductCollectionDetails extends AbstractCollectionDetails<SalableProductCollection> implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String accountingPeriod;
	@IncludeInputs(layout=Layout.VERTICAL) private CostDetails cost;
	
	public SalableProductCollectionDetails(SalableProductCollection salableProductCollection) {
		super(salableProductCollection);
		accountingPeriod = formatUsingBusiness(salableProductCollection.getAccountingPeriod());
		cost = new CostDetails(salableProductCollection.getCost());
	}
	
	/**/
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_COST = "cost";
	
}