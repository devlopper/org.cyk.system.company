package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.payment.BalanceType;
import org.cyk.system.root.model.search.DefaultSearchCriteria;

@Getter @Setter
public class SaleSearchCriteria extends DefaultSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	private Collection<BalanceType> balanceTypes = new ArrayList<>();
	private Collection<Customer> customers = new ArrayList<>();
	private Boolean done = Boolean.TRUE,hasAtLeastOneCashRegisterMovement;
	
	public SaleSearchCriteria(){
		this(null,null,null);
	}
	
	public SaleSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,null);
	}
	
	public SaleSearchCriteria(Date fromDate,Date toDate,BalanceType balanceType) {
		super(fromDate,toDate);
		if(balanceType!=null)
			this.balanceTypes.add(balanceType);
	}
	
	public SaleSearchCriteria(SaleSearchCriteria criteria){
		super(criteria);
		balanceTypes = new ArrayList<>(criteria.balanceTypes);
		customers = new ArrayList<>(criteria.customers);
		done = criteria.done;
	}
	
	public SaleSearchCriteria(DefaultSearchCriteria criteria){
		super(criteria);
	}
	
}
