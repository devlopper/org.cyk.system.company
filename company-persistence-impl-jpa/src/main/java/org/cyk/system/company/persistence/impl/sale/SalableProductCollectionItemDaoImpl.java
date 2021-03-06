package org.cyk.system.company.persistence.impl.sale;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.system.company.model.Cost;
import org.cyk.system.company.model.sale.SalableProduct;
import org.cyk.system.company.model.sale.SalableProductCollection;
import org.cyk.system.company.model.sale.SalableProductCollectionItem;
import org.cyk.system.company.persistence.api.sale.SalableProductCollectionItemDao;
import org.cyk.system.root.persistence.impl.AbstractCollectionItemDaoImpl;
@Deprecated
public class SalableProductCollectionItemDaoImpl extends AbstractCollectionItemDaoImpl<SalableProductCollectionItem,SalableProductCollection> implements SalableProductCollectionItemDao,Serializable {

	private static final long serialVersionUID = 6306356272165070761L;

	private String readByCollectionBySalableProduct,sumCostAttributesBySalableProductCollection;
	
	@Override
	protected void namedQueriesInitialisation() {
		super.namedQueriesInitialisation();
		registerNamedQuery(readByCollectionBySalableProduct, _select().where(SalableProductCollectionItem.FIELD_COLLECTION)
				.and(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT));
		registerNamedQuery(sumCostAttributesBySalableProductCollection, "SELECT NEW "+
				Cost.class.getName()+"(SUM(r.cost.numberOfProceedElements),SUM(r.cost.value),SUM(r.cost.tax),SUM(r.cost.turnover),SUM(r.cost.reduction),SUM(r.cost.commission))"
				+ " FROM SalableProductCollectionItem r WHERE r.collection = :collection");
	}
	
	@Override
	public Collection<SalableProductCollectionItem> readByCollectionBySalableProduct(SalableProductCollection salableProductCollection, SalableProduct salableProduct) {
		return namedQuery(readByCollectionBySalableProduct).parameter(SalableProductCollectionItem.FIELD_COLLECTION, salableProductCollection)
				.parameter(SalableProductCollectionItem.FIELD_SALABLE_PRODUCT, salableProduct).resultMany();
	}

	@Override
	public Cost sumCostBySalableProductCollection(SalableProductCollection salableProductCollection) {
		return namedQuery(sumCostAttributesBySalableProductCollection,Cost.class).parameter(SalableProductCollectionItem.FIELD_COLLECTION, salableProductCollection)
				.nullValue(new Cost()).resultOne();
	}
	
	
	
}
 