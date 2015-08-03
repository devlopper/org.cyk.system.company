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
	protected Boolean done;
	
	public AbstractSaleStockSearchCriteria(){
		this(null,null);
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity,Boolean saleDone) {
		super(fromDate,toDate);
		this.minimumQuantity = minimumQuantity;
		this.done = saleDone;
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,BigDecimal.ZERO,Boolean.TRUE);
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity) {
		this(fromDate,toDate,minimumQuantity,Boolean.TRUE);
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate,Boolean saleDone) {
		this(fromDate,toDate,BigDecimal.ZERO,Boolean.TRUE);
	}
	
	/**/
	
	public static final String FIELD_MINIMUM_QUANTITY = "minimumQuantity";
}
