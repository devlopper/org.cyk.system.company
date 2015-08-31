package org.cyk.system.company.persistence.impl.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.cyk.system.company.model.product.Sale;
import org.cyk.system.company.model.product.SaleStockInput;
import org.cyk.system.company.model.product.SaleStockInputSearchCriteria;
import org.cyk.system.company.persistence.api.product.SaleDao;
import org.cyk.system.company.persistence.api.product.SaleStockInputDao;
import org.cyk.system.root.model.search.AbstractPeriodSearchCriteria;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.utility.common.computation.ArithmeticOperator;
import org.cyk.utility.common.computation.LogicalOperator;

public class SaleStockInputDaoImpl extends AbstractSaleStockDaoImpl<SaleStockInput,SaleStockInputSearchCriteria> implements SaleStockInputDao {

	private static final long serialVersionUID = 6920278182318788380L;

	private String readBySaleComputedIdentifier,readBySales;
	
	@Inject private SaleDao saleDao;
	
	@Override
    protected void namedQueriesInitialisation() {
    	super.namedQueriesInitialisation();
    	
    	QueryStringBuilder queryStringBuilder = _select().where("externalIdentifier",ArithmeticOperator.LIKE)
			.and().between("sale.date").where(LogicalOperator.AND,"sale.done","saleDone",ArithmeticOperator.EQ)
			.and("remainingNumberOfGoods", "minimumRemainingGoods", ArithmeticOperator.GTE)
			.and().whereString("ABS(r.tangibleProductStockMovement.quantity) >= :minimumQuantity");
    	
    	String readByCriteriaDateAscendingOrderQuery = queryStringBuilder.orderBy("sale.date", Boolean.TRUE).getValue();
    	registerNamedQuery(readByCriteria,readByCriteriaDateAscendingOrderQuery);
        registerNamedQuery(readByCriteriaDateAscendingOrder,readByCriteriaDateAscendingOrderQuery );
        registerNamedQuery(readByCriteriaDateDescendingOrder,queryStringBuilder.orderBy("sale.date", Boolean.FALSE));
        
        registerNamedQuery(readBySaleComputedIdentifier,_select().where("sale."+Sale.FIELD_COMPUTED_IDENTIFIER,Sale.FIELD_COMPUTED_IDENTIFIER) );
        registerNamedQuery(readBySales,_select().whereIdentifierIn("sale") );
        
    }

	@Override
	public Collection<SaleStockInput> readByCriteria(SaleStockInputSearchCriteria searchCriteria) {
		if(StringUtils.isNotBlank(searchCriteria.getIdentifierStringSearchCriteria().getPreparedValue())){
			Sale sale = saleDao.readByComputedIdentifier(searchCriteria.getIdentifierStringSearchCriteria().getPreparedValue());
			if(sale==null)
				return new ArrayList<SaleStockInput>();
			SaleStockInput saleStockInput = readBySale(sale);
			if(saleStockInput==null)
				return new ArrayList<SaleStockInput>();
			return Arrays.asList(saleStockInput);
		}
		return super.readByCriteria(searchCriteria);
	}
	
	@Override
	protected void applyPeriodSearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractPeriodSearchCriteria searchCriteria) {
		super.applyPeriodSearchCriteriaParameters(queryWrapper, searchCriteria);
		SaleStockInputSearchCriteria saleStockInputSearchCriteria = (SaleStockInputSearchCriteria) searchCriteria;
		
		queryWrapper.parameter("minimumRemainingGoods", saleStockInputSearchCriteria.getMinimumRemainingGoodsCount());
	}

	@Override
	public Collection<SaleStockInput> readBySales(Collection<Sale> sales) {
		if(sales==null || sales.isEmpty())
			return new ArrayList<SaleStockInput>();
		return namedQuery(readBySales).parameterIdentifiers(sales).resultMany();
	}

	@Override
	public SaleStockInput readBySale(Sale sale) {
		if(sale==null)
			return null;
		Collection<SaleStockInput> collection = readBySales(Arrays.asList(sale));
		return collection.isEmpty()?null:collection.iterator().next();
	}
	
	
}
