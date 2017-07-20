package org.cyk.system.company.model.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.cyk.system.company.model.stock.StockTangibleProductMovementSearchCriteria;
import org.cyk.system.root.model.search.DefaultSearchCriteria;
import org.cyk.system.root.model.search.StringSearchCriteria;

@Getter @Setter
public abstract class AbstractSaleStockTangibleProductMovementSearchCriteria extends DefaultSearchCriteria implements Serializable {

	private static final long serialVersionUID = 6796076474234170332L;

	protected Sale.SearchCriteria saleSearchCriteria;
	protected StringSearchCriteria externalIdentifierStringSearchCriteria = new StringSearchCriteria();
	protected StockTangibleProductMovementSearchCriteria stockTangibleProductMovementSearchCriteria;
	
	public AbstractSaleStockTangibleProductMovementSearchCriteria(){
		this(null,null);
	}
	
	public AbstractSaleStockTangibleProductMovementSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity,Boolean saleDone) {
		super(fromDate,toDate);
		saleSearchCriteria = new Sale.SearchCriteria(fromDate,toDate);
		stockTangibleProductMovementSearchCriteria = new StockTangibleProductMovementSearchCriteria(fromDate, toDate,minimumQuantity);
	}
	
	public AbstractSaleStockTangibleProductMovementSearchCriteria(Date fromDate,Date toDate) {
		this(fromDate,toDate,BigDecimal.ZERO,Boolean.TRUE);
	}
	
	public AbstractSaleStockTangibleProductMovementSearchCriteria(Date fromDate,Date toDate,BigDecimal minimumQuantity) {
		this(fromDate,toDate,minimumQuantity,Boolean.TRUE);
	}
	
	public AbstractSaleStockTangibleProductMovementSearchCriteria(Date fromDate,Date toDate,Boolean saleDone) {
		this(fromDate,toDate,BigDecimal.ZERO,Boolean.TRUE);
	}
	
	public AbstractSaleStockTangibleProductMovementSearchCriteria(AbstractSaleStockTangibleProductMovementSearchCriteria criteria){
		super(criteria);
		//saleSearchCriteria = new Sale.SearchCriteria(criteria.saleSearchCriteria);
		externalIdentifierStringSearchCriteria = new StringSearchCriteria(criteria.externalIdentifierStringSearchCriteria);
		stockTangibleProductMovementSearchCriteria = new StockTangibleProductMovementSearchCriteria(criteria.stockTangibleProductMovementSearchCriteria);
	}
	
	
}
