package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

@Getter @Setter
public class SalableProductCollectionDetails extends AbstractSalableProductCollectionDetails<SalableProductCollection> implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String accountingPeriod;
	@IncludeInputs(layout=Layout.VERTICAL) private CostDetails cost = new CostDetails(null);
	
	public SalableProductCollectionDetails(SalableProductCollection salableProductCollection) {
		super(salableProductCollection);
		setMaster(salableProductCollection);	
	}
	
	public void setMaster(SalableProductCollection salableProductCollection){
		super.setMaster(salableProductCollection);
		if(salableProductCollection==null){
			
		}else{
			accountingPeriod = formatUsingBusiness(salableProductCollection.getAccountingPeriod());
			if(cost == null)
				cost = new CostDetails(salableProductCollection.getCost());
			else
				cost.set(salableProductCollection.getCost());
		}
	}
	
	/**/
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_COST = "cost";

	@Override
	protected SalableProductCollection getSalableProductCollection() {
		return master;
	}
	
}