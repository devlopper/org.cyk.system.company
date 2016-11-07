package org.cyk.system.company.business.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.business.impl.CostDetails;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.root.business.impl.AbstractCollectionDetails;
import org.cyk.system.root.model.AbstractCollection;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs;
import org.cyk.utility.common.annotation.user.interfaces.IncludeInputs.Layout;
import org.cyk.utility.common.annotation.user.interfaces.Input;
import org.cyk.utility.common.annotation.user.interfaces.InputText;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSalableProductCollectionDetails<COLLECTION extends AbstractIdentifiable> extends AbstractCollectionDetails<COLLECTION> implements Serializable{
	
	private static final long serialVersionUID = -4741435164709063863L;
	
	@Input @InputText private String accountingPeriod;
	@IncludeInputs(layout=Layout.VERTICAL) private CostDetails cost = new CostDetails(null);
	
	public AbstractSalableProductCollectionDetails(COLLECTION salableProductCollection) {
		super(salableProductCollection);
		setMaster(salableProductCollection);	
	}
	
	public void setMaster(COLLECTION salableProductCollection){
		super.setMaster(salableProductCollection);
		if(salableProductCollection==null){
			
		}else{
			accountingPeriod = formatUsingBusiness(getSalableProductCollection().getAccountingPeriod());
			if(cost == null)
				cost = new CostDetails(getSalableProductCollection().getCost());
			else
				cost.set(getSalableProductCollection().getCost());
		}
	}
	
	protected abstract SalableProductCollection getSalableProductCollection();
	
	@Override
	public AbstractCollection<?> getCollection() {
		return getSalableProductCollection();
	}
	
	/**/
	
	public static final String FIELD_ACCOUNTING_PERIOD = "accountingPeriod";
	public static final String FIELD_COST = "cost";
	
}