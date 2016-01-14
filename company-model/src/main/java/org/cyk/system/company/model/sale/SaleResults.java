package org.cyk.system.company.model.sale;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.Cost;
import org.cyk.system.root.model.AbstractModelElement;

@Getter @Setter @Embeddable
public class SaleResults extends AbstractModelElement implements Serializable {

	private static final long serialVersionUID = 2700928054823690772L;

	@Embedded private Cost cost = new Cost();
	
	@Override
	public String getUiString() {
		return toString();
	}
	
	public Cost getCost(){
		if(cost==null)
			cost = new Cost();
		return cost;
	}

	@Override
	public String getLogMessage() {
		return String.format(LOG_FORMAT,cost.getLogMessage());
	}
	
	private static final String LOG_FORMAT = SaleResults.class.getSimpleName()+"(%s)";
	
	public static final String FIELD_COST = "cost";
}