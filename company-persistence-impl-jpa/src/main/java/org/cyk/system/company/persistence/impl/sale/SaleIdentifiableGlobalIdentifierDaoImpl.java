package org.cyk.system.company.persistence.impl.sale;

import java.io.Serializable;

import org.cyk.system.company.model.sale.SaleIdentifiableGlobalIdentifier;
import org.cyk.system.company.persistence.api.sale.SaleIdentifiableGlobalIdentifierDao;
import org.cyk.system.root.model.search.AbstractFieldValueSearchCriteriaSet;
import org.cyk.system.root.persistence.impl.QueryStringBuilder;
import org.cyk.system.root.persistence.impl.QueryWrapper;
import org.cyk.system.root.persistence.impl.globalidentification.AbstractJoinGlobalIdentifierDaoImpl;
import org.cyk.utility.common.Constant;

public class SaleIdentifiableGlobalIdentifierDaoImpl extends AbstractJoinGlobalIdentifierDaoImpl<SaleIdentifiableGlobalIdentifier,SaleIdentifiableGlobalIdentifier.SearchCriteria> implements SaleIdentifiableGlobalIdentifierDao,Serializable {

	private static final long serialVersionUID = 6306356272165070761L;
	
	@Override
	protected String getReadByCriteriaQueryString() {
		return super.getReadByCriteriaQueryString()+Constant.CHARACTER_SPACE+"AND r.sale.identifier IN :"+QueryStringBuilder.VAR_IDENTIFIERS;
	}
	
	@Override
	protected void applySearchCriteriaParameters(QueryWrapper<?> queryWrapper,AbstractFieldValueSearchCriteriaSet searchCriteria) {
		super.applySearchCriteriaParameters(queryWrapper, searchCriteria);
		queryWrapper.parameter(QueryStringBuilder.VAR_IDENTIFIERS, ids(((SaleIdentifiableGlobalIdentifier.SearchCriteria)searchCriteria).getSales()));
	}
	
	
}
 