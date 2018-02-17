package org.cyk.system.company.persistence.impl.sale;

import java.util.Collection;

import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.model.sale.SaleProductInstance;
import org.cyk.system.company.persistence.api.sale.SaleProductInstanceDao;
import org.cyk.system.root.persistence.impl.AbstractTypedDao;

public class SaleProductInstanceDaoImpl extends AbstractTypedDao<SaleProductInstance> implements SaleProductInstanceDao {

	private static final long serialVersionUID = 6920278182318788380L;

	//private String readBySaleProduct,readBySalableProductInstanceCode;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		/*registerNamedQuery(readBySaleProduct, _select().where(SaleProductInstance.FIELD_SALE_PRODUCT));
		registerNamedQuery(readBySalableProductInstanceCode, _select().where(commonUtils.attributePath(SaleProductInstance.FIELD_SALABLE_PRODUCT_INSTANCE,
				SalableProductInstance.FIELD_GLOBAL_IDENTIFIER,GlobalIdentifier.FIELD_CODE), GlobalIdentifier.FIELD_CODE));
		*/
	}
	
	@Override
	public Collection<SaleProductInstance> readBySaleProduct(SalableProductCollectionItem saleProduct) {
		return null;//namedQuery(readBySaleProduct).parameter(SaleProductInstance.FIELD_SALE_PRODUCT, saleProduct).resultMany();
	}

	@Override
	public SaleProductInstance readBySalableProductInstanceCode(String code) {
		return null;//namedQuery(readBySalableProductInstanceCode).ignoreThrowable(NoResultException.class).parameter(GlobalIdentifier.FIELD_CODE, code).resultOne();
	}

}
