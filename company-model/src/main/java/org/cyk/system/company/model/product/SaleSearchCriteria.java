package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.root.model.search.AbstractFieldValueSearchCriteriaSet;
import org.cyk.system.root.model.search.DateSearchCriteria;

@Getter @Setter
public class SaleSearchCriteria extends AbstractFieldValueSearchCriteriaSet implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private DateSearchCriteria fromDateSearchCriteria,toDateSearchCriteria;
	private Collection<BalanceType> balanceTypes = new ArrayList<>();
	private Boolean computeBalance=Boolean.TRUE;
	
	public SaleSearchCriteria(){
		this(null,null,null);
	}
	
	public SaleSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,null);
	}
	
	public SaleSearchCriteria(Date fromDate,Date toDate,BalanceType balanceType) {
		this.fromDateSearchCriteria = new DateSearchCriteria(fromDate);
		this.toDateSearchCriteria = new DateSearchCriteria(toDate);
		if(balanceType!=null)
			this.balanceTypes.add(balanceType);
	}
	
}
