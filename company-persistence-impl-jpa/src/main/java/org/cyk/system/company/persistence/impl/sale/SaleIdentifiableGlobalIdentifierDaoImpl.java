package org.cyk.system.company.persistence.impl.sale;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.cyk.system.company.model.sale.Sale;
import org.cyk.system.company.model.sale.SaleIdentifiableGlobalIdentifier;
import org.cyk.system.company.persistence.api.sale.SaleIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.model.search.AbstractFieldValueSearchCriteriaSet;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.system.root.persistence.impl.globalidentification.AbstractJoinGlobalIdentifierDaoImpl;

public class SaleIdentifiableGlobalIdentifierDaoImpl extends AbstractJoinGlobalIdentifierDaoImpl<SaleIdentifiableGlobalIdentifier,SaleIdentifiableGlobalIdentifier.SearchCriteria> implements SaleIdentifiableGlobalIdentifierDao,Serializable {

	private static final long serialVersionUID = 6306356272165070761L;
	
	private String readBySales;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readBySales, _select().whereIdentifierIn(SaleIdentifiableGlobalIdentifier.FIELD_SALE));
	}
	
	/*@Override
	protected String getReadByCriteriaQueryString() {
		return super.getReadByCriteriaQueryString()+Constant.CHARACTER_SPACE+"AND r.sale.identifier IN :"+QueryStringBuilder.VAR_IDENTIFIERS;
	}*/
	
	@Override
	protected void applySearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractFieldValueSearchCriteriaSet searchCriteria) {
		super.applySearchCriteriaParameters(queryWrapper, searchCriteria);
		//queryWrapper.parameter(QueryStringBuilder.VAR_IDENTIFIERS, ids(((SaleIdentifiableGlobalIdentifier.SearchCriteria)searchCriteria).getSales()));
	}

	@Override
	public Collection<SaleIdentifiableGlobalIdentifier> readBySales(Collection<Sale> sales) {
		return namedQuery(readBySales).parameterIdentifiers(sales).resultMany();
	}

	@Override
	public Collection<SaleIdentifiableGlobalIdentifier> readBySale(Sale sale) {
		return readBySales(Arrays.asList(sale));
	}
	
	
}
 