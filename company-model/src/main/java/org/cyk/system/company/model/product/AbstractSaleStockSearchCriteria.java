package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.model.search.StringSearchCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractSaleStockSearchCriteria extends DefaultSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	protected StringSearchCriteria externalIdentifierStringSearchCriteria = new StringSearchCriteria();
	protected BigDecimal minimumQuantity;
	protected Boolean done = Boolean.TRUE;
	
	public AbstractSaleStockSearchCriteria(){
		this(null,null);
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,BigDecimal.ZERO);
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity) {
		super(fromDate,toDate);
		this.minimumQuantity = BigDecimal.ZERO;
	}
	
}
