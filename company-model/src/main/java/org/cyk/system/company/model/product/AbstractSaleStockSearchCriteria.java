package org.cyk.system.company.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.model.search.StringSearchCriteria;

@Getter @Setter
public abstract class AbstractSaleStockSearchCriteria extends DefaultSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	protected SaleSearchCriteria saleSearchCriteria;
	protected StringSearchCriteria externalIdentifierStringSearchCriteria = new StringSearchCriteria();
	protected BigDecimal minimumQuantity;
	
	public AbstractSaleStockSearchCriteria(){
		this(null,null);
	}
	
	public AbstractSaleStockSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity,Boolean saleDone) {
		super(fromDate,toDate);
		this.minimumQuantity = minimumQuantity;
		saleSearchCriteria = new SaleSearchCriteria(fromDate,toDate);
		saleSearchCriteria.setDone(saleDone);
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
	
	public AbstractSaleStockSearchCriteria(AbstractSaleStockSearchCriteria criteria){
		super(criteria);
		saleSearchCriteria = new SaleSearchCriteria(criteria.saleSearchCriteria);
		externalIdentifierStringSearchCriteria = new StringSearchCriteria(criteria.externalIdentifierStringSearchCriteria);
		minimumQuantity = criteria.minimumQuantity;
	}
	
	/**/
	
	public static final String FIELD_MINIMUM_QUANTITY = "minimumQuantity";
}
